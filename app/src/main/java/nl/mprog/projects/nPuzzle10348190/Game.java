package nl.mprog.projects.nPuzzle10348190;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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
    private static final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        GridView gameField = (GridView)findViewById(R.id.gameView);
        Intent intent = getIntent();
        int imageId = intent.getIntExtra("IMAGE_ID", 0);
        int difficulty = intent.getIntExtra("DIFFICULTY", 0);
        set_difficulty(difficulty);
        set_image_id(imageId);
        Puzzle puzzle;
        puzzle = new Puzzle(difficulty);
        set_puzzle(puzzle);

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;


        Bitmap imgImmutable = BitmapFactory.decodeResource(this.getBaseContext().getResources(), imageId);
        Bitmap img = imgImmutable.copy(Bitmap.Config.ARGB_8888, true);

        set_image_size(width, height, img);
        int numColumns = get_difficulty() + 3;
        int numCells = numColumns * numColumns;
        gameField.setNumColumns(numColumns);
        Tile[] allTiles = new Tile[numCells];
        int x;
        int y;

        Bitmap imgResized = get_image();
        int countTiles = 0;
        for(int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numColumns; j++) {
                if(countTiles < numCells -1) {
                    x = j * get_tile_width();
                    y = i * get_tile_height();
                    Bitmap subImage = Bitmap.createBitmap(imgResized, x, y, get_tile_width(), get_tile_height());
                    allTiles[countTiles] = new Tile(countTiles, subImage);
                    countTiles++;
                }
            }
        }

        Bitmap emptyTemp = BitmapFactory.decodeResource(this.getBaseContext().getResources(), R.drawable.empty);
        Bitmap empty = Bitmap.createBitmap(emptyTemp,0,0,get_tile_width(),get_tile_height());

        allTiles[numCells-1] = new Tile(8, empty);

        set_all_tiles(allTiles);
        puzzle.set_current_state(puzzle.get_solution());

        Adapter gameAdapter = new GameAdapter(numCells,allTiles,puzzle);
        gameField.setAdapter((GameAdapter)gameAdapter);
        set_adapter((GameAdapter) gameAdapter);
        gameField.setOnItemClickListener(respondToClick);

        ((GameAdapter) gameAdapter).change();

        show_toast("Take a look at the puzzle");

        reset_num_moves();

        puzzle.set_current_state(numCells);
        scramble_image(puzzle);


        ((GameAdapter) gameAdapter).change();

        long timeStarted = System.nanoTime();
        set_start_time(timeStarted);
    }

    public AdapterView.OnItemClickListener respondToClick;

    {
        respondToClick = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                Puzzle puzzle = get_puzzle();
                Tile[] gameTiles = get_all_tiles();

                int[] currentState = puzzle.get_current_state();

                //System.out.println(i);

                int nextTile = i+1;
                int prevTile = i-1;
                int lowerTile = i + (get_difficulty() +3);
                int upperTile = i - (get_difficulty() +3);


                if(currentState[i] == gameTiles.length-1){
                    show_toast("No tile here!");
                } else if(i==0){
                    System.out.println(1);
                    if(currentState[nextTile] == (gameTiles.length - 1)){
                        swap_tiles(i, nextTile, currentState, view);
                    } else if(currentState[lowerTile] == (gameTiles.length - 1)){
                        swap_tiles(i, lowerTile, currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i == get_difficulty() + 2){
                    System.out.println(2);
                    if(currentState[prevTile] == (gameTiles.length - 1)){
                        swap_tiles(i, prevTile, currentState, view);
                    } else if(currentState[lowerTile] == (gameTiles.length - 1)){
                        swap_tiles(i, lowerTile, currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i == (gameTiles.length-1)){
                    System.out.println(3);
                    if(currentState[prevTile] == (gameTiles.length - 1)){
                        swap_tiles(i,prevTile,currentState, view);
                    } else if(currentState[upperTile] == (gameTiles.length - 1)){
                        swap_tiles(i,upperTile,currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i == gameTiles.length - (get_difficulty() + 3)){
                    System.out.println(4);
                    if(currentState[nextTile] == (gameTiles.length - 1)){
                        swap_tiles(i,nextTile,currentState, view);
                    } else if(currentState[upperTile] == (gameTiles.length - 1)){
                        swap_tiles(i,upperTile,currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i > 0 && i < (get_difficulty() + 2)){
                    System.out.println(5);
                    if(currentState[nextTile] == (gameTiles.length - 1)){
                        swap_tiles(i,nextTile,currentState, view);
                    } else if(currentState[lowerTile] == (gameTiles.length - 1)){
                        swap_tiles(i,lowerTile,currentState, view);
                    } else if( currentState[prevTile] == (gameTiles.length - 1) ) {
                        swap_tiles(i,prevTile,currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i < gameTiles.length-1 && i > (gameTiles.length - (get_difficulty() + 3))){
                    System.out.println(6);
                    if(currentState[nextTile] == (gameTiles.length - 1)){
                        swap_tiles(i,nextTile,currentState, view);
                    } else if(currentState[upperTile] == (gameTiles.length - 1)){
                        swap_tiles(i,upperTile,currentState, view);
                    } else if( currentState[prevTile] == (gameTiles.length - 1) ) {
                        swap_tiles(i,prevTile,currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i > 0 && i < gameTiles.length-1 - (get_difficulty() + 3) && (i % (get_difficulty() + 3)) == 0){
                    System.out.println(7);
                    if(currentState[nextTile] == (gameTiles.length - 1)){
                        swap_tiles(i,nextTile,currentState, view);
                    } else if(currentState[upperTile] == (gameTiles.length - 1)){
                        swap_tiles(i,upperTile,currentState, view);
                    } else if(currentState[lowerTile] == (gameTiles.length - 1)) {
                        swap_tiles(i,lowerTile,currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else if(i > (get_difficulty()+2) && ((i - (get_difficulty() + 2))%(get_difficulty() + 3) == 0)) {
                    System.out.println(8);
                    if (currentState[prevTile] == (gameTiles.length - 1)) {
                        swap_tiles(i, prevTile, currentState, view);
                    } else if (currentState[upperTile] == (gameTiles.length - 1)) {
                        swap_tiles(i, upperTile, currentState, view);
                    } else if (currentState[lowerTile] == (gameTiles.length - 1)) {
                        swap_tiles(i, lowerTile, currentState, view);
                    } else {
                        show_toast("Invalid move!");
                    }
                } else {
                    System.out.println(9);
                    if(currentState[prevTile] == (gameTiles.length - 1)){
                        swap_tiles(i,prevTile,currentState, view);
                    } else if(currentState[upperTile] == (gameTiles.length - 1)){
                        swap_tiles(i,upperTile,currentState, view);
                    } else if(currentState[lowerTile] == (gameTiles.length - 1)) {
                        swap_tiles(i, lowerTile, currentState, view);
                    } else if(currentState[nextTile] == (gameTiles.length-1)){
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
        return id == R.id.settings_restart || super.onOptionsItemSelected(item);
    }

    public static void set_difficulty(int d){
        DIFFICULTY = d;
    }

    public static int get_difficulty(){
        return DIFFICULTY ;
    }

    public static void set_image_size(int screenWidth, int screenHeight, Bitmap img){
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
        Bitmap imgResized = Bitmap.createScaledBitmap(img,newImageWidth,newImageHeight,true);
        set_image(imgResized);
        img = get_image();

        int gameDifficulty = get_difficulty();
        int numColumns = gameDifficulty + 3;

        int tileHeight = img.getHeight() / numColumns;
        int tileWidth = img.getWidth() / numColumns;

        set_tile_size(tileHeight, tileWidth);

    }

    public static void set_tile_size(int h, int w){
        TILE_HEIGHT = h;
        TILE_WIDTH = w;
    }

    public static int get_tile_width(){
        return TILE_WIDTH;
    }

    public static int get_tile_height(){
        return TILE_HEIGHT;
    }

    public static void set_all_tiles(Tile[] tiles){
        ALL_TILES = tiles;
    }

    public static Tile[] get_all_tiles(){
        return ALL_TILES;
    }

    public static void set_image(Bitmap img){
        IMAGE = img;
    }

    public static Bitmap get_image(){
        return IMAGE;
    }

    public void scramble_image(Puzzle puzzle){
        int[] begin = puzzle.get_current_state();
        Tile[] allTiles = get_all_tiles();
        Tile[] newTileOrder = new Tile[get_all_tiles().length];
        for(int i = 0; i < get_all_tiles().length; i++){
            newTileOrder[i] = allTiles[begin[i]];
        }
        set_all_tiles(newTileOrder);
    }

    public static GameAdapter get_adapter(){
        return ADAPTER;
    }

    public static void set_adapter(GameAdapter a){
        ADAPTER = a;
    }

    public static void set_puzzle(Puzzle puzzle){
        PUZZLE = puzzle;
    }

    public static Puzzle get_puzzle(){
        return PUZZLE;
    }

    public void swap_tiles(int tile1, int tile2, int[] currentState, View view){
        Puzzle puzzle = get_puzzle();
        int tempTile = currentState[tile2];
        currentState[tile2] = currentState[tile1];
        currentState[tile1] = tempTile;
        puzzle.set_current_state(currentState);

        Tile[] allTiles = get_all_tiles();
        Tile temp = allTiles[tile2];
        allTiles[tile2] = allTiles[tile1];
        allTiles[tile1] = temp;
        set_all_tiles(allTiles);

        GameAdapter gameAdapter = get_adapter();
        gameAdapter.change();

        set_num_moves();


        if(puzzle.check_solution()){
            long endTime = System.nanoTime();
            long totalTime = (endTime - get_start_time())/1000000000;
            int numMoves = get_num_moves();

            System.out.println(totalTime);

            Intent intentOut = new Intent(view.getContext(), Score.class);
            intentOut.putExtra("TIME", totalTime);
            intentOut.putExtra("MOVES", numMoves);
            intentOut.putExtra("DIFFICULTY", get_difficulty());
            intentOut.putExtra("IMAGE", get_image_id());
            startActivity(intentOut);
            finish();
        }
    }

    public void show_toast(String str){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, str, duration);
        toast.show();
    }

    private static void set_start_time(long time){
        START_TIME = time;
    }

    private static long get_start_time(){
        return START_TIME;
    }

    private static void reset_num_moves(){
        NUMBER_MOVES = 0;
    }

    private static void set_num_moves(){
        NUMBER_MOVES += 1;
    }

    private static int get_num_moves(){
        return NUMBER_MOVES;
    }

    private static int get_image_id(){
        return IMAGE_ID;
    }

    private static void set_image_id(int id){
        IMAGE_ID = id;
    }

}
