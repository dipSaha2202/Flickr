package com.dip.flickr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements
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

        activateToolbar(false);

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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String tags = preferences.getString(FLICKR_QUERY, "");
        if (tags.length() > 0){
            CreateUrlAndGetJsonData getJsonData = new CreateUrlAndGetJsonData(
                    "https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true,
                    MainActivity.this);
            getJsonData.execute(tags);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.search_menu){
            Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(searchIntent);
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
        Intent intent = new Intent(MainActivity.this, PhotoDetailsActivity.class);
        intent.putExtra(PHOTO_TRANSFER, recycleViewAdapter.getPhotoOnTap(position));
        startActivity(intent);
    }
}
