package com.example.newsviews.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.newsviews.R;
import com.example.newsviews.databinding.ActivityMainBinding;
import com.example.newsviews.databinding.NavHeaderBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private final static String TAG = SearchResultsActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 100;
    ActivityMainBinding mainBinding;
    NavHeaderBinding navBinding;
    private ActionBarDrawerToggle drawerToggle;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Firing Sign Up
        fireSignUp();
        //Setting content view (The main UI) through data binding
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Setting the new toolbar over action bar
        setSupportActionBar((Toolbar) mainBinding.toolbar);

        // Find our drawer view
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mainBinding.drawerLayout.addDrawerListener(drawerToggle);

        setupDrawerContent(mainBinding.navView);

        //Sets Text for Header views
        navBinding = NavHeaderBinding.bind(mainBinding.navView.getHeaderView(0));
        if (user != null) populateHeaderView(navBinding);


        //Start the application with default home fragment
        if (savedInstanceState == null) startHomeFragment();

    }


    //Checks for internet
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    //Firing up FireBase Auth UI
    private void fireSignUp() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder()
                            .build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTheme(R.style.AppTheme)
                            .setLogo(R.drawable.news_views_144dp)
                            .build(),
                    RC_SIGN_IN);
        } else {

        }

    }

    //Taking back the Result from FireBase Auth UI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Toast.makeText(
                        this,
                        "Logged in",
                        Toast.LENGTH_SHORT)
                        .show();
                user = FirebaseAuth.getInstance().getCurrentUser();
                populateHeaderView(navBinding);

                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }


    public void populateHeaderView(NavHeaderBinding headerView) {
        if (user != null) {
            try {
                headerView.userNameText.setText(user.getDisplayName());
                headerView.userEmailText.setText(user.getEmail());
                Picasso
                        .get()
                        .load(user.getPhotoUrl())
                        .into(headerView.userImageView);
            } catch (Exception e){
                e.printStackTrace();
            }
        }



    }

    //Starts home fragment as default
    private void startHomeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent, new HomeFragment()).commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    //Though we are using only portrait mode its implemented for safety
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(
                this,
                mainBinding.drawerLayout,
                (Toolbar) mainBinding.toolbar,
                R.string.drawer_open,
                R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navDrawer) {
        navDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;

        switch (item.getItemId()) {
            case R.id.nav_home_fragment:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_about_fragment:
                fragmentClass = AboutFragment.class;
                break;
            case R.id.nav_exit:
                finishAffinity();
                System.exit(0);

            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        //Insert Fragment by replacing any existing one
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        mainBinding.drawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mainBinding.drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        ComponentName componentName =
                new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName));

        return true;
    }

}



