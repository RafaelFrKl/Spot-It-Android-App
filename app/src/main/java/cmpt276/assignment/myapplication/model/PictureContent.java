/**
 * Used for loading the images into the view
 */
package cmpt276.assignment.myapplication.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PictureContent {
    public static final List<PictureItem> ITEMS = new ArrayList<>();

    public static void loadSavedImages(File dir) {
        ITEMS.clear();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                String absolutePath = file.getAbsolutePath();
                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
                if (extension.equals(".jpg")) {
                    loadImage(file);
                }
            }
        }
    }

    public static void deleteSavedImages(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                String absolutePath = file.getAbsolutePath();
                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
                if (extension.equals(".jpg")) {
                    file.delete();
                }
            }
        }
        ITEMS.clear();
    }

    public static void loadImage(File file) {
        try {
            PictureItem newItem = new PictureItem();
            //newItem.uri = Uri.fromFile(file);
            newItem.bmap = BitmapFactory.decodeStream(new FileInputStream(file));
            ListOfFlickr list = ListOfFlickr.getInstance();
            newItem.pos = list.getLength();
            list.add(newItem);
            addItem(newItem);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static void loadStorageImage(String path) {
        try {
            File file = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static void addItem(PictureItem item) {
        ITEMS.add(0, item);
    }
}