/**
 * Picture Manager Class
 */
package cmpt276.assignment.myapplication.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//code taken from Pritpals Assignment 2, the lensmanager class
public class ListOfFlickr implements Iterable<PictureItem> {

    private List<PictureItem> All;

    //making it a singleton
    public static ListOfFlickr instance;

    public static void refresh(){
        instance = null;
        return;
    }

    public static ListOfFlickr getInstance() {
        if(instance == null){
            instance = new ListOfFlickr();
        }

        return instance;
    }

    private ListOfFlickr() {
        All = new ArrayList<PictureItem>();
    }

    //regular code below

    public void add(PictureItem i){
        All.add(i);
    }

    public void remove(int pos) {
        All.remove(pos);
    }

    public Bitmap getAtBitmapAtPos(int index) {
        return  All.get(index).getbmap();
    }


    public int getLength(){
        return All.size();
    }

    @Override
    public Iterator<PictureItem> iterator() {
        return All.iterator();
    }


}
