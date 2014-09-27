package nl.mprog.projects.nPuzzle10348190;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class Home extends ActionBarActivity {

    private static GridView VIEW;
    private static int COUNT_IMAGES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final GridView imageView = (GridView)findViewById(R.id.imageView);
        VIEW = imageView;

        set_num();
        // the metrics are used to resize the images
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        imageView.setOnItemClickListener(respondToClick);
        imageView.setAdapter(new HomeAdapter(COUNT_IMAGES, imageView.getContext(), metrics));
}

    public AdapterView.OnItemClickListener respondToClick = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String name = "puzzle_" + i;
            int drawableResourceId = adapterView.getContext().getResources().getIdentifier(name, "drawable", "nl.mprog.projects.nPuzzle10348190");
            Intent intent = new Intent(view.getContext(), Game.class);
            intent.putExtra("IMAGE_ID", drawableResourceId);
            intent.putExtra("DIFFICULTY", 0);
            startActivity(intent);
        }
    };
    /*
    This function sets the number of images to be displayed on the honme screen.
     */
    private void set_num(){
        boolean check = false;
        int countImages = 0;
        while(!check){
            String name = "puzzle_" + countImages;
            int id = VIEW.getContext().getResources().getIdentifier(name, "drawable", "nl.mprog.projects.nPuzzle10348190");
            countImages += 1;
            if(id == 0){
                check = true;
            }
        }
        COUNT_IMAGES = countImages-1;
    }

}
