package com.tcssol.newzz.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tcssol.newzz.Model.News;
import com.tcssol.newzz.R;
import com.tcssol.newzz.databinding.ActivityBrowseArticleBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BrowseArticle extends AppCompatActivity {
    private ActivityBrowseArticleBinding binding;
    private TextView title;
    private TextView by;
    private TextView date;
    private ImageView image;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display
        EdgeToEdge.enable(this);

        // Inflate the layout using view binding
        binding = ActivityBrowseArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Set the content view to the root of the inflated view

        // Set padding to handle system insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ActionBar bar=getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        title = binding.textViewTitle;
        by = binding.textViewBy;
        date = binding.textViewDate;
        image = binding.imageView2;
        content = binding.textViewContent;

        // Get intent and set data
        Intent intent = getIntent();
        if (intent != null) {
            Log.d("Browser", "Received Intent");
            News news = intent.getParcelableExtra("News");
            if (news != null) {
                Log.d("Browser", news.getTitle());
                title.setText(news.getTitle());
                by.setText(news.getCleanUrl());
                LocalDateTime dateTime = LocalDateTime.parse(news.getPublishedDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a dd-MM-yyyy");
                String formattedDateTime = dateTime.format(formatter);
                date.setText(formattedDateTime);
                content.setText(news.getSummary());
                String imagelink = news.getMedia();

                // Load image using Glide
                Glide.with(this)
                        .load(imagelink)
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.image_holder_foreground)
                                .error(R.mipmap.image_not_found_foreground)
                                .centerCrop())
                        .into(image);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle home button click (back button)
        if (item.getItemId() == android.R.id.home) {
            finish(); // Finish the activity when the back button is clicked
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}