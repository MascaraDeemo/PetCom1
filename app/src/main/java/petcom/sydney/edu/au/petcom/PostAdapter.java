package petcom.sydney.edu.au.petcom;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;


public class PostAdapter extends ArrayAdapter<Post> {
    Location location;
    LocationManager locationManager;
    Handler handler;
    LatLng posterLocation;
    LatLng myLocation;
    String tempString;
    String[] latlngTemp;
    ViewHolder holder;

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
            String provider = locationManager.getBestProvider(criteria, true);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Location Permission already granted

                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    Log.d("Sam", location.getLatitude()+"");
                    myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                } else {

                }
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
            tempString = p.getLocationString();
            latlngTemp = tempString.split(",");
            posterLocation = new LatLng(Double.parseDouble(latlngTemp[0]), Double.parseDouble(latlngTemp[1]));
            double dis = greatCircleInMeters(posterLocation, myLocation);
            String disS = String.format("%.3f", dis);
            distance.setText( disS+ " Km");
            Picasso.with(getContext()).load(p.getUser().getProfileUrl()).into(userAvatar);

            if (p.getHasPicture() == true) {
                Picasso.with(getContext()).load(p.getPicture()).into(picView);
                picView.setVisibility(View.VISIBLE);
            } else if (p.getHasPicture() == false) {
                picView.setVisibility(GONE);
            }
        }

        public void updateTimeRemaining(long currentTime) {
            long timeDiff = p.getDuration() - currentTime;
            Log.i("sophie", timeDiff + " " + p.getDuration() + "  " + currentTime + " " + new Date(currentTime).toString() + "  " + new Date(p.getDuration()).toString());
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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
            holder.picView = (ImageView) convertView.findViewById(R.id.moments_pic);
            holder.userAvatar = (ImageView) convertView.findViewById(R.id.user_pic_post);
            convertView.setTag(holder);

            synchronized (lstHolder) {
                lstHolder.add(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setPostView(getItem(position));

        if(holder.body.getText().equals("")){
            holder.body.setVisibility(GONE);
        }else{
            holder.body.setVisibility(View.VISIBLE);
        }
        Button mapBtn = (Button) convertView.findViewById(R.id.map_btn);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Join")
                        .setMessage("Do you want to join?")
                        .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getContext(), showMap.class);
                                intent.putExtra("location", getItem(position).getLocationString());
                                intent.putExtra("postID", getItem(position).getPostID());
                                getContext().startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });

        return convertView;
    }

    public double greatCircleInMeters(LatLng latLng1, LatLng latLng2) {
        return greatCircleInKilometers(latLng1.latitude, latLng1.longitude, latLng2.latitude,
                latLng2.longitude);
    }


    public double greatCircleInKilometers(double lat1, double long1, double lat2, double long2) {
        double phi1 = lat1 * PI_RAD;
        double phi2 = lat2 * PI_RAD;
        double lam1 = long1 * PI_RAD;
        double lam2 = long2 * PI_RAD;

        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));
    }


        static double PI_RAD = Math.PI / 180.0;


}




