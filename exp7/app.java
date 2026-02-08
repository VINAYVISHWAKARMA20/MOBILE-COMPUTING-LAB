package com.example.mobilecom;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements LocationListener {


   private TextView tvDisplay;
   private LocationManager locationManager;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);


       tvDisplay = findViewById(R.id.tvDisplay);
       Button btnGetLocation = findViewById(R.id.btnGetLocation);
       locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


       btnGetLocation.setOnClickListener(v -> {
           if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                   != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
           } else {
               locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
               tvDisplay.setText("Locating...");
           }
       });
   }


   @Override
   public void onLocationChanged(Location location) {
       try {
           Geocoder geocoder = new Geocoder(this, Locale.getDefault());
           List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


           if (addresses != null && !addresses.isEmpty()) {
               Address adr = addresses.get(0);


               String info = "LATITUDE: " + location.getLatitude() + "\n" +
                       "LONGITUDE: " + location.getLongitude() + "\n\n" +
                       "CITY: " + adr.getLocality() + "\n" +
                       "STATE: " + adr.getAdminArea() + "\n" +
                       "COUNTRY: " + adr.getCountryName();


               tvDisplay.setText(info);
           }
       } catch (Exception e) {
           tvDisplay.setText("Lat: " + location.getLatitude() + "\nLon: " + location.getLongitude() + "\n(Error fetching Address)");
       }
   }


   @Override public void onProviderDisabled(String p) {
       Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show();
   }
   @Override public void onProviderEnabled(String p) {}
   @Override public void onStatusChanged(String p, int s, Bundle b) {}
}
