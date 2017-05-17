package com.divofmod.blackstories;

import android.app.Application;
import android.content.Context;

import com.divofmod.blackstories.helper.LocaleHelper;

public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, base.getResources().getConfiguration().locale.getCountry().toLowerCase()));
    }
}
