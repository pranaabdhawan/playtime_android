package com.example.pranaab.playtime_android_app;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pranaab.playtime_android_app.Model.DummyContent;
import com.example.pranaab.playtime_android_app.Model.Event;
import com.example.pranaab.playtime_android_app.Model.EventRepository;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * TODO: Refurbish the event class (model) depending on the standard accepted in the backend.
     */
    private Event mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // TODO: Make a loader when making I/O calls
            mItem = EventRepository.events.get(Integer.valueOf(getArguments().getString(ARG_ITEM_ID)));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.getDetails());
            //((TextView) rootView.findViewById(R.id.item_detail_details_dynamic)).setText(mItem.getName());
//            ((TextView) rootView.findViewById(R.id.item_detail_name)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.item_detail_time)).setText("Time: " + mItem.getStart_time() + " - " + mItem.getEnd_time());
            ((TextView) rootView.findViewById(R.id.item_detail_location)).setText("Location: " + mItem.getLocation());
            ((TextView) rootView.findViewById(R.id.item_detail_capacity)).setText("Number of People: " + mItem.getNum_joined_members() + "/" + mItem.getMax_subscribers());
            ((TextView) rootView.findViewById(R.id.item_detail_spots_left)).setText(Integer.toString(mItem.getMax_subscribers() - mItem.getNum_joined_members()) + " spots left!");
            ProgressBar simpleProgressBar = (ProgressBar) rootView.findViewById(R.id.indeterminateBar);
            double progress = ((double)mItem.getNum_joined_members()/mItem.getMax_subscribers()) * 100;
            Log.i("progress", Double.toString(progress));
            simpleProgressBar.setProgress((int) progress);
        }

        return rootView;
    }
}