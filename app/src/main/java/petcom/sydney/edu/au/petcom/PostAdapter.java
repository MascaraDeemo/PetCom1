package petcom.sydney.edu.au.petcom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<Post> {

    public PostAdapter(@NonNull Context context, int resource, ArrayList<Post> objects){
        super(context,resource,objects);
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_layout,null);
        }
        Post p = getItem(position);
        if(p!=null){
            TextView userName = (TextView)convertView.findViewById(R.id.username_post);
            TextView title = (TextView)convertView.findViewById(R.id.title);
            TextView body = (TextView)convertView.findViewById(R.id.postbody);
            TextView comment =(TextView)convertView.findViewById(R.id.comment);
            TextView like = (TextView)convertView.findViewById(R.id.like);

            userName.setText(p.getUserName());
            title.setText(p.getTitle());
            body.setText(p.getInput());
            if(p.isComment() ==false){
                comment.setText("");
            }else{
                comment.setText(Integer.toString(p.getComment()));
            }
            like.setText(Integer.toString(p.getLike()));
        }
        return convertView;
    }
}
