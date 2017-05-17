package com.divofmod.blackstories;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.io.InputStream;

public class StoryActivity extends AppCompatActivity {

    public static final String IS_SOLVED = "com.divofmod.blackstories.IS_SOLVED";
    public static final String ID = "com.divofmod.blackstories.ID";

    private AdView mAdView;

    private String[] mStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_story);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9614953003503909~2081093477");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("46E7C358212766867467163B485E34DE")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mStory = getIntent().getExtras().getStringArray("story");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(mStory[1]);

        ImageView image = (ImageView) findViewById(R.id.image);
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open(mStory[0] + ".png");
            image.setImageDrawable(Drawable.createFromStream(inputStream, null));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        TextView story = (TextView) findViewById(R.id.story);
        story.setText(mStory[2]);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(StoryActivity.this).setCancelable(true)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle(mStory[1])
                        .setMessage(mStory[3])
                        .setPositiveButton(R.string.close, null)
                        .show();
            }
        });

        CheckBox solved = (CheckBox) findViewById(R.id.solved);
        solved.setChecked(getIntent().getExtras().getBoolean("is_solved"));
        solved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                Intent intent = new Intent();
                intent.putExtra(IS_SOLVED, checkBox.isChecked());
                intent.putExtra(ID, mStory[0]);
                setResult(RESULT_OK, intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onPause() {
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
    }
}
