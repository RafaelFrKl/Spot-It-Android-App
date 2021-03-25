/**
 * Main Menu of the game
 */
package cmpt276.assignment.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cmpt276.assignment.myapplication.model.ListOfFlickr;
import cmpt276.assignment.myapplication.model.PictureContent;

public class MainMenu extends AppCompatActivity {

    //variables for using local Gallery
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final int PICK_IMAGE = 100;

    //Creates an intent to switch to a different activity
    public static Intent fromUsernameToMenu(Context c){
        return new Intent(c, MainMenu.class);
    }

    //On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // code taken from https://stackoverflow.com/questions/51374551/read-and-write-external-storage-permission-isnt-working
        // cannot export cards without this permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_main_menu);
        setTitle("Welcome to Spot It!");
        ListOfFlickr refresh = ListOfFlickr.getInstance();
        refresh.refresh();
        PictureContent.loadSavedImages(new File(SavedImagesActivity.loadData(this)));
        //Creates button functionality
        registerClickCallback();
    }

    //Creates button functionality
    private void registerClickCallback(){
        //Get Play Game button
        Button play = (Button) findViewById(R.id.playbtn);
        //Creates Play button Functionality
        play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Goes to selected activity
                Intent i = GameScreen.fromMenutoGame(MainMenu.this);
                startActivity(i);
            }
        });

        //Get High Scores button
        Button highscores = (Button) findViewById(R.id.highscoresbtn);
        //Creates High Score button Functionality
        highscores.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Goes to selected activity
                Intent i = HighScores.fromMenuToHighScores(MainMenu.this);
                startActivity(i);
            }
        });

        //Get Options button
        Button options = (Button) findViewById(R.id.optionsbtn);
        //Creates Options button Functionality
        options.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Goes to selected activity
                Intent i = OptionsScreen.fromMenuToOptions(MainMenu.this);
                startActivity(i);
            }
        });

        //Get Help button
        Button help = (Button) findViewById(R.id.helpbtn);
        //Creates Help button Functionality
        help.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Goes to selected activity
                Intent i = HelpScreen.fromMenuToHelp(MainMenu.this);
                startActivity(i);
            }
        });

        //Get photo button
        Button flickr = (Button) findViewById(R.id.picbtn);
        //Creates photo button Functionality
        flickr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Goes to selected activity
                Intent i = PhotoGalleryActivity.fromMenuToFlickr(MainMenu.this);
                startActivity(i);
            }
        });

        //Get saved Images from Flickr button
        Button SavedFlickr = (Button) findViewById(R.id.savedimgbtn);
        //Creates Flickr Image Gallery functionality
        SavedFlickr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Goes to saved images gallery
                Intent i = SavedImagesActivity.fromMenuToSavedImages(MainMenu.this);
                startActivity(i);
            }
        });

        //Get Local image Gallery button
        Button localGallery = (Button) findViewById(R.id.localgalleybtn);
        localGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

                Toast toast = Toast. makeText(getApplicationContext(), "Long press image to select multiple images", Toast. LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            if(data.getData()!= null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    Bitmap resized = getResizedBitmap(bitmap, 130, 130);
                    String path = saveToInternalStorage(resized);
                    saveData(path);

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                if(data.getClipData()!= null) {
                    ClipData mClipData=data.getClipData();
                    for(int i=0;i<mClipData.getItemCount();i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bitmap resized = getResizedBitmap(bitmap, 130, 130);
                        String path = saveToInternalStorage(resized);
                        saveData(path);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(MainMenu.this);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(cw.getFilesDir().getName(), MODE_PRIVATE);
        long ts = System.currentTimeMillis();
        String fileName = ts + ".jpg";

        File mypath = new File(directory,fileName);
        //counter++;

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            //Use the compress method on the BitMap object to write image to the OutputStream
            //bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 60, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public void saveData(String path) {
        SharedPreferences sharedPreferences = MainMenu.this.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("filePathKey", path);
        editor.apply();
    }
}
