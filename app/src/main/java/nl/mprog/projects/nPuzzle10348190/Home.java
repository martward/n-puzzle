package nl.mprog.projects.nPuzzle10348190;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import java.io.*;
import java.util.AbstractList;


public class Home extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        GridView imageView = (GridView)findViewById(R.id.imageView);
        // imageView.setNumColumns(3);
        imageView.setOnItemClickListener(respondToClick);
        imageView.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return 7;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ImageView image_view = new ImageView(viewGroup.getContext());
                //image_view.setImageResource(R.drawable.puzzle_0);
                String name = "puzzle_" + i;
                int drawableResourceId = viewGroup.getContext().getResources().getIdentifier(name, "drawable", "nl.mprog.projects.nPuzzle10348190");
                image_view.setImageResource(drawableResourceId);
                AbsListView.LayoutParams params = new AbsListView.LayoutParams(viewGroup.getWidth(),viewGroup.getWidth() + 25);
                image_view.setLayoutParams(params);
                return image_view;
            }

        });

}
    public AdapterView.OnItemClickListener respondToClick = new AdapterView.OnItemClickListener(){


        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String name = "puzzle_" + i;
            int drawableResourceId = adapterView.getContext().getResources().getIdentifier(name, "drawable", "nl.mprog.projects.nPuzzle10348190");
            System.out.println(drawableResourceId);
            Intent intent = new Intent(view.getContext(), Game.class);
            intent.putExtra("IMAGE_ID", drawableResourceId);
            startActivity(intent);
        }
    };

}
