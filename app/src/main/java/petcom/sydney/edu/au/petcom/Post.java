package petcom.sydney.edu.au.petcom;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import petcom.sydney.edu.au.petcom.UserProfiles.User;

public class Post {
    private String input;
    private String title;
    private String picture;
    private boolean hasPicture;
    private User user;

    public Post(){

    }
    public Post(String title,String input, String picture,User user){
        this.input=input;
        this.title=title;
        this.picture = picture;
        this.user = user;
    }

    public Post(String title,String input, User user){
        this.input=input;
        this.title=title;
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

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("input",input);
        result.put("title",title);
        result.put("hasPicture",hasPicture);
        result.put("picture",picture);
        return result;
    }
}
