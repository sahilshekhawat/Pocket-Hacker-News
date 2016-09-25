package io.github.sahilshekhawat.pockethackernews.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.app.*;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import io.github.sahilshekhawat.pockethackernews.Activities.PostDetailFragment;
import io.github.sahilshekhawat.pockethackernews.Activities.PostListActivity;
import io.github.sahilshekhawat.pockethackernews.Data.Data;
import io.github.sahilshekhawat.pockethackernews.Data.Items;
import io.github.sahilshekhawat.pockethackernews.R;
import io.github.sahilshekhawat.pockethackernews.Utils.ConvertTime;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * An activity representing a single Post detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PostListActivity}.
 */
public class PostDetailActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Items item;
    private AppBarLayout appBarLayout;
    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView nestedScrollView;
    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        if(toolbar != null){
            toolbar.setTitle("");
        }

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        nestedScrollView = (NestedScrollView) findViewById(R.id.post_detail_container);
        //Changing fonts
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/MuseoSans_500.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        //Setting up Floading action bar for
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {


               //nestedScrollView.scrollTo(0, 0);

               CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
               AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
               if (behavior != null) {
                   behavior.onNestedFling(coordinatorLayout, appBarLayout, null, 0, 10000, true);
               }
                //Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });

        //Status bar color change
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        // Show the Up button in the action bar.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Pocket Hacker News");
        }
        Long id = Long.parseLong(getIntent().getStringExtra(PostDetailFragment.ARG_ITEM_ID));
        item = Data.items.get(id);

        TextView titleTextView = (TextView) findViewById(R.id.articleTitle);
        TextView pointsTextView = (TextView) findViewById(R.id.points);
        TextView commentsTextView = (TextView) findViewById(R.id.comments);
        TextView byTextView = (TextView) findViewById(R.id.by);
        TextView timeTextView = (TextView) findViewById(R.id.time);
        TextView externalUrlTextView = (TextView) findViewById(R.id.externalUrl);
        TextView hnUrlTextView = (TextView) findViewById(R.id.hnUrl);

        if(item.getTitle() != null){
            titleTextView.setText(item.getTitle());
        }
        if(item.getScore() != null){
            pointsTextView.setText(Long.toString(item.getScore()));
        }

        if(item.getKids() != null){
            commentsTextView.setText(Integer.toString(item.getKids().size()) + " comments");
        }

        if(item.getBy() != null){
            byTextView.setText(item.getBy());
        }

        if(item.getUrl() != null){
            externalUrlTextView.setText(item.getUrl());
        }

        hnUrlTextView.setText("https://news.ycombinator.com/item?id=" + Long.toString(item.getId()));

        if(item.getTime() != null){
            timeTextView.setText(ConvertTime.toSmallHumanReadable(item.time));
        }

        externalUrlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetailActivity.this, WebViewActivity.class);
                intent.putExtra("url", item.url);
                startActivity(intent);
            }
        });

        hnUrlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetailActivity.this, WebViewActivity.class);
                intent.putExtra("url", "https://news.ycombinator.com/item?id="+Long.toString(item.id));
                startActivity(intent);
            }
        });

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(PostDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(PostDetailFragment.ARG_ITEM_ID));
            PostDetailFragment fragment = new PostDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.post_detail_container, fragment)
                    .commit();
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post_details, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void showShareDialog() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, "Pocket Hacker News");
        intent.putExtra(Intent.EXTRA_TEXT, item.url);

        startActivity(Intent.createChooser(intent, getString(R.string.menu_share)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, PostListActivity.class));
            return true;
        }
        if (id == R.id.menu_share) {
            showShareDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}
