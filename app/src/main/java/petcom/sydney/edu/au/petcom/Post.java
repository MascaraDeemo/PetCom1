package petcom.sydney.edu.au.petcom;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {
    private String input;
    private String title;
    private String userName;
    private String picture;
    private boolean hasPicture;

    public Post(){

    }
    public Post(String title,String input, String userName,String picture){
        this.input=input;
        this.title=title;
        this.userName=userName;
        this.picture = picture;
    }

    public Post(String title,String input, String userName){
        this.input=input;
        this.title=title;
        this.userName=userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("input",input);
        result.put("title",title);
        result.put("userName",userName);
        result.put("hasPicture",hasPicture);
        result.put("picture",picture);
        return result;
    }
}
