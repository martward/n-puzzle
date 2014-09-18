package nl.mprog.projects.nPuzzle10348190;

/**
 * Created by Martijn on 9/18/2014.
 */

import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.Image;


public class Tile {
    private int ID;
    private Bitmap IMAGE;

    Tile(int Id, Bitmap Im){
        set_id(Id);
        set_image(Im);
    }

    public void set_id(int id){
        ID = id;
    }

    public int get_id(){
        return ID;
    }

    public void set_image(Bitmap im){
        IMAGE = im;
    }

    public Bitmap get_image(){
        return IMAGE;
    }
}
