package io.github.sahilshekhawat.pockethackernews.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.github.sahilshekhawat.pockethackernews.Data.Data;
import io.github.sahilshekhawat.pockethackernews.Data.Items;
import io.github.sahilshekhawat.pockethackernews.Data.StoryType;
import io.github.sahilshekhawat.pockethackernews.R;
import io.github.sahilshekhawat.pockethackernews.Utils.ConvertTime;
import io.github.sahilshekhawat.pockethackernews.dummy.DummyContent;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    RecyclerView recyclerView;
    ItemRecyclerViewAdapter itemRecyclerViewAdapter;
    String currentStoryType;
    SwipeRefreshLayout swipeRefreshLayout;
    private final Object lock = new Object();

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

        //Hide fab for now.
        fab.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.post_list);

        if (findViewById(R.id.post_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

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

    private void setupRecyclerViewAdapter(@NonNull RecyclerView recyclerView, ArrayList<Items> dataSet) {
        itemRecyclerViewAdapter = new ItemRecyclerViewAdapter(dataSet);
        recyclerView.setAdapter(itemRecyclerViewAdapter);
        itemRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void refreshContent(){
        Log.d("refreshContent()", "called!");
        if(!swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(true);
        }
        //Remove all previous items.
        Data.setAllItems(currentStoryType, new ArrayList<Items>());
        //All all items order wise, skip if not present.
        ArrayList<Long> stories = Data.getAllStories(currentStoryType);
        for(Long id: stories){
            if(Data.items.get(id) != null){
                Data.addItem(currentStoryType, Data.items.get(id));
            }
        }
        setupRecyclerViewAdapter(recyclerView, Data.getAllItems(currentStoryType));
        swipeRefreshLayout.setRefreshing(false);
    }

    public void initNavigationDrawer() {

        navigationView.setCheckedItem(R.id.top);
        if(firebaseTopStories == null) {
            firebaseTopStories = firebase.child(StoryType.TOPSTORIES);
        }
        currentStoryType = StoryType.TOPSTORIES;
        swipeRefreshLayout.setRefreshing(true);
        setupRecyclerViewAdapter(recyclerView, Data.topStoryItems);
        getData(firebaseTopStories, StoryType.TOPSTORIES);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.top:
                        //Toast.makeText(getApplicationContext(),"Top",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        currentStoryType = StoryType.TOPSTORIES;
                        //swipeRefreshLayout.setRefreshing(true);
                        setupRecyclerViewAdapter(recyclerView, Data.topStoryItems);
                        if(firebaseTopStories == null){
                            swipeRefreshLayout.setRefreshing(true);
                            firebaseTopStories = firebase.child(StoryType.TOPSTORIES);
                            getData(firebaseTopStories, StoryType.TOPSTORIES);
                        } else{
                            getItemsForType(StoryType.TOPSTORIES);
                        }

                        break;

                    case R.id.new_top:
                        //Toast.makeText(getApplicationContext(),"New",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        currentStoryType = StoryType.NEWSTORIES;
                        //swipeRefreshLayout.setRefreshing(true);
                        setupRecyclerViewAdapter(recyclerView, Data.newStoryItems);
                        if(firebaseNewStories == null){
                            swipeRefreshLayout.setRefreshing(true);
                            firebaseNewStories = firebase.child(StoryType.NEWSTORIES);
                            getData(firebaseNewStories, StoryType.NEWSTORIES);
                        } else{
                            getItemsForType(StoryType.NEWSTORIES);
                        }

                        break;
                    case R.id.ask_hn:
                        //Toast.makeText(getApplicationContext(),"Ask HN",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        currentStoryType = StoryType.ASKSTORIES;
                        //swipeRefreshLayout.setRefreshing(true);
                        setupRecyclerViewAdapter(recyclerView, Data.askStoryItems);
                        if(firebaseAskStories == null){
                            swipeRefreshLayout.setRefreshing(true);
                            firebaseAskStories = firebase.child(StoryType.ASKSTORIES);
                            getData(firebaseAskStories, StoryType.ASKSTORIES);

                        } else{
                            getItemsForType(StoryType.ASKSTORIES);
                        }

                        break;
                    case R.id.show_hn:
                        //Toast.makeText(getApplicationContext(),"Show HN",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        currentStoryType = StoryType.SHOWSTORIES;
                        //swipeRefreshLayout.setRefreshing(true);
                        setupRecyclerViewAdapter(recyclerView, Data.showStoryItems);
                        if(firebaseShowStories == null){
                            swipeRefreshLayout.setRefreshing(true);
                            firebaseShowStories = firebase.child(StoryType.SHOWSTORIES);
                            getData(firebaseShowStories, StoryType.SHOWSTORIES);
                        } else{
                            getItemsForType(StoryType.SHOWSTORIES);
                        }

                        break;
                    case R.id.popular:
                        //Toast.makeText(getApplicationContext(),"Popular",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        currentStoryType = StoryType.BESTSTORIES;
                        //swipeRefreshLayout.setRefreshing(true);
                        setupRecyclerViewAdapter(recyclerView, Data.bestStoryItems);
                        if(firebaseBestStories == null){
                            swipeRefreshLayout.setRefreshing(true);
                            firebaseBestStories = firebase.child(StoryType.BESTSTORIES);
                            getData(firebaseBestStories,  StoryType.BESTSTORIES);
                        } else{
                            getItemsForType(StoryType.BESTSTORIES);
                        }
                        break;
                    case R.id.jobs:
                        drawerLayout.closeDrawers();
                        currentStoryType = StoryType.JOBSTORIES;
                        //
                        setupRecyclerViewAdapter(recyclerView, Data.jobStoryItems);
                        if(firebaseJobStories == null){
                            swipeRefreshLayout.setRefreshing(true);
                            firebaseJobStories = firebase.child(StoryType.JOBSTORIES);
                            getData(firebaseJobStories,  StoryType.JOBSTORIES);
                        } else{
                            getItemsForType(StoryType.JOBSTORIES);
                        }
                        break;
                    /*case R.id.bookmarks:
                        Toast.makeText(getApplicationContext(),"Bookmarks",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(PostListActivity.this, SettingsActivity.class);
//                        startActivity(intent);
                        break;*/
                    case R.id.feedback:
                        Toast.makeText(getApplicationContext(),"Feedback",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

//        View header = navigationView.getHeaderView(0);
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


    private void getData(Firebase firebase,final String child){
        Log.d("getData()", child);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("getData() " + child, ((ArrayList<Long>) dataSnapshot.getValue()).toString());
                if(child.equals(StoryType.TOPSTORIES)){
                    Data.topStories = (ArrayList<Long>)dataSnapshot.getValue();
                    getItemsForType(StoryType.TOPSTORIES);
                }
                if(child.equals(StoryType.NEWSTORIES)) {
                    Data.newStories = (ArrayList<Long>)dataSnapshot.getValue();
                    getItemsForType(StoryType.NEWSTORIES);
                }
                if(child.equals(StoryType.ASKSTORIES)) {
                    Data.askStories = (ArrayList<Long>)dataSnapshot.getValue();
                    getItemsForType(StoryType.ASKSTORIES);
                }
                if(child.equals(StoryType.SHOWSTORIES)) {
                    Data.showStories = (ArrayList<Long>)dataSnapshot.getValue();
                    getItemsForType(StoryType.SHOWSTORIES);
                }
                if(child.equals(StoryType.BESTSTORIES)) {
                    Data.bestStories = (ArrayList<Long>)dataSnapshot.getValue();
                    getItemsForType(StoryType.BESTSTORIES);
                }
                if(child.equals(StoryType.JOBSTORIES)) {
                    Data.jobStories = (ArrayList<Long>)dataSnapshot.getValue();
                    getItemsForType(StoryType.JOBSTORIES);
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Read Failed with error: " + firebaseError.getMessage());
            }
        });
    }

    private void getItemsForType(String storyType){
        Log.d("getItemsForType()", storyType);
        ArrayList<Long> stories = new ArrayList<>();
        ArrayList<Items> storiesItems = new ArrayList<>();
        if(storyType.equals(StoryType.TOPSTORIES)) {
            stories = Data.topStories;
            //storiesItems = Data.topStoryItems;
        }
        if(storyType.equals(StoryType.BESTSTORIES)) {
            stories = Data.bestStories;
            //storiesItems = Data.bestStoryItems;
        }
        if(storyType.equals(StoryType.NEWSTORIES)) {
            stories = Data.newStories;
            //storiesItems = Data.newStoryItems;
        }
        if(storyType.equals(StoryType.SHOWSTORIES)) {
            stories = Data.showStories;
            //storiesItems = Data.showStoryItems;
        }
        if(storyType.equals(StoryType.ASKSTORIES)) {
            stories = Data.askStories;
            //storiesItems = Data.askStoryItems;
        }
        if(storyType.equals(StoryType.JOBSTORIES)) {
            stories = Data.jobStories;
        }

        Log.d("getItemsForType()", Integer.toString(stories.size()));

        int lastPosition = 0;
        for(Integer i = 0; i<stories.size(); i++  ) {
            Long id = stories.get(i);
            if (Data.items.get(id) == null) {
                lastPosition = i;
            }
        }

        for(Integer i = 0; i<stories.size(); i++  ) {
            Long id = stories.get(i);
            if (i == stories.size() - 1) {
                Log.d("getItemsForType()", "Done:)");
            }
            if (Data.items.get(id) == null) {
                System.out.print(Integer.toString(i) + ",");
                getItem(id, storyType, i, lastPosition);
            }
        }
    }

    private void getItem(final Long id, final String storyType, final int position, final int storySize){
        //
        Firebase firebaseItem = firebaseItems.child(Long.toString(id));
//        ArrayList<Items> tmp = new ArrayList<>();
//        Items newItem = new Items();
//        newItem.id = id;
//        Data.addItem(storyType, position, newItem);

        firebaseItem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.println(dataSnapshot.getValue());
                Log.d("getItem()", storyType + " " + Integer.toString(position));
                Items item = new Items();
//                HashMap<String, Object> dataSnapshotValue = (HashMap<String, Object>) dataSnapshot.getValue();

                if(dataSnapshot.hasChild("id")) {
                    item.setId((Long) dataSnapshot.child("id").getValue());
                }

                if(dataSnapshot.hasChild("title")) {
                    item.setTitle((String) dataSnapshot.child("title").getValue());
                }

                if(dataSnapshot.hasChild("type")) {
                    item.setType((String) dataSnapshot.child("type").getValue());
                }

                if(dataSnapshot.hasChild("kids")) {
                    item.setKids((ArrayList<Long>) dataSnapshot.child("kids").getValue());
                }

                if(dataSnapshot.hasChild("score")) {
                    item.setScore((Long) dataSnapshot.child("score").getValue());
                }

                if(dataSnapshot.hasChild("by")) {
                    item.setBy((String) dataSnapshot.child("by").getValue());
                }

                if(dataSnapshot.hasChild("text")) {
                    item.setText((String) dataSnapshot.child("text").getValue());
                }

                if(dataSnapshot.hasChild("time")) {
                    item.setTime((Long) dataSnapshot.child("time").getValue());
                }

                if(dataSnapshot.hasChild("url")) {
                    item.setUrl((String) dataSnapshot.child("url").getValue());
                }

                boolean isNewItem = false;
                Items prevItem = Data.items.get(item.id);
                if(prevItem == null) {
                    isNewItem = true;
                    new ArticleMetaTagParser().execute(id);
                } else if(prevItem.articleTitle == null){
                    new ArticleMetaTagParser().execute(id);
                }

                //Adding prev data to it.
                if(prevItem != null) {
                    if(prevItem.articleTitle != null){
                        item.articleTitle = prevItem.articleTitle;
                    }
                    if(prevItem.articleText != null){
                        item.articleText = prevItem.articleText;
                    }
                    if(prevItem.articleImageURL != null){
                        item.articleImageURL = prevItem.articleImageURL;
                    }
                }

                Data.items.put(item.id, item);
                if(isNewItem && position == storySize){
                    refreshContent();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private String getTitle(Document doc){
        Elements titleTags = doc.select("title");
        if(titleTags != null && titleTags.size() > 0){
            return titleTags.get(0).text();
        } else{
            Elements metaTags = doc.select("meta");
            for(Element meta: metaTags){
                if(meta.attr("name").toLowerCase().contains("title")){
                    return meta.attr("content");
                }
            }

        }
        return null;
    }

    private String getThumbnail(Document doc){
        Elements metaTags = doc.select("meta");
        for(Element meta: metaTags){
            if(meta.attr("name").toLowerCase().contains("image")){
                return meta.attr("content");
            }
        }
        return null;
    }

    private String getDescription(Document doc){
        Elements metaTags = doc.select("meta");
        for(Element meta: metaTags){
            if(meta.attr("name").toLowerCase().contains("description")){
                return meta.attr("content");
            }
        }
        return null;
    }


    private class ArticleMetaTagParser extends AsyncTask<Long, Void, Long> {
        @Override
        protected Long doInBackground(Long... longs) {
            Long id = longs[0];
            Items item = Data.items.get(id);
            try {
                Document doc = Jsoup.connect(item.url).ignoreContentType(true).get();
                String title = getTitle(doc);
                String image = getThumbnail(doc);
                String text = getDescription(doc);
                item.articleTitle = title;
                item.articleImageURL = image;
                item.articleText = text;
                Data.items.put(id, item);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return id;
        }

        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);

            int position = Data.itemContains(currentStoryType, id);
            if(position != -1){
                itemRecyclerViewAdapter.notifyItemChanged(position);
            }

            //Pre load images for faster loading.
            Items item = Data.items.get(id);
            if(item != null && item.articleImageURL != null) {
                try {
                    UrlImageViewHelper.loadUrlDrawable(getBaseContext(), item.articleImageURL);
                } catch(Exception e){
                    //skip if any error occured.
                    e.printStackTrace();
                }
            }
        }
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
            if(holder.mItem.score != null) {
                holder.mView.findViewById(R.id.points_icon).setVisibility(View.VISIBLE);
                holder.score.setText(Long.toString(mValues.get(position).score) + " points");
            } else{
                holder.mView.findViewById(R.id.points_icon).setVisibility(View.GONE);
            }
            if(holder.mItem.time != null){
                String assignedTime = ConvertTime.toHumanReadable(mValues.get(position).time);
                holder.time.setText(assignedTime);
            }
            if(holder.mItem.by != null)
                holder.by.setText(mValues.get(position).by);
            if(holder.mItem.text != null)
                holder.text.setText(Html.fromHtml(mValues.get(position).text.trim()));
            if(holder.text.getText().length() < 2){
                TextView textView = (TextView) holder.mView.findViewById(R.id.text);
                textView.setVisibility(View.GONE);
            }
            if(holder.mItem.kids != null) {
                holder.mView.findViewById(R.id.comment_icon).setVisibility(View.VISIBLE);
                String comments = Integer.toString(mValues.get(position).kids.size());
                holder.numKids.setText(comments + " comments");
            } else{
                holder.mView.findViewById(R.id.comment_icon).setVisibility(View.GONE);
            }

            if(holder.mItem.url != null){
                try {
                    URI uri = new URI(holder.mItem.url);
                    String domain = uri.getHost();
                    holder.url.setText(domain);

                    LinearLayout postLinkLinearLayout = (LinearLayout) holder.mView.findViewById(R.id.post_link);
                    postLinkLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(PostListActivity.this, WebViewActivity.class);
                            intent.putExtra("url", holder.mItem.url);
                            startActivity(intent);
                        }
                    });

                    TextView urlTitleTextView = (TextView)holder.mView.findViewById(R.id.urlTitle);
                    TextView urlTextTextView = (TextView)holder.mView.findViewById(R.id.urlText);
                    ImageView urlImageView = (ImageView) holder.mView.findViewById(R.id.urlImage);
                    if(holder.mItem.articleTitle != null){
                        urlTitleTextView.setVisibility(View.VISIBLE);
                        urlTitleTextView.setText(holder.mItem.articleTitle);
                    } else{
                        urlTitleTextView.setVisibility(View.GONE);
                    }

                    if(holder.mItem.articleText != null){
                        urlTextTextView.setVisibility(View.VISIBLE);
                        urlTextTextView.setText(holder.mItem.articleText);
                    } else{
                        urlTextTextView.setVisibility(View.GONE);
                    }

                    if(holder.mItem.articleImageURL != null){
                        urlImageView.setVisibility(View.VISIBLE);
                        UrlImageViewHelper.setUrlDrawable(urlImageView, holder.mItem.articleImageURL);
                    } else{
                        urlImageView.setVisibility(View.GONE);
                    }

                } catch(URISyntaxException e){
                    e.printStackTrace();
                }

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
            public final TextView url;
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
                url = (TextView) view.findViewById(R.id.url);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + title.getText() + "'";
            }
        }
    }
}