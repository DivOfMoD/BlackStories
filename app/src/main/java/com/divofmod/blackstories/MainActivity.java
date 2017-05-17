package com.divofmod.blackstories;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.divofmod.blackstories.helper.LocaleHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String APP_PREFERENCES = "blacks_stories";

    private AlertDialog.Builder mAlertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        selectMenuItem(R.id.nav_unsolved);
        if (sharedPreferences.getString("first_run", "true").equals("true")) {
            sharedPreferences.edit().putString("first_run", "false").apply();
            startActivity(new Intent(this, TutorialActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            openQuitDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_lang) {
            String[] entriesLang = getResources().getStringArray(R.array.entries_lang);
            final String[] entryValuesLang = getResources().getStringArray(R.array.entry_values_lang);

            mAlertDialogBuilder = new AlertDialog.Builder(this);
            mAlertDialogBuilder.setTitle(R.string.lang_dialog_title)
                    .setCancelable(true)
                    .setItems(entriesLang, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            LocaleHelper.setLocale(MainActivity.this, entryValuesLang[item]);
                            finish();
                            startActivity(new Intent(MainActivity.this, MainActivity.this.getClass()));
                        }
                    })
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        selectMenuItem(item.getItemId());
        return true;
    }

    private void selectMenuItem(int itemId) {
        Fragment fragment = null;
        Class fragmentClass;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemId) {
            case (R.id.nav_unsolved):
                fragmentClass = UnsolvedFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fragmentManager.beginTransaction().replace(R.id.container, fragment, "unsolved").commit();

                getSupportActionBar().setSubtitle(R.string.unsolved);
                break;
            case (R.id.nav_solved):
                fragmentClass = SolvedFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fragmentManager.beginTransaction().replace(R.id.container, fragment, "solved").commit();

                getSupportActionBar().setSubtitle(R.string.solved);
                break;
            case (R.id.nav_buy_full_version):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=com.divofmod.blackstories_full")));
                break;
            case (R.id.nav_choose):
                startActivity(new Intent(this, ChooseActivity.class));
                break;
            case (R.id.nav_tutorial):
                startActivity(new Intent(this, TutorialActivity.class));
                break;
            case (R.id.nav_send_your_story):
                startActivity(Intent.createChooser(new Intent(Intent.ACTION_SENDTO)
                        .setData(Uri.parse("mailto:divofmod@gmail.com" +
                                "?subject=" + Uri.encode("New Black Story") +
                                "&body=" + Uri.encode("Title:\n" +
                                "\n" +
                                "Story:\n" +
                                "\n" +
                                "Answer:\n" +
                                "\n" +
                                "Your name:\n" +
                                "\n"))), getString(R.string.send_email)));
                break;
            case (R.id.nav_rate_app):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=com.divofmod.blackstories")));
                break;
            case (R.id.nav_about):
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
    }

    private void openQuitDialog() {
        mAlertDialogBuilder = new AlertDialog.Builder(this);
        mAlertDialogBuilder.setCancelable(true)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.exiting_the_application)
                .setMessage(R.string.quit_the_application)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}
