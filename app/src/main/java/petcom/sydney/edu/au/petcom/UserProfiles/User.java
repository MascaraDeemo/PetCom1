package petcom.sydney.edu.au.petcom.UserProfiles;

import android.support.v4.widget.SwipeRefreshLayout;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class User {

    private String userName;
    private String ProfileUrl;
    private String Uid;

    public User(){}

    public User(String userName, String userProfilePic, String Uid){
        this.userName = userName;
        this.ProfileUrl=userProfilePic;
        this.Uid = Uid;
    }

    public String getUid(){
        return Uid;
    }

    public void setUid(String Uid){
        this.Uid = Uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileUrl(){
        return ProfileUrl;
    }

    public void setProfileUrl(String userProfilePic){
        this.ProfileUrl=userProfilePic;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }


    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("userName",userName);
        result.put("ProfileUrl",ProfileUrl);
        result.put("Uid",Uid);
        return result;
    }

}
