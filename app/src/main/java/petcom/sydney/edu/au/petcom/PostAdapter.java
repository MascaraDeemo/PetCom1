package petcom.sydney.edu.au.petcom;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PostAdapter extends ArrayAdapter<Post> {
    private Post p;
    private StopWatch stopWatch;
    Handler handler;
    TextView timeText;
    public PostAdapter(@NonNull Context context, int resource, ArrayList<Post> objects){
        super(context,resource,objects);
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_layout,null);
        }
        p = getItem(position);
        stopWatch = new StopWatch();
        if(p!=null) {
            Log.i("poiuy", p.getTitle() + " " + p.getHasPicture() + " " + p.getPicture());
            TextView userName = (TextView) convertView.findViewById(R.id.username_post);
            TextView title = (TextView) convertView.findViewById(R.id.title_post);
            TextView body = (TextView) convertView.findViewById(R.id.postbody);
            TextView uName = (TextView) convertView.findViewById(R.id.username_post);
            timeText = (TextView) convertView.findViewById(R.id.time_text);

            SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd : hh:mm:ss");
            try {
                Date tempD = ft.parse(p.getEnddate());
                timeText.setText("This event is valid until: "+ft.format(tempD));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            uName.setText(p.getUser().getUserName());
            if(p.getUser().getProfileUrl() != null){
                ImageView userTouXiang = (ImageView)convertView.findViewById(R.id.user_pic_post);
                Picasso.with(getContext()).load(p.getUser().getProfileUrl()).into(userTouXiang);
            }else if(p.getUser().getProfileUrl() == null){
                ImageView userTouXiang = (ImageView)convertView.findViewById(R.id.user_pic_post);
                userTouXiang.setVisibility(View.GONE);
            }

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

                }
            });
            Button commentBtn = (Button)convertView.findViewById(R.id.comment_btn);
            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return convertView;
    }


}
