package com.example.loginscreen.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.loginscreen.R;
import com.example.loginscreen.fragment.DashBoardFragment;
import com.example.loginscreen.fragment.HelpFragment;
import com.example.loginscreen.fragment.MedicationFragment;
import com.example.loginscreen.fragment.SettingsFragment;

public class HomeActivity extends BaseActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private Handler mHandler;

    public static int navItemIndex = 0;

    private static final String TAG_DASH_BOARD = "Dashboard";
    private static final String TAG_MEDICATIONS = "Medications";
    private static final String TAG_SETTINGS = "Settings";
    private static final String TAG_HELPS = "Help";
    private static final String TAG_RATE_US = "Rate Us";
    private static final String TAG_FEEDBACK = "Send Feedback";
    private static final String TAG_SHARE = "Share";
    public static String CURRENT_TAG = TAG_DASH_BOARD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Pill Reminder");
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        setUpNavigationView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NewMedicine.class);
                startActivityForResult(intent, 200);
            }
        });

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASH_BOARD;
            loadFragment();
        }
    }



    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // checking if user is on other navigation menu
        // rather than home
        if (navItemIndex != 0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASH_BOARD;
            loadFragment();
            return;
        }

        super.onBackPressed();
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.drawerDashboard: {
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_DASH_BOARD;
                        toolbar.setTitle("Pill Reminder");
                        break;
                    }
                    case R.id.drawerMedications: {
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MEDICATIONS;
                        toolbar.setTitle(CURRENT_TAG);
                        break;
                    }
//                    case R.id.drawerSettings: {
//                        navItemIndex = 2;
//                        CURRENT_TAG = TAG_SETTINGS;
//                        toolbar.setTitle(CURRENT_TAG);
//                        break;
//                    }
//                    case R.id.drawerHelp: {
//                        navItemIndex = 3;
//                        CURRENT_TAG = TAG_HELPS;
//                        toolbar.setTitle(CURRENT_TAG);
//                        break;
//                    }
//                    case R.id.drawerRateApp: {
//                        navItemIndex = 4;
//                        CURRENT_TAG = TAG_RATE_US;
//                        toolbar.setTitle("MySafeGuard");
//                        openPlayStore();
//                        drawer.closeDrawers();
//                        return true;
//                    }
//                    case R.id.drawerSendFeedback: {
//                        navItemIndex = 5;
//                        CURRENT_TAG = TAG_FEEDBACK;
//                        toolbar.setTitle("MySafeGuard");
//                        feedBack();
//                        drawer.closeDrawers();
//                        return true;
//                    }
//                    case R.id.drawerShareApp: {
//                        navItemIndex = 6;
//                        CURRENT_TAG = TAG_SHARE;
//                        toolbar.setTitle("MySafeGuard");
//                        openShare();
//                        drawer.closeDrawers();
//                        return true;
//                    }
                    default: {
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_DASH_BOARD;
                        toolbar.setTitle("");
                    }
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                };

        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }



    private void loadFragment() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);

        /*if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            if(resultCode == 201){
                drawer.closeDrawers();
                toggleFab();
                return;
            }
        }*/

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        mHandler.post(mPendingRunnable);
        toggleFab();
        drawer.closeDrawers();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == RESULT_OK){
                loadFragment();
            }
        }
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0 || navItemIndex == 1) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    private Fragment getFragment() {
        switch (navItemIndex) {
            case 0: {
                // Dashboard Fragment
                return new DashBoardFragment();
            }
            case 1: {
                // Medication Fragment
                return new MedicationFragment();
            }
            case 2: {
                // Setting fragment
                return new SettingsFragment();
            }
            case 3: {
                // Help fragment
                return new HelpFragment();
            }
            default: {
                return new DashBoardFragment();
            }
        }
    }

    private void feedBack() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.feedback_form_url)));
        startActivity(intent);
    }

    private void openPlayStore() {
        Uri uri = Uri.parse("market://details?id=com.uwin.mysafeguard");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "http://play.google.com/store/apps/details?id=com.uwin.mysafeguard")));
        }
    }

    private void openShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.medication_share_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.medication_share_content));
        startActivity(Intent.createChooser(intent, getString(R.string.medication_share_chooser)));
    }
}