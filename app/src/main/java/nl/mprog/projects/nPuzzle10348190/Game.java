package nl.mprog.projects.nPuzzle10348190;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.os.Handler;

import java.util.Map;
import java.util.Set;


public class Game extends ActionBarActivity {

    private static int IMAGE_ID;
    private static int DIFFICULTY;
    private static int TILE_HEIGHT;
    private static int TILE_WIDTH;
    private static Tile[] ALL_TILES;
    private static Bitmap IMAGE;
    private static GameAdapter ADAPTER;
    private static Puzzle PUZZLE;
    private static int NUMBER_MOVES;
    private static long START_TIME;
    private static GridView GAME_VIEW;
    private static int PASSED_TIME;
    private static boolean BACK_PRESSED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        GAME_VIEW = (GridView)findViewById(R.id.gameView);
        Intent intent = getIntent();
        boolean load = intent.getBooleanExtra("LOAD",false);
        Context context = getApplicationContext();
        if(!load){
            initialize_game(context);
        } else{
            load_game(context);
        }
    }


    /*
    This function creates all the tiles for playing the game.
    These tiles are objects with an id and an image, which is a subimage of the total image which
    was selected on the home screen.
     */
    private void set_tiles(){
        int numColumns = DIFFICULTY + 3;
        int numCells = numColumns * numColumns;
        GAME_VIEW.setNumColumns(numColumns);
        Tile[] allTiles = new Tile[numCells];

        int countTiles = 0;
        for(int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numColumns; j++) {
                if(countTiles < numCells -1) {
                    Bitmap subImage = Bitmap.createBitmap(IMAGE, (j * TILE_WIDTH), (i * TILE_HEIGHT), TILE_WIDTH, TILE_HEIGHT);
                    allTiles[countTiles] = new Tile(countTiles, subImage);
                    countTiles++;
                }
            }
        }
        Bitmap emptyTemp = BitmapFactory.decodeResource(this.getBaseContext().getResources(), R.drawable.empty);
        Bitmap empty = Bitmap.createBitmap(emptyTemp,0,0,TILE_WIDTH,TILE_HEIGHT);
        allTiles[numCells-1] = new Tile(numCells-1, empty);

        ALL_TILES = allTiles;
    }

    /*
    Whenever the game is paused, the state is saved so even if the app is killed by android because of
    a shortage of memory, the game will be available for the user when he returns.
     */
    @Override
    public void onPause(){
        if(!BACK_PRESSED) {
            System.out.println("Saving");
            SharedPreferences pref = getSharedPreferences("AppData", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("difficulty", DIFFICULTY);
            editor.putInt("moves", NUMBER_MOVES);
            long endTime = System.nanoTime();
            int totalTime = (int) ((endTime - START_TIME) / 1000000000) + PASSED_TIME;
            editor.putInt("time", totalTime);
            String str = "";
            int[] currentState = PUZZLE.get_current_state();
            int i;
            for (i = 0; i < currentState.length - 1; i++) {
                str = str + currentState[i] + ",";
            }
            str = str + currentState[i];
            editor.putString("currentState", str);
            editor.putInt("imageId", IMAGE_ID);
            editor.commit();
            super.onPause();
        }else{
            super.onPause();
        }
    }

    /*
    Initialized a new game if previous game was ended.
     */
    private void initialize_game(Context context){
        BACK_PRESSED = false;
        Intent intent = getIntent();
        IMAGE_ID = intent.getIntExtra("IMAGE_ID", 0);
        DIFFICULTY = intent.getIntExtra("DIFFICULTY", 0);
        PUZZLE = new Puzzle(DIFFICULTY);

        set_image_tiles(context);

        PUZZLE.set_current_state(PUZZLE.get_solution());

        set_adapter();

        show_toast("Take a look at the puzzle");

        NUMBER_MOVES = 0;
        /*
        Now the program waits for 3 seconds and then scrambles the image.
         */
        Runnable r = new Runnable() {
            @Override
            public void run(){
                int numCells = ALL_TILES.length;
                PUZZLE.set_current_state(numCells);
                scramble_image(PUZZLE);
                ADAPTER.change();
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 3000);

        START_TIME = System.nanoTime();
        PASSED_TIME = 0;
    }

    /*
    Resizes the image and creates the tiles for the game.
     */
    private void set_image_tiles(Context context){
        // preperations to scale the image
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Bitmap imgImmutable = BitmapFactory.decodeResource(context.getResources(), IMAGE_ID);
        Bitmap img = imgImmutable.copy(Bitmap.Config.ARGB_8888, true);
        //scaling the image
        set_image_size(width, height, img);

        set_tiles();
    }

    /*
    Loads a game when all data is present.
     */
    private void load_game(Context context){
        BACK_PRESSED = false;
        SharedPreferences pref = getSharedPreferences("AppData", MODE_PRIVATE);
        DIFFICULTY = pref.getInt("difficulty", 0);
        NUMBER_MOVES = pref.getInt("moves", 0);
        PASSED_TIME = pref.getInt("time", 0);
        IMAGE_ID = pref.getInt("imageId", 0);
        PUZZLE = new Puzzle(DIFFICULTY);
        set_image_tiles(context);
        String currentState = pref.getString("currentState", "");
        String[] state = currentState.split(",");
        int[] newState = new int[state.length];
        for(int i = 0; i < state.length;i++){
            newState[i] = Integer.parseInt(state[i]);
        }

        set_adapter();

        PUZZLE.set_current_state(newState);
        scramble_image(PUZZLE);
        ADAPTER.change();
    }

    /*
    Sets the adapter for the gridview.
     */
    private void set_adapter(){
        Adapter gameAdapter = new GameAdapter(ALL_TILES.length,ALL_TILES,PUZZLE);
        GAME_VIEW.setAdapter((GameAdapter)gameAdapter);
        ADAPTER = (GameAdapter)gameAdapter;
        GAME_VIEW.setOnItemClickListener(respondToClick);
    }

    /*
    This is the clicklistener which handles taps on the screen.
    It basically records the position of the tap, and then checks if one of the surrounding positions
    holds the empty space. If this is true, then the tile will be moved to the empty space. If this
    is not true, a toast will be shown to inform the user that his/her move was not valid.
    The user will also be informed when tapping the empty space.
     */

    public AdapterView.OnItemClickListener respondToClick;

    {
        respondToClick = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {

                int[] currentState = PUZZLE.get_current_state();

                int nextTile = i+1;
                int prevTile = i-1;
                int lowerTile = i + (DIFFICULTY +3);
                int upperTile = i - (DIFFICULTY +3);


                if(currentState[i] == ALL_TILES.length-1){
                    show_toast("No tile here!");
                } else if(i==0){
                    if(currentState[nextTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i, nextTile, currentState, view);
                    } else if(currentState[lowerTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i, lowerTile, currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i == DIFFICULTY + 2){
                    if(currentState[prevTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i, prevTile, currentState, view);
                    } else if(currentState[lowerTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i, lowerTile, currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i == (ALL_TILES.length-1)){
                    if(currentState[prevTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,prevTile,currentState, view);
                    } else if(currentState[upperTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,upperTile,currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i == ALL_TILES.length - (DIFFICULTY + 3)){
                    if(currentState[nextTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,nextTile,currentState, view);
                    } else if(currentState[upperTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,upperTile,currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i > 0 && i < (DIFFICULTY + 2)){
                    if(currentState[nextTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,nextTile,currentState, view);
                    } else if(currentState[lowerTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,lowerTile,currentState, view);
                    } else if( currentState[prevTile] == (ALL_TILES.length - 1) ) {
                        swap_tiles(i,prevTile,currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i < ALL_TILES.length-1 && i > (ALL_TILES.length - (DIFFICULTY + 3))){
                    if(currentState[nextTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,nextTile,currentState, view);
                    } else if(currentState[upperTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,upperTile,currentState, view);
                    } else if( currentState[prevTile] == (ALL_TILES.length - 1) ) {
                        swap_tiles(i,prevTile,currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i > 0 && i < ALL_TILES.length-1 - (DIFFICULTY + 3) && (i % (DIFFICULTY + 3)) == 0){
                    if(currentState[nextTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,nextTile,currentState, view);
                    } else if(currentState[upperTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,upperTile,currentState, view);
                    } else if(currentState[lowerTile] == (ALL_TILES.length - 1)) {
                        swap_tiles(i,lowerTile,currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i > (DIFFICULTY+2) && ((i - (DIFFICULTY + 2))%(DIFFICULTY + 3) == 0)) {
                    if (currentState[prevTile] == (ALL_TILES.length - 1)) {
                        swap_tiles(i, prevTile, currentState, view);
                    } else if (currentState[upperTile] == (ALL_TILES.length - 1)) {
                        swap_tiles(i, upperTile, currentState, view);
                    } else if (currentState[lowerTile] == (ALL_TILES.length - 1)) {
                        swap_tiles(i, lowerTile, currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else {
                    if(currentState[prevTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,prevTile,currentState, view);
                    } else if(currentState[upperTile] == (ALL_TILES.length - 1)){
                        swap_tiles(i,upperTile,currentState, view);
                    } else if(currentState[lowerTile] == (ALL_TILES.length - 1)) {
                        swap_tiles(i, lowerTile, currentState, view);
                    } else if(currentState[nextTile] == (ALL_TILES.length-1)){
                        swap_tiles(i,nextTile,currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.settings_easy){
            if(DIFFICULTY != 0){
                delete_saved_game();
                save_difficulty(0);
                Intent intent = new Intent(GAME_VIEW.getContext(),Game.class);
                intent.putExtra("DIFFICULTY", 0);
                intent.putExtra("IMAGE_ID", IMAGE_ID);
                startActivity(intent);
                finish();
            }else{
                show_toast("Difficulty is already Easy.");
            }
        } else if(id == R.id.settings_medium){
            if(DIFFICULTY != 1){
                delete_saved_game();
                save_difficulty(1);
                Intent intent = new Intent(GAME_VIEW.getContext(),Game.class);
                intent.putExtra("DIFFICULTY", 1);
                intent.putExtra("IMAGE_ID", IMAGE_ID);
                startActivity(intent);
                finish();
            }else{
                show_toast("Difficulty is already Medium.");
            }
        } else if(id == R.id.settings_hard){
            if(DIFFICULTY != 2){
                delete_saved_game();
                save_difficulty(2);
                Intent intent = new Intent(GAME_VIEW.getContext(),Game.class);
                intent.putExtra("DIFFICULTY", 2);
                intent.putExtra("IMAGE_ID", IMAGE_ID);
                startActivity(intent);
                finish();
            }else{
                show_toast("Difficulty is already Hard.");
            }


        } else if(id == R.id.settings_restart){
            delete_saved_game();
            Intent restartIntent = new Intent(GAME_VIEW.getContext(), Game.class);
            restartIntent.putExtra("IMAGE_ID", IMAGE_ID);
            restartIntent.putExtra("DIFFICULTY", DIFFICULTY);
            startActivity(restartIntent);
            finish();
        } else if(id == R.id.settings_image){
            BACK_PRESSED = true;
            delete_saved_game();
            Intent homeIntent = new Intent(GAME_VIEW.getContext(), Home.class);
            startActivity(homeIntent);
            finish();
        }

        return id == R.id.settings_restart || super.onOptionsItemSelected(item);
    }

    /*
    This function takes the measurements of the screen and an image, and resizes the image to
    fit the screen.
    Depending on the ratio between de width of the image and its height, the image will either be fitted
    to the width of the screen or the height of the screen.
     */
    private void set_image_size(int screenWidth, int screenHeight, Bitmap img){
        int imageWidth = img.getWidth();
        int imageHeight = img.getHeight();
        int newImageWidth = imageWidth;
        int newImageHeight = imageHeight;
        if(imageWidth < imageHeight){
            if( imageHeight > screenHeight - 100 ){
                newImageHeight = screenHeight - 100;
                newImageWidth = (int)((newImageHeight*1.00) / imageHeight) * imageWidth;
            }
        }else if(imageHeight <= imageWidth){
            if(imageWidth > screenWidth - 100){
                newImageWidth = screenWidth - 100;
                newImageHeight = (int)(((newImageWidth * 1.00) / imageWidth) * imageHeight);
            }
        }
        IMAGE = Bitmap.createScaledBitmap(img,newImageWidth,newImageHeight,true);

        int gameDifficulty = DIFFICULTY;
        int numColumns = gameDifficulty + 3;

        int tileHeight = IMAGE.getHeight() / numColumns;
        int tileWidth = IMAGE.getWidth() / numColumns;

        TILE_HEIGHT = tileHeight;
        TILE_WIDTH = tileWidth;
    }

    /*
    Saves the difficulty when it's changed.
    This is a setting which will be kept even when the app is closed.
     */
    private void save_difficulty(int difficulty){
        SharedPreferences pref = getSharedPreferences("AppData", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("difficulty");
        editor.putInt("difficulty", difficulty);
        editor.commit();
    }

    /*
    Modifies what happens when the user goes back to the home screen.
     */
    public void onBackPressed(){
        BACK_PRESSED = true;
        delete_saved_game();
        Intent homeIntent = new Intent(GAME_VIEW.getContext(),Home.class);
        startActivity(homeIntent);
        finish();
    }

    /*
    This function scrambles the image after it has been shown to the user.
     */
    private void scramble_image(Puzzle puzzle){
        int[] begin = puzzle.get_current_state();
        Tile[] allTiles = ALL_TILES;
        Tile[] newTileOrder = new Tile[ALL_TILES.length];
        for(int i = 0; i < ALL_TILES.length; i++){
            newTileOrder[i] = allTiles[begin[i]];
        }
        ALL_TILES = newTileOrder;
    }

    /*
    This functions swaps two tiles.
    It begins with collecting all necessary data like the current state, the solution and the tiles.
    It then swaps two tiles by changing the order of the lists which keep them.
    It also checks if the current state after the swap is identical to the solution, in which case a
    new intent will be created and the user will be shown the Score activity.
     */

    private void swap_tiles(int tile1, int tile2, int[] currentState, View view){
        int tempTile = currentState[tile2];
        currentState[tile2] = currentState[tile1];
        currentState[tile1] = tempTile;
        PUZZLE.set_current_state(currentState);

        Tile[] allTiles = ALL_TILES;
        Tile temp = allTiles[tile2];
        allTiles[tile2] = allTiles[tile1];
        allTiles[tile1] = temp;

        ALL_TILES = allTiles;

        ADAPTER.change();

        NUMBER_MOVES += 1;


        if(PUZZLE.check_solution() & NUMBER_MOVES > 2){
            delete_saved_game();
            long endTime = System.nanoTime();
            long totalTime = ((endTime - START_TIME)/1000000000) + PASSED_TIME;

            Intent intentOut = new Intent(view.getContext(), Score.class);
            intentOut.putExtra("TIME", totalTime);
            intentOut.putExtra("MOVES", NUMBER_MOVES);
            intentOut.putExtra("IMAGE", IMAGE_ID);
            startActivity(intentOut);
            finish();
        }
    }

    /*
    This function takes a string and prints a short toast.
     */
    private void show_toast(String str){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }

    private void delete_saved_game(){
        SharedPreferences pref = getSharedPreferences("AppData", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        editor.putInt("difficulty", DIFFICULTY);
        editor.commit();
    }
}
