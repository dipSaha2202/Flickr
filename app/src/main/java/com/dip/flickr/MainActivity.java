package com.dip.flickr;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
                                        CreateUrlAndGetJsonData.DataListener,
                                        RecycleViewItemClickListener.OnRecycleViewItemClickListener{

    private static final String TAG = "Result";
    public static final String URL =
            "https://api.flickr.com/services/feeds/photos_public.gne?tags=android&tagmodeany&format=json&nojsoncallback=1";
    private RecycleViewAdapter recycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycle_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecycleViewItemClickListener(
                MainActivity.this, recyclerView, this));

        recycleViewAdapter = new RecycleViewAdapter(this, new ArrayList<Photo>());
        recyclerView.setAdapter(recycleViewAdapter);

        //DownloadSourceCode code = new DownloadSourceCode();
        //code.execute(URL);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CreateUrlAndGetJsonData getJsonData = new CreateUrlAndGetJsonData(
                "https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true,
                MainActivity.this);
         getJsonData.execute("android, nougat");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /*@Override
    public void downloadListener(String data, DownloadStatus status) {
            if (status == DownloadStatus.COMPLETED){
                Log.d(TAG, "onDownloadComplete: completed : code - " + data);
            } else {
                Log.d(TAG, "onDownloadComplete: current status : " + status);
            }
    }*/

    @Override
    public void dataProcessingListener(List<Photo> data, DownloadStatus status) {
        if (status == DownloadStatus.COMPLETED){
            recycleViewAdapter.loadNewData(data);
           // recycleViewAdapter.notifyDataSetChanged();
        } else {
            Log.d(TAG, "onDownloadComplete: current status : " + status);
        }
    }

    @Override
    public void onRecycleItemClick(View view, int position) {

    }

    @Override
    public void onRecycleItemLongClick(View view, int position) {

    }
}
