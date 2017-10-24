package com.foodsingh.mindwires.foodsingh;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by PRANSHOO VERMA on 21/10/2017.
 */

public class web_view_class extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
