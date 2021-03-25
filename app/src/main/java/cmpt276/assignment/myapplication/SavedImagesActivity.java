/**
 * Displays Images downloaded from Flickr
 */
package cmpt276.assignment.myapplication;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

import cmpt276.assignment.myapplication.model.ListOfFlickr;
import cmpt276.assignment.myapplication.model.PictureContent;
import cmpt276.assignment.myapplication.model.PictureItem;

import static cmpt276.assignment.myapplication.model.PictureContent.deleteSavedImages;
import static cmpt276.assignment.myapplication.model.PictureContent.loadSavedImages;

public class SavedImagesActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener{
    private SavedImagesActivity context;
    private DownloadManager downloadManager;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    private static final String SHARED_PREFS = "sharedPrefs";
    String filePath;

    //Creates an intent to switch to a different activity
    public static Intent fromMenuToSavedImages(Context c) {
        Intent intent = new Intent(c, SavedImagesActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);
        setTitle(getString(R.string.savedimages));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
        context = this;
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        filePath = loadData(this);

        if (recyclerViewAdapter == null) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            recyclerView = (RecyclerView) currentFragment.getView();
            recyclerViewAdapter = ((RecyclerView) currentFragment.getView()).getAdapter();
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }

        PictureContent.loadImage(new File(filePath));
        PictureContent.loadStorageImage(filePath);
        recyclerViewAdapter.notifyItemInserted(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            deleteSavedImages(new File(filePath));
            ListOfFlickr list = ListOfFlickr.getInstance();
            list.refresh();
            recyclerViewAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadSavedImages(new File(filePath));
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString("filePathKey", "");
        return text;
    }

    @Override
    public void onListFragmentInteraction(PictureItem item) {
        // This is where you'd handle clicking an item in the list
        ListOfFlickr list = ListOfFlickr.getInstance();
        //list.remove(item.pos);
        Toast toast = Toast.makeText(this, "Image Removed", Toast.LENGTH_SHORT);
        toast.show();

        //put code here to remove it from the file
        String path = loadData(this);
        File folder = new File(path);
        File[] files = folder.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //files[i]
                Bitmap testmap = BitmapFactory.decodeFile(files[i].getAbsolutePath());

                if(list.getAtBitmapAtPos(item.pos).sameAs(testmap)){
                   // System.out.println("ITS GOOD");
                    files[i].delete();

                }
            }
        }
        this.recreate();
    }
}