/**
 * A class that contains all the information needed for downloaded images
 */
package cmpt276.assignment.myapplication.model;

import android.graphics.Bitmap;

public class PictureItem {
    public Bitmap bmap;
    public int pos;
    //1 = deleted, 0 =
    public boolean state = false;

    //assigning id
    int id;

    public Bitmap getbmap(){
        return bmap;
    }
}
