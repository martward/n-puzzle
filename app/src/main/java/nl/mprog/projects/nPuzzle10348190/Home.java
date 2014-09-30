package nl.mprog.projects.nPuzzle10348190;

/*
Martijn Wardenaar
10348190
martijnwardenaar@gmail.com

This activity is opened when the app is opened, or when te user decided to return to this menu from
the game or the score screen. This activity checks if a preferred difficulty is saved, and otherwise
sets it to easy. If it is saved it will load this difficulty. It then checks whether a saved game is
present and starts a new intent to load the game if this is the case. If this is not the case the
activity will show the images which are available to the user. The user can select an image by
simply tapping it.
*/

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class Home extends ActionBarActivity {

    private static GridView VIEW;
    private static int COUNT_IMAGES;
    private static int DIFFICULTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Starting Home");
        handle_preferences();

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
            intent.putExtra("DIFFICULTY", DIFFICULTY);
            intent.putExtra("LOAD", false );
            startActivity(intent);
            finish();
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

    /*
    This function checks if a game state is stored in AppData by checking if currentState is set.
    If so, Game.class will be called with the message to load a previous game.
    Otherwise the function will check if the difficulty is set and load it or set it depending on
    the check.
     */
    public void handle_preferences(){
        SharedPreferences pref = getSharedPreferences("AppData", MODE_PRIVATE);

        if(!pref.contains("currentState")){
            if(!pref.contains("difficulty")){
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("difficulty", 0);
                editor.commit();
                DIFFICULTY = 0;
            } else{
                DIFFICULTY = pref.getInt("difficulty", DIFFICULTY);
            }
        } else {
            Intent loadGame = new Intent(this.getBaseContext(),Game.class);
            loadGame.putExtra("LOAD", true );
            startActivity(loadGame);
            finish();
        }

    }

}
