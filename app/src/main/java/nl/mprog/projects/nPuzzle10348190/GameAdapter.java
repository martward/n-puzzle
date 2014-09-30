package nl.mprog.projects.nPuzzle10348190;

/*
Martijn Wardenaar
10348190
martijnwardenaar@gmail.com
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GameAdapter extends BaseAdapter {


    private static int NUM_CELLS;
    private static Tile[] ALL_TILES;
    private static Puzzle PUZZLE;

    GameAdapter(int num, Tile[] allTiles, Puzzle puzzle){
        NUM_CELLS = num;
        ALL_TILES = allTiles;
        PUZZLE = puzzle;
    }

    @Override
    public int getCount() {
        return NUM_CELLS;
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
        int[] currentState = PUZZLE.get_current_state();

        ImageView image_view = new ImageView(viewGroup.getContext());
        image_view.setImageBitmap(ALL_TILES[currentState[i]].get_image());
        image_view.setPadding(5, 5, 5, 5);
        return image_view;
    }

}
