package nl.mprog.projects.nPuzzle10348190;

import android.graphics.Bitmap;


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

    public void set_image(Bitmap im){
        IMAGE = im;
    }

    public Bitmap get_image(){
        return IMAGE;
    }
}
