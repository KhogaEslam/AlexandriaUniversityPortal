/*
 * $Id: $
 *
 * Copyright (C) 2012 Stoyan Rachev (stoyanr@gmail.com)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 */

package eg.edu.alexu.alexandriauniversity.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import eg.edu.alexu.alexandriauniversity.R;
import eg.edu.alexu.alexandriauniversity.content.ContentManager;
import eg.edu.alexu.alexandriauniversity.sync.Synchronizer;
import eg.edu.alexu.alexandriauniversity.util.DialogUtils;

public class AddChannelActivity extends Activity {

    private static final String TAG = "AddChannelActivity";

    private Handler handler;
    private EditText urlEditText;

    private ProgressDialog progressDialog;

    private String eventsEn = "http://au.alexu.edu.eg/_layouts/feed.aspx?xsl=1&web=/English&page=0f998fdc-948b-4c87-b317-dd4f31f6c5a0&wp=3665dfc9-3e79-49f8-9742-c090bcf39bac&pageurl=/English/Pages/EventsRSSFeeds.aspx";
    private String eventsAr = "http://au.alexu.edu.eg/_layouts/feed.aspx?xsl=1&web=/Arabic&page=09987285-60b6-445d-8025-cfe135fe97bf&wp=f30e97b3-e5a7-4e79-824b-1f188b97efac&pageurl=/Arabic/Pages/EventsRSSFeeds.aspx";
    private String newsEn = "http://au.alexu.edu.eg/_layouts/feed.aspx?xsl=1&web=/English&page=4c15cd20-e84c-4b7e-b942-e31352fc095e&wp=d5725327-35d2-46b0-b52d-aec37aa6ae7d&pageurl=/English/Pages/NewsRSSFeeds.aspx";
    private String newsAr = "http://au.alexu.edu.eg/_layouts/feed.aspx?xsl=1&web=/Arabic&page=f6352b68-40b7-4d9a-b089-ffe9b05c36ae&wp=64b5929f-f754-460b-9dc2-6249b2f0ce33&pageurl=/Arabic/Pages/NewsRSSFeeds.aspx";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.add_channel_activity);
        handler = new Handler();
        initControls();
    }
    
    @Override
    public void onStop() {
        super.onStop();
        dismissProgressDialog();
    }

    public void initControls() {

        Button addButton;
        //private TextView url1EditText;
        Button addButton1;
        //private TextView url2EditText;
        Button addButton2;
        //private TextView url3EditText;
        Button addButton3;
        //private TextView url4EditText;
        Button addButton4;

        //url1EditText = (TextView) findViewById(R.id.url1EditText);
        addButton1 = (Button) findViewById(R.id.addButton1);
        addButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChannel1(eventsEn);
            }
        });

        //url2EditText = (TextView) findViewById(R.id.url2EditText);
        addButton2 = (Button) findViewById(R.id.addButton2);
        addButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChannel1(eventsAr);
            }
        });

        //url3EditText = (TextView) findViewById(R.id.url3EditText);
        addButton3 = (Button) findViewById(R.id.addButton3);
        addButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChannel1(newsEn);
            }
        });

        //url4EditText = (TextView) findViewById(R.id.url4EditText);
        addButton4 = (Button) findViewById(R.id.addButton4);
        addButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChannel1(newsAr);
            }
        });

        urlEditText = (EditText) findViewById(R.id.urlEditText);
        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChannel();
            }
        });
    }

    private void addChannel1(String url) {

        //Check internet connection for now all times but next step will be first time only
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //Check internet connection for now all times but next step will be first time only

        if (wifi.isAvailable() && wifi.isConnected()) {
            Toast.makeText(this, "Loading News over WIFI", Toast.LENGTH_LONG).show();
            //String url = urlEditText.getText().toString();
            Log.d(TAG, "Adding new channel with URL " + url);
            showProgressDialog();
            new Thread(new AddChannelRunnable(url)).start();

        } else if (mobile.isAvailable() && mobile.isConnected()) {
            Toast.makeText(this, "Loading News Over Mobile 3G ", Toast.LENGTH_LONG).show();
            //String url = urlEditText.getText().toString();
            Log.d(TAG, "Adding new channel with URL " + url);
            showProgressDialog();
            new Thread(new AddChannelRunnable(url)).start();

        } else {
            Toast.makeText(this, "No Network WIFI/3G", Toast.LENGTH_LONG).show();
        }
    }

    private void addChannel() {
        //Check internet connection for now all times but next step will be first time only
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //Check internet connection for now all times but next step will be first time only

        if (wifi.isAvailable() && wifi.isConnected()) {
            Toast.makeText(this, "Loading News over WIFI", Toast.LENGTH_LONG).show();
            String url = urlEditText.getText().toString();
            Log.d(TAG, "Adding new channel with URL " + url);
            showProgressDialog();
            new Thread(new AddChannelRunnable(url)).start();

        } else if (mobile.isAvailable() && mobile.isConnected()) {
            Toast.makeText(this, "Loading News Over Mobile 3G ", Toast.LENGTH_LONG).show();
            String url = urlEditText.getText().toString();
            Log.d(TAG, "Adding new channel with URL " + url);
            showProgressDialog();
            new Thread(new AddChannelRunnable(url)).start();

        } else {
            Toast.makeText(this, "No Network WIFI/3G", Toast.LENGTH_LONG).show();
        }
    }


    private void showProgressDialog() {
        // @formatter:off
        progressDialog = ProgressDialog.show(AddChannelActivity.this,
            getResources().getText(R.string.please_wait), 
            getResources().getText(R.string.reading_feed), true, false);
        // @formatter:on
    }
    
    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    private void setResult(long id) {
        getIntent().setData(ContentManager.getChannelUri(id));
        setResult(RESULT_OK, getIntent());
    }
    
    private class AddChannelRunnable implements Runnable {
        private final String url;

        public AddChannelRunnable(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            try {
                final long id = addChannel(url);
                assert (id >= 0);
                postFinish(id);
            } catch (final Exception e) {
                //postShowError(e.toString());
            }
        }

        private long addChannel(String url) throws Exception {
            Synchronizer refresh = new Synchronizer(AddChannelActivity.this);
            return refresh.sync(-1, url);
        }


        private void postFinish(final long id) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    setResult(id);
                    finish();
                }
            });
        }

        private void postShowError(final String msg) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    DialogUtils.showErrorDialog(AddChannelActivity.this, msg);
                }
            });
        }
    }

}
