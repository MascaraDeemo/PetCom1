package petcom.sydney.edu.au.petcom.UserProfiles;

import android.support.v4.widget.SwipeRefreshLayout;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class User {

    private String userName;
    private String userProfilePic;
    public User(){}

    public User(String userName, String userProfilePic){
        this.userName = userName;
        this.userProfilePic=userProfilePic;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfilePic(){
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic){
        this.userProfilePic=userProfilePic;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }


    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("userName",userName);
        result.put("userProfilePic",userProfilePic);
        return result;
    }

}
