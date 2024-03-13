package com.tcssol.newzz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tcssol.newzz.R;
import com.tcssol.newzz.databinding.ActivityBrowseArticleBinding;

public class BrowseArticle extends AppCompatActivity {
    private ActivityBrowseArticleBinding binding;
    WebView webView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse_article);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding=ActivityBrowseArticleBinding.inflate(getLayoutInflater());
        webView=binding.webView;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
//        webView.loadUrl("https://www.google.com");

        Intent intent=getIntent();
        if(intent!=null){
            String data=intent.getStringExtra("Url");
            webView.loadUrl(data);
        }
    }
}