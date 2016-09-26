package io.github.sahilshekhawat.pockethackernews.Activities;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.github.sahilshekhawat.pockethackernews.Data.Data;
import io.github.sahilshekhawat.pockethackernews.Data.Items;
import io.github.sahilshekhawat.pockethackernews.R;
import io.github.sahilshekhawat.pockethackernews.Utils.ConvertTime;
import io.github.sahilshekhawat.pockethackernews.dummy.DummyContent;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * A fragment representing a single Post detail screen.
 * This fragment is either contained in a {@link PostListActivity}
 * in two-pane mode (on tablets) or a {@link PostDetailActivity}
 * on handsets.
 */
public class PostDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Items mItem;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Items> dataSet;
    private ItemCommentsRecyclerViewAdapter itemCommentsRecyclerViewAdapter;
    private Firebase firebase;
    private Firebase firebaseItems;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Necessary to set Firebase context before using it.
        Firebase.setAndroidContext(getContext());

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = Data.items.get(Long.parseLong(getArguments().getString(ARG_ITEM_ID)));
            Activity activity = this.getActivity();
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle("");
//            }
        }

        firebase = new Firebase("https://hacker-news.firebaseio.com/v0/");
        firebaseItems = firebase.child("item");


        Log.d("mItem Kids size:", Integer.toString(mItem.kids.size()));
        Data.comments.clear();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.post_detail, container, false);

        // Show the dummy content as text in a TextView.
        //if (mItem != null) {
        //    ((TextView) rootView.findViewById(R.id.post_detail)).setText(mItem.text);
        // }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.post_detail_list);
        recyclerView.setNestedScrollingEnabled(false);
        //swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_post_detail_swipe_refresh_layout);

/*
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

*/
        for(int i=0; i<mItem.kids.size(); i++){
            Long id = mItem.kids.get(i);

            if(Data.items.get(id) == null){
                getItem(mItem.kids.get(i), i, mItem.kids.size(), 0);
            } else{
                Data.comments.add(Data.items.get(id));
            }
        }
        setupRecyclerViewAdapter(recyclerView);

        //swipeRefreshLayout.setRefreshing(true);
        return rootView;
    }


    private void setupRecyclerViewAdapter(@NonNull RecyclerView recyclerView) {
        Log.d("setupAdapter", "called!");
        itemCommentsRecyclerViewAdapter = new ItemCommentsRecyclerViewAdapter(Data.comments);
        recyclerView.setAdapter(itemCommentsRecyclerViewAdapter);
        itemCommentsRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void refreshContent(){
        Log.d("refreshContent()", "called!");
        /*if(!swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(true);
        }*/
        //Remove all previous items.
        Data.comments.clear();
        for(int i=0; i<mItem.kids.size(); i++){
            if(Data.items.get(mItem.kids.get(i)) != null){
                Data.comments.add(Data.items.get(mItem.kids.get(i)));
            }
        }
        setupRecyclerViewAdapter(recyclerView);
        //swipeRefreshLayout.setRefreshing(false);
    }

    private void getItem(final Long id, final int position, final int size, final int level,) {

        Firebase firebaseItem = firebaseItems.child(Long.toString(id));
        firebaseItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("getItem()", "position" + Integer.toString(position));
                Items item = new Items();

                if (dataSnapshot.hasChild("id")) {
                    item.setId((Long) dataSnapshot.child("id").getValue());
                }

                if (dataSnapshot.hasChild("title")) {
                    item.setTitle((String) dataSnapshot.child("title").getValue());
                }

                if (dataSnapshot.hasChild("type")) {
                    item.setType((String) dataSnapshot.child("type").getValue());
                }

                if (dataSnapshot.hasChild("kids")) {
                    item.setKids((ArrayList<Long>) dataSnapshot.child("kids").getValue());
                }

                if (dataSnapshot.hasChild("score")) {
                    item.setScore((Long) dataSnapshot.child("score").getValue());
                }

                if (dataSnapshot.hasChild("by")) {
                    item.setBy((String) dataSnapshot.child("by").getValue());
                }

                if (dataSnapshot.hasChild("text")) {
                    item.setText((String) dataSnapshot.child("text").getValue());
                }

                if (dataSnapshot.hasChild("time")) {
                    item.setTime((Long) dataSnapshot.child("time").getValue());
                }

                if (dataSnapshot.hasChild("url")) {
                    item.setUrl((String) dataSnapshot.child("url").getValue());
                }

                item.level = level;

                boolean isNewItem = false;
                Items prevItem = Data.items.get(item.id);
                if (prevItem == null) {
                    isNewItem = true;
                }
                Data.items.put(item.id, item);
                if (isNewItem && position + 1 == size) {
                    refreshContent();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    //Adapter
    public class ItemCommentsRecyclerViewAdapter
            extends RecyclerView.Adapter<ItemCommentsRecyclerViewAdapter.ViewHolder> {

        private final List<Items> mValues;

        public ItemCommentsRecyclerViewAdapter(List<Items> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_details_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            //Setting appropriate margins for comments.
            LinearLayout linearLayout = (LinearLayout) holder.mView.findViewById(R.id.post_detail_main_linear_layout);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)linearLayout.getLayoutParams();
            params.setMargins(10+(holder.mItem.level*5),0,0,0);
            linearLayout.setLayoutParams(params);

            if(holder.mItem.by != null){
                holder.by.setText(holder.mItem.by);
            }
            if(holder.mItem.time != null){
                holder.time.setText(ConvertTime.toHumanReadable(holder.mItem.time));
            }
            if(holder.mItem.text != null){
                holder.text.setText(Html.fromHtml(holder.mItem.text.trim()));
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView time;
            public final TextView by;
            public final TextView text;
            public Items mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                time = (TextView) view.findViewById(R.id.time);
                by = (TextView) view.findViewById(R.id.by);
                text = (TextView) view.findViewById(R.id.text);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + text.getText() + "'";
            }
        }
    }
}
