package com.example.gpsdemo;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.example.gpsdemo.util.LocationUtils;
import com.example.gpsdemo.util.TextViewUtil;

public class MainActivity extends Activity {
    private Button gpsStart;
    private Button gpsStop;
    private Button networkStart;
    private Button networkStop;
    private Button passiveStart;
    private Button passiveStop;
    private Button clearLog;

    private TextView textView;
    // Get the longitude and latitude when GPS changed
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            if (location != null)
                TextViewUtil.infoBlueTextView(textView, "\t" + "Latitude:" + location.getLatitude() + "\nLongitude:" + location.getLongitude() + "\n");
            else
                TextViewUtil.infoRedTextView(textView, "\t" + "data not found." + "\n");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }
    };
    private Location mLocation;
    private Context mContext;
    private LocationManager mLocationManager;
    public Button.OnClickListener btnClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            Button btn = (Button) v;
            if (btn.getId() == R.id.gpsStart) {
                if (LocationUtils.isGpsEnabled(mContext)) {
                    TextViewUtil.infoBlueTextView(textView, "\t" + "GPS Mode success!" + "\n");
                    mLocation = getLocation(LocationManager.GPS_PROVIDER);
                    if (mLocation != null)
                        TextViewUtil.infoBlueTextView(textView, "\t" + "GPS Location: \n" + "Latitude:" + mLocation.getLatitude() + "\nLongitude:" + mLocation.getLongitude() + "\n");
                    else
                        TextViewUtil.infoRedTextView(textView, "\t" + "GPS Location request received, but data not found." + "\n");
                } else {
                    TextViewUtil.infoRedTextView(textView, "\t" + "GPS Mode fail" + "\n");
                }

            } else if (btn.getId() == R.id.gpsStop || btn.getId() == R.id.networkStop || btn.getId() == R.id.passiveStop) {
                if (locationListener != null) {
                    mLocationManager.removeUpdates(locationListener);
                }
                TextViewUtil.infoBlueTextView(textView, "\t" + btn.getText() + "\n");
            } else if (btn.getId() == R.id.networkStart) {
                if (LocationUtils.isNetworkAvailable(mContext) && LocationUtils.isNetworkEnabled(mContext)) {
                    TextViewUtil.infoBlueTextView(textView, "\t" + "Network Mode success!" + "\n");
                    mLocation = getLocation(LocationManager.NETWORK_PROVIDER);
                    if (mLocation != null)
                        TextViewUtil.infoBlueTextView(textView, "\t" + "Network Location: \n" + "Latitude:" + mLocation.getLatitude() + "\nLongitude:" + mLocation.getLongitude() + "\n");
                    else
                        TextViewUtil.infoRedTextView(textView, "\t" + "Network Location request received, but data not found." + "\n");
                } else {
                    TextViewUtil.infoRedTextView(textView, "\t" + "Network Mode fail" + "\n");
                }
            } else if (btn.getId() == R.id.passiveStart) {
                if (LocationUtils.isPassiveEnabled(mContext)) {
                    TextViewUtil.infoBlueTextView(textView, "\t" + "Passive Mode success!" + "\n");
                    mLocation = getLocation(LocationManager.PASSIVE_PROVIDER);
                    if (mLocation != null)
                        TextViewUtil.infoBlueTextView(textView, "\t" + "Passive Location: \n" + "Latitude:" + mLocation.getLatitude() + "\nLongitude:" + mLocation.getLongitude() + "\n");
                    else
                        TextViewUtil.infoRedTextView(textView, "\t" + "Passive Location request received, but data not found." + "\n");
                } else {
                    TextViewUtil.infoRedTextView(textView, "\t" + "Passive Mode fail" + "\n");
                }

            } else if (btn.getId() == R.id.settings) {
                textView.setText("");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);


        gpsStart = (Button) findViewById(R.id.gpsStart);
        gpsStop = (Button) findViewById(R.id.gpsStop);
        networkStart = (Button) findViewById(R.id.networkStart);
        passiveStart = (Button) findViewById(R.id.passiveStart);
        networkStop = (Button) findViewById(R.id.networkStop);
        passiveStop = (Button) findViewById(R.id.passiveStop);
        clearLog = (Button) findViewById(R.id.settings);

        textView = (TextView) findViewById(R.id.text);
        gpsStart.setOnClickListener(btnClickListener);
        gpsStop.setOnClickListener(btnClickListener);
        networkStart.setOnClickListener(btnClickListener);
        passiveStart.setOnClickListener(btnClickListener);
        networkStop.setOnClickListener(btnClickListener);
        passiveStop.setOnClickListener(btnClickListener);
        clearLog.setOnClickListener(btnClickListener);
        mContext = this;
        // if (savedInstanceState == null) {
        // getFragmentManager().beginTransaction()
        // .add(R.id.container, new PlaceholderFragment())
        // .commit();
        // }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return true;
        }
        if (id == R.id.wlan_settings) {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Location getLocation(String mode) {
        // Get the location manager
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(mode)) {
            textView.setText("\t" + mode + " not open." + "\n");
            return null;
        } else {
            textView.setText("\t" + mode + " opened!" + "\n");
            // Find related services
            Criteria criteria = new Criteria();

            // A finer location accuracy requirement
            criteria.setAccuracy(Criteria.ACCURACY_FINE);

            // Not provide altitude information
            criteria.setAltitudeRequired(false);

            // Not provide bearing information
            criteria.setBearingRequired(false);

            // Provider is allowed to incur monetary cost
            criteria.setCostAllowed(true);

            // Indicates the desired maximum power level
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            // Returns the name of the provider that best meets the given criteria
            String provider = mLocationManager.getBestProvider(criteria, true);

            // Returns a Location indicating the data from the last known location
            // fix obtained from the given provider.
            TextViewUtil.infoBlueTextView(textView, "\t" + "Finished init.\n");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {   // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                mLocationManager.requestLocationUpdates(mode, 1000, 5, locationListener);
                Location location = mLocationManager.getLastKnownLocation(mode);
                TextViewUtil.infoBlueTextView(textView, "\t" + "Finished request location.\n");
                return location;
            } else {
                TextViewUtil.infoRedTextView(textView, "\t" + "Need permission: ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION.\n");
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
