package petcom.sydney.edu.au.petcom;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;

import com.google.firebase.database.Exclude;

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
    private Comment comment;
    private Location location;

    public Post(){

    }
    public Post(String title,String input, String picture,User user, Location location){
        this.input=input;
        this.title=title;
        this.picture = picture;
        this.user = user;
        this.location = location;
    }

    public Post(String title,String input, User user, Location location){
        this.input=input;
        this.title=title;
        this.user = user;
        this.location = location;
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
    public Comment getComment(){
        return comment;
    }
    public void setComment(Comment comment){
        this.comment = comment;
    }

    public Location getLocation(){
        return location;
    }
    public void setLocation(Location location){
        this.location = location;
    }


    public String getLocationString(){
        return location.getLatitude() + "," + location.getLongitude();
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("user",user);
        result.put("postID",postID);
        result.put("title",title);
        result.put("input",input);
        result.put("hasPicture",hasPicture);
        result.put("location", location.getLatitude() +","+ location.getLongitude());
        if(hasPicture == true) {
            result.put("picture", picture);
        }else if(hasPicture == false){
            result.put("picture",null);
        }
        result.put("comment",null);
        return result;
    }
}
