package com.dip.flickr;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class CreateUrlAndGetJsonData extends AsyncTask<String, Void, List<Photo>> implements DownloadSourceCode.DownloadListener {
    private static final String TAG = "Result";
    private List<Photo> photoList;
    private String baseURL, language;
    private boolean matchAll, runningOnSameThread = false;
    private final DataListener dataCallBack;

    public CreateUrlAndGetJsonData(String baseURL, String language, boolean matchAll, DataListener dataCallBack) {
        this.baseURL = baseURL;
        this.language = language;
        this.matchAll = matchAll;
        this.dataCallBack = dataCallBack;
        photoList = null;
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        String destinationUri = createURI(params[0], language, matchAll);
        DownloadSourceCode code = new DownloadSourceCode(this);
        code.executeOnThisThread(destinationUri);
        return photoList;
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
            if (dataCallBack != null){
                dataCallBack.dataProcessingListener(photoList, DownloadStatus.COMPLETED);
            }
    }

    interface DataListener{
        void dataProcessingListener(List<Photo> data, DownloadStatus status);
    }

    void executeOnSameThread(String searchCriteria){
        runningOnSameThread = true;
        String destinationURI = createURI(searchCriteria, language, matchAll);
        DownloadSourceCode code = new DownloadSourceCode(this);
        code.execute(destinationURI);
    }

    private String createURI(String searchCriteria, String language, boolean matchAll) {
        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", language)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    public void downloadListener(String data, DownloadStatus status) {
        if (status == DownloadStatus.COMPLETED){
            photoList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray itemsArray = jsonObject.getJSONArray("items");

                for (int i =0 ; i < itemsArray.length() ; i++){
                    JSONObject photoDetails = itemsArray.getJSONObject(i);

                    String title = photoDetails.getString("title");
                    String author = photoDetails.getString("author");
                    String authorId = photoDetails.getString("author_id");
                    String tags = photoDetails.getString("tags");
                    String photoUrl = photoDetails.getJSONObject("media").getString("m");

                    String link = photoUrl.replaceFirst("_m.", "_b.");

                    photoList.add(new Photo(title, author, authorId, link, tags, photoUrl));
                    Log.d(TAG, "downloadListener: data : " + photoList.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "downloadListener: error " + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            } catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "downloadListener: error " + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if (runningOnSameThread){
            if (dataCallBack != null){
                dataCallBack.dataProcessingListener(photoList, status);
            }
        }
    }
}
