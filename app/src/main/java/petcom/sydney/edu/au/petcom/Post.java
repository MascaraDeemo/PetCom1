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
    private Comment comment;

    private Location location;

    private String locationString;


    private String startdate;
    private String enddate;

    public Post(){
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd : hh:mm:ss");
        this.startdate = ft.format(d);
    }

    public Post(String title,String input, String picture,User user, Location location){
        this.input=input;
        this.title=title;
        this.picture = picture;
        this.user = user;

        this.location = location;

        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd : hh:mm:ss");
        this.startdate = ft.format(d);

    }

    public Post(String title,String input, User user, Location location) {
        this.input = input;
        this.title = title;
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
    public void setStartdate(String date){
        this.startdate = date;
    }
    public String getStartdate(){
        return startdate;
    }
    public void setEnddate(long time) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd : hh:mm:ss");
        Date tempD = ft.parse(startdate);
        long endTime = tempD.getTime()+time;
        Date end = new Date(endTime);
        this.enddate = ft.format(end);
    }

    public void setEnddateInMain(String enddate){
        this.enddate = enddate;
    }
    public String getEnddate(){
        return enddate;
    }

//    public Location getLocation(){
//        return location;
//    }
//    public void setLocation(Location location){
//        this.location = location;
//    }


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
        result.put("start_date",startdate);
        result.put("end_date",enddate);
        result.put("hasPicture",hasPicture);
        result.put("location", locationString);
        if(hasPicture == true) {
            result.put("picture", picture);
        }else if(hasPicture == false){
            result.put("picture",null);
        }
        result.put("comment",null);
        return result;
    }
}
