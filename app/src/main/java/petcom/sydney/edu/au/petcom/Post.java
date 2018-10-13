package petcom.sydney.edu.au.petcom;

public class Post {
    private String input;
    private String title;
    private String userName;
    private long publishTime;
    private int like;
    private boolean isComment;
    private int comment;

    public Post(String title,String input, String userName,
            int like){
        this.input=input;
        this.title=title;
        this.userName=userName;
        this.publishTime=System.currentTimeMillis();
        this.isComment=false;
        this.like=like;
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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public boolean isComment() {
        return isComment;
    }

    public void setComment(int comment) {
        if(isComment == true){
            this.comment = 0;
        }else{
            this.comment = comment;
        }
    }

    public int getComment() {
        return comment;
    }
}
