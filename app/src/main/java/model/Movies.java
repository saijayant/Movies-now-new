package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macbookpro on 01/08/17.
 */

public class Movies implements Parcelable {
    @SerializedName("title")
    String title;
    @SerializedName("releaseDate")
    String releaseDate;
    @SerializedName("moviePosterUrl")
    String moviePosterUrl;
    @SerializedName("ratings")
    String ratings;
    @SerializedName("plotSynopsis")
    String plotSynopsis;
    @SerializedName("id")
    String id;

    public Movies(Parcel in) {
        title=in.readString();
        releaseDate=in.readString();
        moviePosterUrl=in.readString();
        ratings=in.readString();
        plotSynopsis=in.readString();
        id=in.readString();

    }


    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public Movies() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(moviePosterUrl);
        dest.writeString(ratings);
        dest.writeString(plotSynopsis);
        dest.writeString(id);
    }
}
