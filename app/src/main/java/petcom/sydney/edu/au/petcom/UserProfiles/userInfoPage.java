package petcom.sydney.edu.au.petcom.UserProfiles;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class userInfoPage {
    String title;
    String idKey;
    long responder;
    public userInfoPage(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public long getResponder(){
        return this.responder;
    }

    public void setResponder(long responder){
        this.responder=responder;
    }
}
