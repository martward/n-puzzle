package nl.mprog.projects.nPuzzle10348190;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by Martijn on 9/20/2014.
 */
public class GameAdapter extends BaseAdapter {


    private static int NUM_CELLS;
    private static Tile[] ALL_TILES;
    private static Puzzle PUZZLE;

    GameAdapter(int num, Tile[] allTiles, Puzzle puzzle){
        set_num_cells(num);
        set_all_tiles(allTiles);
        set_puzzle(puzzle);
    }

    @Override
    public int getCount() {
        return get_num_cells();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void change(){
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Puzzle puzzle = get_puzzle();
        int[] currentState = puzzle.get_current_state();

        ImageView image_view = new ImageView(viewGroup.getContext());
        Tile[] allTiles;
        allTiles = get_all_tiles();
        image_view.setImageBitmap(allTiles[currentState[i]].get_image());
        image_view.setPadding(5, 5, 5, 5);
        return image_view;
    }

    public static void set_all_tiles(Tile[] tiles){
        ALL_TILES = tiles;
    }

    public static Tile[] get_all_tiles(){
        return ALL_TILES;
    }

    public static void set_num_cells(int num){
        NUM_CELLS = num;
    }

    public static int get_num_cells(){
        return NUM_CELLS;
    }

    public static void set_puzzle(Puzzle puzzle){
        PUZZLE = puzzle;
    }

    public static Puzzle get_puzzle(){
        return PUZZLE;
    }

}
