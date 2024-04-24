package com.tcssol.newzz.Model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tcssol.newzz.R;
import com.tcssol.newzz.databinding.NewsItemBinding;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> allNews;
    private Context context;
    private NewsItemBinding binding;
    private NewItemClickListner clickListner;
    private LocalDateTime current=LocalDateTime.now();
    private int viewType;


    public NewsAdapter(List<News> allNews, Context context,int viewType,NewItemClickListner clickListner) {
        this.allNews = allNews;
        this.context = context;
        this.clickListner=clickListner;
        this.viewType=viewType;
        Log.d("Saved","Initializing Adapter");
//        Log.d("Saved",String.valueOf(allNews.size()));
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        binding= NewsItemBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding,viewType,clickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        News news=allNews.get(position);
        holder.title.setText(news.getTitle());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

// Parse the published date string into a LocalDateTime object using the formatter
        LocalDateTime publishedAt = LocalDateTime.parse(news.getPublishedDate(), formatter);
        Duration duration=Duration.between(publishedAt,current);
        String hours=(int)duration.toHours()+"h";
        if(duration.toHours()>24){
            hours=(int)duration.toDays()+"d";
        }
        holder.date.setText(hours);
        String src=news.getCleanUrl();
        String sub=src.substring(0,src.indexOf('.'));
        holder.source.setText(String.format("%s%s", String.valueOf(src.charAt(0)).toUpperCase(), src.substring(1, sub.length())));

        ImageView imageView=holder.image;


        String imageUrl=news.getMedia();
        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.image_holder_foreground) // Placeholder image
                        .error(R.mipmap.image_not_found_foreground)
                        .centerCrop()// Error image in case of loading failure
                )
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);

    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public int getItemCount() {
        if(allNews!=null)
            return allNews.size();
        else 
            return 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView title;
        private TextView source;
        private TextView date;
        private ImageButton save;
        private ImageButton share;
        private ImageButton delete;
        private NewItemClickListner click;
        public ViewHolder(@NonNull NewsItemBinding binding,int viewType,NewItemClickListner clickListner) {
            super(binding.getRoot());
            image=binding.imageView;
            title=binding.textView;
            source=binding.newsItemSrc;
            date=binding.newsitemDate;
            save = binding.imageButtonSave;
            if(viewType==1) {
                save.setOnClickListener(this);
            }else{
                save.setVisibility(View.GONE);
                delete=binding.imageButtonDelete;
                delete.setVisibility(View.VISIBLE);
                delete.setOnClickListener(this);
            }
            share=binding.imageButtonShare;
            binding.getRoot().setOnClickListener(this);
            share.setOnClickListener(this);
            click=clickListner;
        }

        @Override
        public void onClick(View v) {
            int id=v.getId();
            News news=allNews.get(getAdapterPosition());
            if(id==R.id.imageButtonShare){
                click.shareArticle(news.getLink());
            }else if(id==R.id.imageButtonSave){
                click.saveArticle(news);
            }else if(id==R.id.imageButtonDelete){
                click.deleteArticle(news);
            }
            else{
                click.OpenArticle(news);
            }


        }
    }

}
