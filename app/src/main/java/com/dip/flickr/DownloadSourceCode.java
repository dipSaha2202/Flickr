package com.dip.flickr;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

enum DownloadStatus{IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, COMPLETED}

class DownloadSourceCode extends AsyncTask<String, Void, String> {
    private DownloadStatus downloadStatus;
    private final DownloadListener callback;

    interface DownloadListener{
        void downloadListener(String data, DownloadStatus status);
    }

    public DownloadSourceCode(DownloadListener mActivity) {
        this.downloadStatus = DownloadStatus.IDLE;
        this.callback = mActivity;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        StringBuilder result = new StringBuilder();

         if (strings == null){
             downloadStatus = DownloadStatus.NOT_INITIALISED;
             return null;
         }

        try {
            downloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

           for (String line = bufferedReader.readLine() ; line != null ; line = bufferedReader.readLine()){
               result.append(line).append("\n");
           }

           downloadStatus = DownloadStatus.COMPLETED;
           return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e){
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    void executeOnThisThread(String uri){
        if (callback != null){
            callback.downloadListener(doInBackground(uri), downloadStatus);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (callback != null){
            callback.downloadListener(s, downloadStatus);
        }
    }
}
