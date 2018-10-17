package petcom.sydney.edu.au.petcom;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;


public class User {

    private String userName;



    public User(){}


    public User(String userName){
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }


    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("userName",userName);
        return result;
    }

}
