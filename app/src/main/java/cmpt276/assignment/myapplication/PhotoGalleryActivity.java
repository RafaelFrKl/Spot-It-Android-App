/**
 * Displays the Flickr Images
 */
package cmpt276.assignment.myapplication;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class PhotoGalleryActivity extends SavedImagesFragment {

    //Creates an intent to switch to a different activity
    public static Intent fromMenuToFlickr(Context c){
        return new Intent(c, PhotoGalleryActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
