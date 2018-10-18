package petcom.sydney.edu.au.petcom;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import petcom.sydney.edu.au.petcom.Post;
import petcom.sydney.edu.au.petcom.UserProfiles.User;

public class Comment {
    private String reply;
    private User user;


    public Comment(){

    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment(String reply, String postID, User user){
        this.reply = reply;
        this.user = user;
    }
    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("reply",reply);
        result.put("user",user);
        return result;
    }

}
