package com.example.seriestracker;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.seriestracker.imdbapi.IMDBAPI;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle(getTitle() + " - Search");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void SearchContentClick(View view) {
        InputMethodManager mImMan = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mImMan.hideSoftInputFromWindow(view.getWindowToken(), 0);

        String searchContentText = ((TextView)findViewById(R.id.search_content_text)).getText().toString();

        if (!TextUtils.isEmpty(searchContentText) && searchContentText.length() >= 3){
            IMDBAPI.searchAll(this, searchContentText);
        }
    }
}