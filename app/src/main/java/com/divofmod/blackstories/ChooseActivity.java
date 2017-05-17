package com.divofmod.blackstories;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class ChooseActivity extends AppCompatActivity {

    EditText mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.choose);

        final Random random = new Random();

        mEdit = (EditText) findViewById(R.id.edit);
        final TextView text = (TextView) findViewById(R.id.text);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm())
                    return;
                text.setText(String.valueOf(random.nextInt(Integer.parseInt(mEdit.getText().toString())) + 1));
            }
        });

    }

    private boolean validateForm() {
        boolean valid = true;

        String key = mEdit.getText().toString();
        if (TextUtils.isEmpty(key) || Integer.parseInt(key) > 1000000 || Integer.parseInt(key) == 0) {
            mEdit.setError(getString(R.string.enter_number_error));
            mEdit.setText("");
            valid = false;
        } else
            mEdit.setError(null);
        return valid;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}