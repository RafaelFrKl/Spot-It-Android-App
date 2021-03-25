/**
 * Options Menu Screen. which provides the user with different sets of images to choose from.
 */
package cmpt276.assignment.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cmpt276.assignment.myapplication.model.ListOfFlickr;
import cmpt276.assignment.myapplication.model.PictureContent;

public class OptionsScreen extends AppCompatActivity {
    //Creates an intent to switch to a different activity
    public static Intent fromMenuToOptions(Context c) {
        Intent intent = new Intent(c, OptionsScreen.class);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_screen);
        setTitle(getString(R.string.options));
        ListOfFlickr refresh = ListOfFlickr.getInstance();
        refresh.refresh();
        PictureContent.loadSavedImages(new File(SavedImagesActivity.loadData(this)));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setupRadioButtonsForImageSet();
        setupRadioButtonsForMode();
        setupRadioButtonsForExport();
        setupRadioButtonsForDiff();
        setupExportButton();
        setupImagesPerCard();
        setupDrawPileSize();

        int testnum = refresh.getLength();
        if(OptionsScreen.getMode(this) == 2){
            if(OptionsScreen.getNumImagesPerCard(this) == 3 && testnum < 7){
                SharedPreferences mode = getSharedPreferences("mode", MODE_PRIVATE);
                SharedPreferences.Editor edit= mode.edit();
                edit.putInt("mode", 0);
                edit.apply();
            }
            if(OptionsScreen.getNumImagesPerCard(this) == 4 && testnum < 13){
                SharedPreferences mode = getSharedPreferences("mode", MODE_PRIVATE);
                SharedPreferences.Editor edit= mode.edit();
                edit.putInt("mode", 0);
                edit.apply();

            }
            if(OptionsScreen.getNumImagesPerCard(this) == 6 && testnum < 31){
                SharedPreferences mode = getSharedPreferences("mode", MODE_PRIVATE);
                SharedPreferences.Editor edit= mode.edit();
                edit.putInt("mode", 0);
                edit.apply();
            }
        }


    }

    private void setupExportButton() {
        Button btn = findViewById(R.id.exportbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }
    private void openGallery() {
        // some the below code in this function
        // is used from https://stackoverflow.com/questions/28195788/open-a-specific-folder-inside-gallery-android
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath() + "/saved_images");
        gallery.setDataAndType(uri,"image/*");
        startActivity(Intent.createChooser(gallery, "Open folder"));

        Toast.makeText(this,"Show internal storage and navigate to Pictures/saved_images",
                Toast.LENGTH_LONG).show();
    }

    private void setupRadioButtonsForExport() {
        RadioGroup btns = findViewById(R.id.exportsetting);

        RadioButton export1 = new RadioButton(this);

        export1.setText(R.string.no);
        export1.setTextColor(Color.WHITE);
        export1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExport(0);
            }
        });
        btns.addView(export1);

        RadioButton export2 = new RadioButton(this);

        export2.setText(R.string.yes);
        export2.setTextColor(Color.WHITE);
        export2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExport(1);
            }
        });
        btns.addView(export2);

        if(getExport(this) == 0){
            export1.setChecked(true);
        }
        if(getExport(this) == 1){
            export2.setChecked(true);
        }
    }

    public void saveExport(int number){
        SharedPreferences mode = getSharedPreferences("export", MODE_PRIVATE);
        SharedPreferences.Editor edit= mode.edit();
        edit.putInt("export", number);
        edit.apply();
    }
    public static int getExport(Context c){
        SharedPreferences numImagesPerCard= c.getSharedPreferences("export", MODE_PRIVATE);
        return numImagesPerCard.getInt("export",1);
    }

    private void setupRadioButtonsForDiff() {
        RadioGroup btns = findViewById(R.id.diffsettings);

        RadioButton diff1 = new RadioButton(this);

        diff1.setText(R.string.easy);
        diff1.setTextColor(Color.WHITE);
        diff1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDifficulty(0);
            }
        });
        btns.addView(diff1);

        RadioButton diff2 = new RadioButton(this);

        diff2.setText(R.string.normal);
        diff2.setTextColor(Color.WHITE);
        diff2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDifficulty(1);
            }
        });
        btns.addView(diff2);

        RadioButton diff3 = new RadioButton(this);

        diff3.setText(R.string.hard);
        diff3.setTextColor(Color.WHITE);
        diff3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDifficulty(2);
            }
        });
        btns.addView(diff3);

        if(getDifficulty(this) == 0){
            diff1.setChecked(true);
        }
        if(getDifficulty(this) == 1){
            diff2.setChecked(true);
        }
        if(getDifficulty(this) == 2){
            diff3.setChecked(true);
        }
    }

    public void saveDifficulty(int number){
        SharedPreferences mode = getSharedPreferences("difficulty", MODE_PRIVATE);
        SharedPreferences.Editor edit= mode.edit();
        edit.putInt("difficulty", number);
        edit.apply();
    }
    public static int getDifficulty(Context c){
        SharedPreferences numImagesPerCard= c.getSharedPreferences("difficulty", MODE_PRIVATE);
        return numImagesPerCard.getInt("difficulty",1);
    }

    private void setupRadioButtonsForMode() {
        RadioGroup btns = findViewById(R.id.gamemode);

        RadioButton mode1 = new RadioButton(this);

        mode1.setText(R.string.imageonly);
        mode1.setTextColor(Color.WHITE);
        mode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMode(0);
            }
        });
        btns.addView(mode1);

        RadioButton mode2 = new RadioButton(this);
        mode2.setText(R.string.imageandtext);
        mode2.setTextColor(Color.WHITE);
        mode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMode(1);
            }
        });
        btns.addView(mode2);

        ListOfFlickr list = ListOfFlickr.getInstance();
        int flickrDeckSize = list.getLength();
        int currentDeck = getNumImagesPerCard(this);


        TextView text = findViewById(R.id.warningtext);
        TextView numText = findViewById(R.id.numimages);
        numText.setText(getString(R.string.numcurrentsaved) + flickrDeckSize);


        RadioButton mode3 = new RadioButton(this);
        mode3.setText(R.string.Flickr_and_Local);
        mode3.setTextColor(Color.WHITE);

        mode3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMode(2);
            }
        });
        if( currentDeck == 3 && flickrDeckSize < 7){
            text.setText(R.string.warning1 );
            text.setTextColor(Color.RED);
            mode3.setClickable(false);
            mode3.setChecked(false);
        }
        if( currentDeck == 4 && flickrDeckSize < 13){
            text.setText(R.string.warning2 );
            text.setTextColor(Color.RED);
            mode3.setClickable(false);
            mode3.setChecked(false);
        }
        if( currentDeck == 6 && flickrDeckSize < 31){
            text.setText(R.string.warning3 );
            text.setTextColor(Color.RED);
            mode3.setClickable(false);
            mode3.setChecked(false);
        }

        btns.addView(mode3);

        if(getMode(this) == 0){
            mode1.setChecked(true);
        }
        if(getMode(this) == 1){
            mode2.setChecked(true);
        }
        if(getMode(this) == 2){
            mode3.setChecked(true);
        }
        System.out.println("Kiiiiiiiiiiiiiiiiiiiiir");
    }
    public void saveMode(int number){
        SharedPreferences mode = getSharedPreferences("mode", MODE_PRIVATE);
        SharedPreferences.Editor edit= mode.edit();
        edit.putInt("mode", number);
        edit.apply();
    }
    public static int getMode(Context c){
        SharedPreferences numImagesPerCard= c.getSharedPreferences("mode", MODE_PRIVATE);
        return numImagesPerCard.getInt("mode",0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    private void saveImageSelection(int set){
        SharedPreferences imageSet = getSharedPreferences("Image_Selection", MODE_PRIVATE);
        SharedPreferences.Editor edit = imageSet.edit();
        edit.putInt("The_Set",set);
        edit.apply();
    }
    public static int getImageSet(Context context){
        SharedPreferences set = context.getSharedPreferences("Image_Selection",MODE_PRIVATE);
        return set.getInt("The_Set", 0);
    }

    private void saveNumImagesPerCard(int number){
        SharedPreferences numImagesPerCard = getSharedPreferences("NUM_Images_Per_Card", MODE_PRIVATE);
        SharedPreferences.Editor edit= numImagesPerCard.edit();
        edit.putInt("NumberOFIMages", number);
        edit.apply();
    }
    public static int getNumImagesPerCard(Context c){
        SharedPreferences numImagesPerCard= c.getSharedPreferences("NUM_Images_Per_Card", MODE_PRIVATE);
        return numImagesPerCard.getInt("NumberOFIMages",3);
    }
    private void saveMaxDrawPileSize(int maxsize){
        SharedPreferences drawPileMax = getSharedPreferences("Draw_Pile_Max", MODE_PRIVATE);
        SharedPreferences.Editor edit = drawPileMax.edit();
        edit.putInt("Max_Size",maxsize);
        edit.apply();
    }
    public static int getDrawPileMaxSize(Context c){
        SharedPreferences numImagesPerCard= c.getSharedPreferences("Draw_Pile_Max", MODE_PRIVATE);
        return numImagesPerCard.getInt("Max_Size",7);
    }

    private void saveDrawPileSize(int size){
        SharedPreferences drawPileSize = getSharedPreferences("drawPileSize",MODE_PRIVATE);
        SharedPreferences.Editor edit = drawPileSize.edit();
        edit.putInt("SizeToUse", size);
        edit.apply();
    }


    public static int getDrawPileSize(Context c){
        SharedPreferences drawPileSize = c.getSharedPreferences("drawPileSize",MODE_PRIVATE);
        return drawPileSize.getInt("SizeToUse", 5);
    }

    private void setupImagesPerCard(){
        int[] Options = getResources().getIntArray(R.array.ImagesPerCard);
        int[] maxDrawPile = getResources().getIntArray(R.array.MaxDrawPile);
        RadioGroup imagesPerCard = (RadioGroup) findViewById(R.id.NumImagesPerCard);
        for(int i=0; i< 3; i++){
            RadioButton option = new RadioButton(this);
            option.setText(""+Options[i]+" "+getResources().getString(R.string.Images_Per_Card));
            option.setTextColor(Color.WHITE);
            int finalI = i;
            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveNumImagesPerCard(Options[finalI]);
                    saveMaxDrawPileSize(maxDrawPile[finalI]);
                    //finish();
                   // startActivity(getIntent());
                    OptionsScreen.this.recreate();
                }
            });
            imagesPerCard.addView(option);
            if(Options[i]== getNumImagesPerCard(this)){
                option.setChecked(true);
            }
        }

    }

    private void setupDrawPileSize(){
        int[] drawPileOptions = getResources().getIntArray(R.array.DrawPileSize);
        RadioGroup drawPileSize = (RadioGroup) findViewById(R.id.DrawPileSize);
        for(int i = 0; i<= drawPileOptions.length; i++){
            RadioButton option= new RadioButton((this));
            if(i<4) {
                option.setText("" + drawPileOptions[i] + " " + getResources().getString(R.string.Cards));
                option.setTextColor(Color.WHITE);
            }
            else{
                option.setText(R.string.All);
                option.setTextColor(Color.WHITE);
            }
            int finalI = i;
            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalI<4) {
                        saveDrawPileSize(drawPileOptions[finalI]);
                    }
                    else{
                        saveDrawPileSize(getDrawPileMaxSize(getApplicationContext()));
                    }
                }
            });
            drawPileSize.addView(option);
            if(i<4) {
                if (drawPileOptions[i] == getDrawPileSize(this)) {
                    option.setChecked(true);
                }
            }
            if(i==4){
                if(getDrawPileSize(this)==getDrawPileMaxSize(this)){
                    option.setChecked(true);
                }
            }
            if(i<4) {
                if (getDrawPileMaxSize(this) < drawPileOptions[i]) {
                    option.setClickable(false);
                    option.setChecked(false);
                }
            }
            if(i==0){
                if(getDrawPileSize(this)>getDrawPileMaxSize(this)){
                    option.setChecked(true);
                    saveDrawPileSize(drawPileOptions[0]);
                }
            }
        }
    }

    private void setupRadioButtonsForImageSet(){
        int flickrDeckSize = 0; //Get through shared preferences

        String[] Names=getResources().getStringArray(R.array.options);
        int[] Selection={0,31};
        RadioGroup selectionChoice = (RadioGroup) findViewById(R.id.RadioGP);
        for(int i=0; i<2; i++){
            RadioButton option= new RadioButton(this);
            option.setText(Names[i]);
            option.setTextColor(Color.WHITE);
            int finalI = i;
            option.setOnClickListener(new View.OnClickListener() {
                final int kit = finalI;
                @Override
                public void onClick(View v) {
                    saveImageSelection(Selection[finalI]);
                }
            });
            selectionChoice.addView(option);

            if(Selection[i]== getImageSet(this)){
                option.setChecked(true);
            }
        }
    }
}