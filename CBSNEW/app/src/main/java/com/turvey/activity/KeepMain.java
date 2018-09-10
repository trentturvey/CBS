package com.turvey.activity;

import android.graphics.Camera;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.turvey.cbs.R;
import com.turvey.fragment.HomeFragment;
import com.turvey.fragment.LearnFragment;
import com.turvey.fragment.ProfileFragment;
import com.turvey.fragment.ShotClockFragment;

public class KeepMain extends AppCompatActivity {
    static KeepMain keepMain;
    private ActionBar actionBar;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KeepLogin.getInstance().finish();
        keepMain = this;
        setContentView(R.layout.homedrawnavbar);
        initToolbar();
        initNavigationMenu();
        displayFragment(R.id.home);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initNavigationMenu() {
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                int id = item.getItemId();
                displayFragment(id);
                actionBar.setTitle(item.getTitle());
                drawer.closeDrawers();
                return true;
            }
        });
    }

    private void displayFragment(int id) {
        Fragment fragment = null;
        if (id == R.id.home) {
            fragment = new HomeFragment();
        } else if (id == R.id.shotclock) {
            fragment = new ShotClockFragment();
        } else if (id == R.id.record) {
          //  startActivity(new Intent(KeepMain.this, KeepCamera.class));
        } else if (id == R.id.learn) {
            fragment = new LearnFragment();
        } else if (id == R.id.account) {
            fragment = new ProfileFragment();
        } else if (id == R.id.logout) {
            doLogout();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.startupfragment, fragment);
            ft.commit();

        }

    }

    public void doLogout() {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(KeepMain.this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(KeepMain.this, KeepLogin.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        doExitApp();
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "Press again to exit app", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    public static KeepMain getInstance() {
        return keepMain;
    }

}