package com.tcssol.newzz.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
@Entity(tableName = "saved_articles")
public class News implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @SerializedName("title")
    private String title;

    @SerializedName("published_date")
    private String publishedDate;

    @SerializedName("link")
    private String link;

    @SerializedName("clean_url")
    private String cleanUrl;

    @SerializedName("summary")
    private String summary;

    @SerializedName("media")
    private String media;

    public News(String title, String publishedDate, String link, String cleanUrl, String summary, String media) {
        this.title = title;
        this.publishedDate = publishedDate;
        this.link = link;
        this.cleanUrl = cleanUrl;
        this.summary = summary;
        this.media = media;
    }
    @Ignore
    protected News(Parcel parcel){
        title =parcel.readString();
        publishedDate = parcel.readString();
        link = parcel.readString();
        cleanUrl = parcel.readString();
        summary = parcel.readString();
        media = parcel.readString();

    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(publishedDate);
        dest.writeString(link);
        dest.writeString(cleanUrl);
        dest.writeString(summary);
        dest.writeString(media);

    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getLink() {
        return link;
    }

    public String getCleanUrl() {
        return cleanUrl;
    }

    public String getSummary() {
        return summary;
    }

    public String getMedia() {
        return media;
    }

}
