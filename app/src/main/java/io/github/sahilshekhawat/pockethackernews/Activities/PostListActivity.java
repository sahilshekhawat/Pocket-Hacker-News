package io.github.sahilshekhawat.pockethackernews.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.sahilshekhawat.pockethackernews.Data.Data;
import io.github.sahilshekhawat.pockethackernews.Data.Items;
import io.github.sahilshekhawat.pockethackernews.Data.StoryType;
import io.github.sahilshekhawat.pockethackernews.R;
import io.github.sahilshekhawat.pockethackernews.dummy.DummyContent;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An activity representing a list of Posts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PostDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PostListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    Firebase firebase;
    Firebase firebaseTopStories = null;
    Firebase firebaseNewStories = null;
    Firebase firebaseBestStories = null;
    Firebase firebaseAskStories = null;
    Firebase firebaseShowStories = null;
    Firebase firebaseJobStories = null;
    Firebase firebaseItems = null;
    Data data;
    ArrayList<Items> dataSet = new ArrayList<>();
    ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    String currentStoryType;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        //Necessary to set Firebase context before using it.
        Firebase.setAndroidContext(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // setting swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_post_list_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
                itemRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        //Changing fonts
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/MuseoSans_500.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Search is TODO", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.post_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.post_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        //Setting data
        data = new Data();

        //Getting data
        firebase = new Firebase("https://hacker-news.firebaseio.com/v0/");
        firebaseItems = firebase.child("item");
        // Navigation Drawer
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        initNavigationDrawer();


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void initNavigationDrawer() {

        navigationView.setCheckedItem(R.id.top);
        if(firebaseTopStories == null)
            firebaseTopStories = firebase.child(StoryType.TOPSTORIES);
        currentStoryType = StoryType.TOPSTORIES;
        swipeRefreshLayout.setRefreshing(true);
        getData(firebaseTopStories, StoryType.TOPSTORIES);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.top:
                        Toast.makeText(getApplicationContext(),"Top",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        currentStoryType = StoryType.TOPSTORIES;
                        if(firebaseTopStories == null)
                            firebaseTopStories = firebase.child(StoryType.TOPSTORIES);
                        swipeRefreshLayout.setRefreshing(true);
                        getData(firebaseTopStories, StoryType.TOPSTORIES);
                        break;

                    case R.id.new_top:
                        Toast.makeText(getApplicationContext(),"New",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        currentStoryType = StoryType.NEWSTORIES;
                        if(firebaseNewStories == null)
                            firebaseNewStories = firebase.child(StoryType.NEWSTORIES);
                        swipeRefreshLayout.setRefreshing(true);
                        getData(firebaseNewStories, StoryType.NEWSTORIES);
                        break;

                    case R.id.ask_hn:
                        Toast.makeText(getApplicationContext(),"Ask HN",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        currentStoryType = StoryType.ASKSTORIES;
                        if(firebaseAskStories == null)
                            firebaseAskStories = firebase.child(StoryType.ASKSTORIES);
                        swipeRefreshLayout.setRefreshing(true);
                        getData(firebaseAskStories, StoryType.ASKSTORIES);
                        break;
                    case R.id.show_hn:
                        Toast.makeText(getApplicationContext(),"Show HN",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        currentStoryType = StoryType.SHOWSTORIES;
                        if(firebaseShowStories == null)
                            firebaseShowStories = firebase.child(StoryType.SHOWSTORIES);
                        swipeRefreshLayout.setRefreshing(true);
                        getData(firebaseShowStories, StoryType.SHOWSTORIES);
                        break;
                    case R.id.popular:
                        Toast.makeText(getApplicationContext(),"Popular",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        currentStoryType = StoryType.BESTSTORIES;
                        if(firebaseBestStories == null)
                            firebaseBestStories = firebase.child(StoryType.BESTSTORIES);
                        swipeRefreshLayout.setRefreshing(true);
                        getData(firebaseBestStories,  StoryType.BESTSTORIES);
                        break;
                    case R.id.bookmarks:
                        Toast.makeText(getApplicationContext(),"Bookmarks",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(PostListActivity.this, SettingsActivity.class);
//                        startActivity(intent);
                        break;
                    case R.id.feedback:
                        Toast.makeText(getApplicationContext(),"Feedback",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);
//        TextView login = (TextView)header.findViewById(R.id.Login);
//        login.setText("Login");

//        RelativeLayout nav_header_login = (RelativeLayout) header.findViewById(R.id.nav_header_login);
//        nav_header_login.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                final Snackbar snackbar = Snackbar.make(view, "Login feature will be implemented soon", Snackbar.LENGTH_LONG);
//                snackbar.setAction("DISMISS", new View.OnClickListener(){
//                            @Override
//                            public void onClick(View view){
//                                snackbar.dismiss();
//                            }
//                        }).show();
//            }
//        });
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    private void refreshContent(){
        swipeRefreshLayout.setRefreshing(true);
        getItemsForType(currentStoryType);
    }

    private void getItemsForType(String storyType){
        dataSet.clear();
        ArrayList<Long> stories = new ArrayList<>();
        if(storyType.equals(StoryType.TOPSTORIES))
            stories = data.topStories;
        if(storyType.equals(StoryType.BESTSTORIES))
            stories = data.bestStories;
        if(storyType.equals(StoryType.NEWSTORIES))
            stories = data.newStories;
        if(storyType.equals(StoryType.SHOWSTORIES))
            stories = data.showStories;
        if(storyType.equals(StoryType.ASKSTORIES))
            stories = data.askStories;


        for(Integer i = 0; i<stories.size(); i++  ){
            Long id = stories.get(i);
            if(data.items.get(id) == null){
                getItem(firebaseItems, id, storyType);
            } else{
                break;
            }
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(dataSet);
        recyclerView.setAdapter(itemRecyclerViewAdapter);
    }

    private void getData(Firebase firebase,final String child){
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                if(child.equals(StoryType.TOPSTORIES)){
                    data.topStories = (ArrayList<Long>)dataSnapshot.getValue();
                    getItemsForType(StoryType.TOPSTORIES);
                }
                if(child.equals(StoryType.NEWSTORIES)) {
                    data.newStories = (ArrayList<Long>)dataSnapshot.getValue();
                    getItemsForType(StoryType.NEWSTORIES);
                }
                if(child.equals(StoryType.ASKSTORIES)) {
                    data.askStories = (ArrayList<Long>)dataSnapshot.getValue();
                    getItemsForType(StoryType.ASKSTORIES);
                }
                if(child.equals(StoryType.SHOWSTORIES)) {
                    data.showStories = (ArrayList<Long>)dataSnapshot.getValue();
                    getItemsForType(StoryType.SHOWSTORIES);
                }
                if(child.equals(StoryType.BESTSTORIES)) {
                    data.bestStories = (ArrayList<Long>)dataSnapshot.getValue();
                    getItemsForType(StoryType.BESTSTORIES);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Read Failed with error: " + firebaseError.getMessage());
            }
        });
    }

    private void getItem(Firebase firebase, final Long id, final String storyType){
        Firebase firebaseItem = firebaseItems.child(Long.toString(id));
        firebaseItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                Items item = new Items();
                HashMap<String, Object> dataSnapshotValue = (HashMap<String, Object>) dataSnapshot.getValue();

                item.setId((Long) dataSnapshotValue.get("id"));
                item.setTitle((String) dataSnapshotValue.get("title"));
                item.setType((String) dataSnapshotValue.get("type"));
                item.setKids((ArrayList<Long>) dataSnapshotValue.get("kids"));
                item.setScore((Long) dataSnapshotValue.get("score"));
                item.setBy((String) dataSnapshotValue.get("by"));
                item.setText((String) dataSnapshotValue.get("text"));
                item.setTime((Long) dataSnapshotValue.get("time"));
//                    item.setDeleted(json.getBoolean("deleted"));
//                    item.setType(json.getString("type"));
//                    item.setBy(json.getString("by"));

                Data.items.put(item.id, item);

                if(currentStoryType.equals(storyType)){
                    dataSet.add(item);
                    itemRecyclerViewAdapter.notifyDataSetChanged();
                }

                if(swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                //data.items[id] = ()
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void swapListData(ArrayList<Items> items){
        //TODO
    }

    public class ItemRecyclerViewAdapter
            extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

        private final List<Items> mValues;

        public ItemRecyclerViewAdapter(List<Items> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            if(holder.mItem.title != null)
                holder.title.setText(mValues.get(position).title);
            if(holder.mItem.score != null)
                holder.score.setText(Long.toString(mValues.get(position).score));
            if(holder.mItem.time != null)
                holder.time.setText(Long.toString(mValues.get(position).time));
            if(holder.mItem.by != null)
                holder.by.setText(mValues.get(position).by);
            if(holder.mItem.text != null)
                holder.text.setText(mValues.get(position).text);
            if(holder.mItem.kids != null) {
                String comments = Integer.toString(mValues.get(position).kids.size());
                holder.numKids.setText(comments);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(PostDetailFragment.ARG_ITEM_ID, Long.toString(holder.mItem.id));
                        PostDetailFragment fragment = new PostDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.post_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, PostDetailActivity.class);
                        intent.putExtra(PostDetailFragment.ARG_ITEM_ID, Long.toString(holder.mItem.id));

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView title;
            public final TextView score;
            public final TextView time;
            public final TextView by;
            public final TextView text;
            public final TextView numKids;
            public Items mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                title = (TextView) view.findViewById(R.id.title);
                score = (TextView) view.findViewById(R.id.score);
                time = (TextView) view.findViewById(R.id.time);
                by = (TextView) view.findViewById(R.id.by);
                text = (TextView) view.findViewById(R.id.text);
                numKids = (TextView) view.findViewById(R.id.numKids);

            }

            @Override
            public String toString() {
                return super.toString() + " '" + title.getText() + "'";
            }
        }
    }
}
