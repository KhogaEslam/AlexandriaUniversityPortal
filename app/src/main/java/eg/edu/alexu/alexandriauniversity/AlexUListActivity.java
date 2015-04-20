package eg.edu.alexu.alexandriauniversity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import eg.edu.alexu.alexandriauniversity.activity.ChannelsActivity;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link AlexUDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link AlexUListFragment} and the item details
 * (if present) is a {@link AlexUDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link AlexUListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class AlexUListActivity extends FragmentActivity
        implements AlexUListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alexu_list);


        /*ActionBarActivity ab = new ActionBarActivity();
        ab.getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        if (findViewById(R.id.alexu_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((AlexUListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.alexu_list))
                    .setActivateOnItemClick(true);
        }
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

            Bundle arguments = new Bundle();
            //arguments.putString(AlexUDetailFragment.ARG_ITEM_ID, id);
            AlexUDetailFragment fragment = new AlexUDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.alexu_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, AlexUDetailActivity.class);
            //detailIntent.putExtra(AlexUDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    /**
     * Callback method from {@link AlexUListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {

        //Check internet connection for now all times but next step will be first time only
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        switch (id) {
            case "1": //about activity
                if (mTwoPane) {
                    // In two-pane mode, show the detail view in this activity by
                    // adding or replacing the detail fragment using a
                    // fragment transaction.

                    Bundle arguments = new Bundle();
                    arguments.putString(AlexUDetailFragment.ARG_ITEM_ID, id);
                    AlexUDetailFragment fragment = new AlexUDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.alexu_detail_container, fragment)
                            .commit();

                } else {
                    // In single-pane mode, simply start the detail activity
                    // for the selected item ID.
                    Intent detailIntent = new Intent(this, AlexUDetailActivity.class);
                    detailIntent.putExtra(AlexUDetailFragment.ARG_ITEM_ID, id);
                    startActivity(detailIntent);
                }
                break;

            case "2": //News and Events Activity
                Intent channelsIntent = new Intent(this, ChannelsActivity.class);
                startActivity(channelsIntent);
                break;

            /*case "3":
                //Check internet connection for now all times but next step will be first time only

                if (wifi.isAvailable() && wifi.isConnected()) {

                    Toast.makeText(this, "Loading News over WIFI", Toast.LENGTH_LONG).show();
                    Intent newsIntent = new Intent(this, NewsArticleListActivity.class);
                    startActivity(newsIntent);
                } else if (mobile.isAvailable() && mobile.isConnected()) {
                    Toast.makeText(this, "Loading News Over Mobile 3G ", Toast.LENGTH_LONG).show();
                    Intent newsIntent = new Intent(this, NewsArticleListActivity.class);
                    startActivity(newsIntent);

                } else {
                    Toast.makeText(this, "No Network WIFI/3G", Toast.LENGTH_LONG).show();
                }
                break;
            case "4":
                //Check internet connection for now all times but next step will be first time only

                if (wifi.isAvailable() && wifi.isConnected()) {

                    Toast.makeText(this, "Loading News over WIFI", Toast.LENGTH_LONG).show();
                    Intent eventsIntent = new Intent(this, RssActivity.class);
                    startActivity(eventsIntent);
                } else if (mobile.isAvailable() && mobile.isConnected()) {
                    Toast.makeText(this, "Loading News Over Mobile 3G ", Toast.LENGTH_LONG).show();
                    Intent eventsIntent = new Intent(this, RssActivity.class);
                    startActivity(eventsIntent);

                } else {
                    Toast.makeText(this, "No Network WIFI/3G", Toast.LENGTH_LONG).show();
                }

                break;
*/
            case "5":
                Intent socialIntent = new Intent(this, SocialMediaActivity.class);
                startActivity(socialIntent);
                break;
            case "6":
                Intent contactIntent = new Intent(this, ContactAUActivity.class);
                startActivity(contactIntent);
                break;
            case "7":
                //Check internet connection for now all times but next step will be first time only

                if (wifi.isAvailable() && wifi.isConnected()) {

                    Toast.makeText(this, "Loading Location over WIFI", Toast.LENGTH_LONG).show();
                    Intent mapIntent = new Intent(this, MapsActivity.class);
                    startActivity(mapIntent);
                } else if (mobile.isAvailable() && mobile.isConnected()) {
                    Toast.makeText(this, "Loading Location Over Mobile 3G ", Toast.LENGTH_LONG).show();
                    Intent mapIntent = new Intent(this, MapsActivity.class);
                    startActivity(mapIntent);

                } else {
                    Toast.makeText(this, "No Network WIFI/3G", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
