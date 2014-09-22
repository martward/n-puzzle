package nl.mprog.projects.nPuzzle10348190;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


public class ShowImage extends ActionBarActivity {

    private static int IMAGE_ID;
    private static int DIFFICULTY;
    private static int TILE_HEIGHT;
    private static int TILE_WIDTH;
    private static Tile[] ALL_TILES;
    private static Bitmap IMAGE;
    private static GameAdapter ADAPTER;
    private static Puzzle PUZZLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        GridView showImage = (GridView) findViewById(R.id.showImage);
        Intent intent = getIntent();
        int imageId = intent.getIntExtra("IMAGE_ID", 0);
        int difficulty = intent.getIntExtra("DIFFICULTY", 0);
        set_values(imageId, difficulty);

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
        showImage.setNumColumns(numColumns);
        final int numCells = numColumns * numColumns;
        final Tile[] allTiles = new Tile[numCells];
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
        showImage.setAdapter((GameAdapter)gameAdapter);
        set_adapter((GameAdapter) gameAdapter);
        showImage.setOnItemClickListener(respondToClick);

    }

    public AdapterView.OnItemClickListener respondToClick;

    {
        respondToClick = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                Intent intentOut = new Intent(view.getContext(), Game.class);
                intentOut.putExtra("IMAGE_ID", get_image_id());
                intentOut.putExtra("Difficulty", get_difficulty());
                startActivity(intentOut);
                finish();
            }
        };
    }

    public static void set_values(int id, int diff){
        IMAGE_ID = id;
        DIFFICULTY = diff;
    }

    public static int get_image_id(){
        return IMAGE_ID;
    }

    public static int get_difficulty(){
        return DIFFICULTY;
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

    public static void set_image(Bitmap img){
        IMAGE = img;
    }

    public static Bitmap get_image(){
        return IMAGE;
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

}
