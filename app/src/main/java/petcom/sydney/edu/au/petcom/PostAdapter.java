package petcom.sydney.edu.au.petcom;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Message;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PostAdapter extends ArrayAdapter<Post> {
    Location location;
    LocationManager locationManager;
    private Post p;
    private StopWatch stopWatch;
    Handler handler;
    TextView timeText;
    LatLng posterLocation;
    LatLng myLocation;
    String[] latlngTemp;
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
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria,true);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted

                location =  locationManager.getLastKnownLocation(provider);
                Log.d("Sam", location.getLatitude()+"");
                myLocation = new LatLng(location.getLatitude(),location.getLongitude());
            } else {

            }
        } else {
            location = locationManager.getLastKnownLocation(provider);
            myLocation = new LatLng(location.getLatitude(),location.getLongitude());
        }



        if(p!=null) {
            Log.i("poiuy", p.getTitle() + " " + p.getHasPicture() + " " + p.getPicture());
            TextView userName = (TextView) convertView.findViewById(R.id.username_post);
            TextView title = (TextView) convertView.findViewById(R.id.title_post);
            TextView body = (TextView) convertView.findViewById(R.id.postbody);
            TextView uName = (TextView) convertView.findViewById(R.id.username_post);
            timeText = (TextView) convertView.findViewById(R.id.time_text);
            TextView distance = (TextView) convertView.findViewById(R.id.distance_text);

            Log.i("yaoxy", p.getEnddate() + "");
            String tempString = p.getLocationString();
            latlngTemp = tempString.split(",");
            posterLocation = new LatLng(Double.parseDouble(latlngTemp[0]),Double.parseDouble(latlngTemp[1]));

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
            distance.setText(calculateDistance(posterLocation,myLocation)*1000 + "m");
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


    private double calculateDistance(LatLng x1, LatLng x2){
        double x1_Lat = x1.latitude*Math.PI / 180;
        double x1_Lng = x1.longitude*Math.PI / 180;
        double x2_Lat = x2.latitude*Math.PI / 180;
        double x2_Lng = x2.longitude*Math.PI / 180;

        double a = x1_Lat - x2_Lat;
        double b = x1_Lng - x2_Lng;

        double cal = 2*Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)+Math.cos(x1_Lat)
                *Math.cos(x2_Lat)*Math.pow(Math.sin(b/2),2)))*6378.137;
        double result = Math.round(cal * 10000d)/10000d;
        return result;
    }


    }




