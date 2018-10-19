package petcom.sydney.edu.au.petcom;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PostAdapter extends ArrayAdapter<Post> {
    Location location;
    LocationManager locationManager;
    private Post p;

    Handler handler;
    TextView timeText;
    LatLng posterLocation;
    LatLng myLocation;
    String tempString;
    String[] latlngTemp;
    private List<ViewHolder> lstHolder;
    private LayoutInflater lf;
    private Handler mHandler = new Handler();
    private Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolder) {
                long nowTime = System.currentTimeMillis();
                for (ViewHolder holder : lstHolder) {
                    holder.updateTimeRemaining(nowTime);
                }
            }
        }
    };

    public PostAdapter(@NonNull Context context, int resource, ArrayList<Post> objects) {
        super(context, resource, objects);
        lf = LayoutInflater.from(context);
        lstHolder = new ArrayList<>();
        startUpdateTimer();
    }

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateTime);
            }
        }, 1000, 1000);
    }

    private class ViewHolder {
        TextView timeText;
        TextView title;
        TextView body;
        TextView uName;
        TextView distance;
        ImageView picView;
        ImageView userAvatar;
        Post p;

        public void setPostView(Post post) {
            p = post;
            title.setText(p.getTitle());
            body.setText(p.getInput());
            uName.setText(p.getUser().getUserName());
            updateTimeRemaining(System.currentTimeMillis());

            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria,true);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted

                    location =  locationManager.getLastKnownLocation(provider);
//                    Log.d("Sam", location.getLatitude()+"");
                    myLocation = new LatLng(location.getLatitude(),location.getLongitude());
                } else {

                }
            } else {
                location = locationManager.getLastKnownLocation(provider);
                myLocation = new LatLng(location.getLatitude(),location.getLongitude());
            }
            tempString = p.getLocationString();
            latlngTemp = tempString.split(",");
            posterLocation = new LatLng(Double.parseDouble(latlngTemp[0]),Double.parseDouble(latlngTemp[1]));
            distance.setText(calculateDistance(posterLocation,myLocation) + "m");
            Picasso.with(getContext()).load(p.getUser().getProfileUrl()).into(userAvatar);

            if(p.getHasPicture() == true) {
                Picasso.with(getContext()).load(p.getPicture()).into(picView);
                picView.setVisibility(View.VISIBLE);
            }else if(p.getHasPicture() == false){
                picView.setVisibility(View.GONE);
            }
        }

        public void updateTimeRemaining(long currentTime) {
            long timeDiff = p.getDuration() - currentTime;
            Log.i("sophie",timeDiff+" "+p.getDuration()+"  "+currentTime+" "+new Date(currentTime).toString()+"  "+ new Date(p.getDuration()).toString());
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                timeText.setText(hours + " : " + minutes + " : " + seconds);
            } else {
                timeText.setText("this event has expired!");
            }
        }
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = lf.inflate(R.layout.post_layout, parent, false);
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_layout,null);
            holder.title = (TextView) convertView.findViewById(R.id.title_post);
            holder.body = (TextView) convertView.findViewById(R.id.postbody);
            holder.uName = (TextView) convertView.findViewById(R.id.username_post);
            holder.timeText = (TextView) convertView.findViewById(R.id.time_text);
            holder.distance = (TextView) convertView.findViewById(R.id.distance_text);
            holder.picView = (ImageView)convertView.findViewById(R.id.moments_pic);
            holder.userAvatar = (ImageView)convertView.findViewById(R.id.user_pic_post);
            convertView.setTag(holder);
            synchronized (lstHolder) {
                lstHolder.add(holder);
            }
        }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.setPostView(getItem(position));

        Button mapBtn = (Button)convertView.findViewById(R.id.map_btn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), showMap.class);
                intent.putExtra("location",tempString);
                getContext().startActivity(intent);
            }
        });

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




