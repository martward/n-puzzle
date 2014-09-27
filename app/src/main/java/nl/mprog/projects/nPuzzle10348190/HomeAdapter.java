package nl.mprog.projects.nPuzzle10348190;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class HomeAdapter extends BaseAdapter {

    private static int COUNT_IMAGES;
    private static Context CONTEXT;
    private static Bitmap[] IMAGES;

    HomeAdapter(int count, Context context, DisplayMetrics metrics ){
        COUNT_IMAGES = count;
        CONTEXT = context;
        setIMAGES(metrics);
    }

    @Override
    public int getCount() {
        return COUNT_IMAGES;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    /*
    This function returns an imageView showing a picture which could be used to
    play the game.
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(viewGroup.getContext());
        imageView.setImageBitmap(IMAGES[i]);
        return imageView;
    }

    /*
    This function stores a bitmap of all images.
    This is used to minimize the working load on the home screen.
     */
    private void setIMAGES(DisplayMetrics metrics){
        Bitmap[] images = new Bitmap[COUNT_IMAGES];
        for(int i = 0; i< COUNT_IMAGES; i++){
            String name = "puzzle_" + i;
            int id = CONTEXT.getResources().getIdentifier(name, "drawable", "nl.mprog.projects.nPuzzle10348190");
            Bitmap temp = BitmapFactory.decodeResource(CONTEXT.getResources(), id);
            images[i] = img_resize(temp, metrics);
        }
        IMAGES = images;
    }

    /*
    This function takes a bitmap and the screen metrics and resizes the images to fit the screen width.
    The bitmap is then returned.
     */
    private Bitmap img_resize(Bitmap img, DisplayMetrics metrics){
        int width = metrics.widthPixels;
        int imageWidth = img.getWidth();
        int imageHeight = img.getHeight();
        int newImageWidth = imageWidth;
        int newImageHeight = imageHeight;
        if(imageWidth > width){
            newImageWidth = width-25;
            newImageHeight = (int)(((newImageWidth * 1.00) / imageWidth) * imageHeight);
        }
        Bitmap imgResized = Bitmap.createScaledBitmap(img,newImageWidth,newImageHeight,true);
        return imgResized;
    }
}
