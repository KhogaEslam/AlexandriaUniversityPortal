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

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
//import android.support.v7.widget.ShareActionProvider;
import android.widget.ShareActionProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import eg.edu.alexu.alexandriauniversity.R;
import eg.edu.alexu.alexandriauniversity.content.ContentManager;
import eg.edu.alexu.alexandriauniversity.model.Channel;
import eg.edu.alexu.alexandriauniversity.model.Item;
import eg.edu.alexu.alexandriauniversity.util.KeyUtils;
import eg.edu.alexu.alexandriauniversity.util.MenuUtils;
import eg.edu.alexu.alexandriauniversity.view.ChannelHeader;

public class ViewItemActivity extends FragmentActivity {

    private static final String ALEXU_SHARE_HASHTAG = " #AlexUApp";
    private String mAlexU;

    private static final String TAG = "ViewItemActivity";

    public static final String EXTRA_POST_IDS = "itemIds";

    public static final String[] PROJECTION = new String[] { Item._ID,
        Item.CHANNEL_ID, Item.TITLE, Item.AUTHOR, Item.DESCRIPTION,
        Item.CONTENTS, Item.READ, Item.LINK, Item.DATE };

    private static final SimpleDateFormat DATE_FMT_TODAY = new SimpleDateFormat(
        "h:mma");
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat(
        "MM/dd/yyyy h:mma");

    // @formatter:off
    private static final String HTML_HEADER = 
"<html>" + 
    "<head>" +
        "<meta charset=\"UTF-8\">"+
        "<style type=\"text/css\">body { background-color: black; color: LightGray; } a { color: #ddf; }"
        + "</style>" + 
    "</head>" + 
    "<body>";

    private static final String HTML_FOOTER = 
    "</body>" + 
"</html>";
    // @formatter:on

    private static final int ITEM_LOADER_ID = 0x03;

    private ContentManager cm;
    private long[] itemIds;
    private long itemId = -1;
    private long newerItemId = -1;
    private long olderItemId = -1;
    private ChannelHeader header;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView dateTextView;
    private WebView contentsWebView;
    private ShareActionProvider mShareActionProvider;

    private final LoaderManager.LoaderCallbacks<Item> ilc = new LoaderManager.LoaderCallbacks<Item>() {

        @Override
        public Loader<Item> onCreateLoader(int id, Bundle args) {
            assert (id == ITEM_LOADER_ID);
            return new ItemLoader(ViewItemActivity.this, cm, itemId);
        }

        @Override
        public void onLoadFinished(Loader<Item> loader, Item item) {
            setData(item);
            // We still need this for the share intent
            ArrayList<String> ar = getDataForShareIntent(item);
            ar.toArray();
            mAlexU = String.format("%s - %s - %s/%s", ar.get(0), ar.get(1), ar.get(2), ar.get(3));

            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareIntent());
            }
        }

        @Override
        public void onLoaderReset(Loader<Item> loader) {
        }
    };

    private static class ItemLoader extends AsyncTaskLoader<Item> {

        private final ContentManager cm;
        private final long itemId;

        public ItemLoader(Context context, ContentManager cm, long itemId) {
            super(context);
            this.cm = cm;
            this.itemId = itemId;
        }

        @Override
        public Item loadInBackground() {
            Item item = cm.queryItemById(itemId);
            item.setRead(true);
            cm.updateItem(item);
            Channel channel = cm.queryChannelById(item.getChannelId());
            item.setChannel(channel);
            return item;
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.view_item_activity);
        cm = ContentManager.getInstance(getContentResolver());
        itemIds = getIntent().getLongArrayExtra(EXTRA_POST_IDS);
        itemId = getItemId();
        initLoaders();
        initSiblings();
        initControls();
    }

    @Override
    protected void onStart() {
        super.onStart();
        forceLoad();
    }

    private long getItemId() {
        return Long.parseLong(getIntent().getData().getPathSegments().get(1));
    }

    private void initSiblings() {
        if (itemIds != null) {
            int i;
            for (i = 0; i < itemIds.length; i++) {
                if (itemIds[i] == itemId)
                    break;
                newerItemId = itemIds[i];
            }
            if (i < itemIds.length - 1) {
                olderItemId = itemIds[i + 1];
            }
        }
    }

    private void initControls() {
        header = (ChannelHeader) findViewById(R.id.channelHeader);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        authorTextView = (TextView) findViewById(R.id.authorTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        contentsWebView = (WebView) findViewById(R.id.contentsWebView);
    }

    private void initLoaders() {
        getSupportLoaderManager().initLoader(ITEM_LOADER_ID, null, ilc);
    }

    private void forceLoad() {
        getSupportLoaderManager().getLoader(ITEM_LOADER_ID).forceLoad();
    }

    private void setData(Item item) {
        Channel channel = item.getChannel();
        header.setData(channel.getId(), channel.getTitle(),
            channel.getDescription(), channel.getIcon());

        titleTextView.setText(item.getTitle());

        authorTextView.setText(item.getAuthor());

        Date date = item.getDate();
        dateTextView.setText(getDateFormat(date).format(date));

        contentsWebView.loadData(HTML_HEADER + getItemBody(item) + HTML_FOOTER,
            "text/html", "utf-8");
    }

    private static SimpleDateFormat getDateFormat(Date date) {
        Calendar then = new GregorianCalendar();
        then.setTime(date);
        Calendar now = new GregorianCalendar();
        SimpleDateFormat fmt;
        if (now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR))
            fmt = DATE_FMT_TODAY;
        else
            fmt = DATE_FMT;
        return fmt;
    }

    private String getItemBody(Item item) {
        String body = "";
        String contents = item.getContents();

        try
        {
            final String s = new String(contents.getBytes(), "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            Log.e("utf8", "conversion", e);
        }

        if (contents != null && contents.length() > 0) {
            body += contents;
        } else {
            String description = item.getDescription();
            if (description != null && description.length() > 0) {
                body += description;
                String link = item.getLink();
                if (hasMoreLink(description, link) == false) {
                    body += "<p><a href=\"" + link + "\">"
                        + getResources().getText(R.string.read_more)
                        + "</a></p>";
                }
            }
        }
        return body;
    }

    private static boolean hasMoreLink(String body, String url) {
        int pos = body.indexOf(url);
        if (pos <= 0)
            return false;
        try {
            if (body.charAt(pos - 1) != '>')
                return false;
            else if (body.charAt(pos + url.length() + 1) != '<')
                return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.view_item_menu, menu);
        MenuUtils.setShowAsActionAll(menu);
        if (newerItemId < 0) {
            menu.removeItem(R.id.newer_item);
        }
        if (olderItemId < 0) {
            menu.removeItem(R.id.older_item);
        }

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) menuItem.getActionProvider();

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mAlexU != null) {
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareIntent());
            }
        }

        return true;
    }

    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mAlexU + ALEXU_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.newer_item:
            return newerItem();
        case R.id.older_item:
            return olderItem();
        case R.id.action_share:
            Intent shareIntent = new Intent(createShareIntent());
            startActivity(shareIntent);
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (KeyUtils.interpretDirection(keyCode)) {
        case KeyEvent.KEYCODE_DPAD_LEFT:
            return newerItem();
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            return olderItem();
        default:
            return super.onKeyUp(keyCode, event);
        }
    }

    private boolean newerItem() {
        if (newerItemId < 0)
            return false;
        viewItem(newerItemId);
        return true;
    }

    private boolean olderItem() {
        if (olderItemId < 0)
            return false;
        viewItem(olderItemId);
        return true;
    }

    private void viewItem(long itemId) {
        Log.d(TAG, "Viewing item " + itemId);
        Uri uri = ContentManager.getItemUri(itemId);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(EXTRA_POST_IDS, itemIds);
        startActivity(intent);
        finish();
    }


    private ArrayList<String> getDataForShareIntent(Item item) {
        ArrayList<String> ar = new ArrayList<>();

        ar.add(item.getTitle());

        ar.add(item.getAuthor());

        Date date = item.getDate();
        ar.add(getDateFormat(date).format(date));

        ar.add(getItemBody(item));

        return ar;
    }
}
