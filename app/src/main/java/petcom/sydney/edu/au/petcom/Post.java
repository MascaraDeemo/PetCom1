package petcom.sydney.edu.au.petcom;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {
    private String input;
    private String title;
    private String userName;
    private long publishTime;
    private int comment;
    public Post(){

    }
    public Post(String title,String input, String userName){
        this.input=input;
        this.title=title;
        this.userName=userName;
        this.publishTime=System.currentTimeMillis();
        this.comment=0;
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

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime() {
        this.publishTime = System.currentTimeMillis();
    }


    public void setComment(int comment) {
        this.comment=comment;
    }

    public int getComment() {
        return comment;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("input",input);
        result.put("title",title);
        result.put("userName",userName);
//        result.put("publishTime",publishTime);
//        result.put("comment",comment);
        return result;
    }
}
