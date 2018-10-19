package petcom.sydney.edu.au.petcom;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import petcom.sydney.edu.au.petcom.UserProfiles.User;

public class Post {
    private String postID;
    private String input;
    private String title;
    private String picture;
    private boolean hasPicture;
    private User user;
    private String locationString;
    private long duration;

    public Post(){
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public boolean getHasPicture(){
        return this.hasPicture;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(User user){
        this.user=user;
    }
    public User getUser(){
        return user;
    }

    public void setDuration(long duration){
        this.duration=duration;
    }
    public long getDuration(){
        return duration;
    }

    public String getLocationString(){
        return locationString;
    }

    public void setLocationString(Location location){
        this.locationString = location.getLatitude()+","+location.getLongitude();
    }

    public void setLocationByString(String location){
        this.locationString = location;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("user",user);
        result.put("postID",postID);
        result.put("title",title);
        result.put("input",input);
       result.put("duration",duration);
        result.put("hasPicture",hasPicture);
        result.put("location", locationString);
        result.put("picture",picture);
        return result;
    }
}
