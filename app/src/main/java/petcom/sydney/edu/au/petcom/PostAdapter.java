package petcom.sydney.edu.au.petcom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
            Log.i("poiuy",p.getTitle()+" "+p.getHasPicture()+" "+p.getPicture());
            TextView userName = (TextView)convertView.findViewById(R.id.username_post);
            TextView title = (TextView)convertView.findViewById(R.id.title_post);
            TextView body = (TextView)convertView.findViewById(R.id.postbody);
            if(p.getHasPicture() == true) {
                ImageView picView = (ImageView)convertView.findViewById(R.id.moments_pic);
                Picasso.with(getContext()).load(p.getPicture()).into(picView);
                picView.setVisibility(View.VISIBLE);
            }else if(p.getHasPicture() == false){
                ImageView picView = (ImageView)convertView.findViewById(R.id.moments_pic);
                picView.setVisibility(View.GONE);
            }
//            userName.setText(p.getUserName());
            title.setText(p.getTitle());
            body.setText(p.getInput());
            LinearLayout singlePost = (LinearLayout)convertView.findViewById(R.id.single_post);
            singlePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), replyPage.class);
                    getContext().startActivity(intent);
                }
            });
        }
        return convertView;
    }
}
