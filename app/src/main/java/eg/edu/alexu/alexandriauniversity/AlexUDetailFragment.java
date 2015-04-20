package eg.edu.alexu.alexandriauniversity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import eg.edu.alexu.alexandriauniversity.dataHelper.OptionsOfList;

/**
 * A fragment representing a single AlexU detail screen.
 * This fragment is either contained in a {@link AlexUListActivity}
 * in two-pane mode (on tablets) or a {@link AlexUDetailActivity}
 * on handsets.
 */
public class AlexUDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private OptionsOfList.OptionsItemsList mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlexUDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = OptionsOfList.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alexu_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
           // ((TextView) rootView.findViewById(R.id.alexu_detail)).setText(mItem.content);
        }

        return rootView;
    }
}
