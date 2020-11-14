package com.natesanchez.knowyourgovernment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.natesanchez.knowyourgovernment.apis.RepsDownloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private RecyclerView recyclerView;
  private OfficialAdapter officialAdapter;
  private final List<Official> officialList = new ArrayList<Official>();
  private String locationString;
  private TextView locationTextView;
  private LocationManager locationManager;
  private Criteria criteria;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerView = findViewById(R.id.recycler_view);
    officialAdapter = new OfficialAdapter(officialList, this);
    recyclerView.setAdapter(officialAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    criteria = new Criteria();
    criteria.setPowerRequirement(Criteria.POWER_HIGH);
    criteria.setAccuracy(Criteria.ACCURACY_FINE);
    criteria.setAltitudeRequired(false);
    criteria.setBearingRequired(false);
    criteria.setSpeedRequired(false);
    locationTextView = findViewById(R.id.main_location);
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 111);
    } else {
      setLocation("");
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public void onClick(View view) {
    Official official = officialList.get(recyclerView.getChildAdapterPosition(view));
    Intent intent = new Intent(getApplicationContext(), OfficialActivity.class);
    intent.putExtra("official", official);
    intent.putExtra("location", locationString);
    startActivity(intent);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_about:
        openAbout();
        return true;
      case R.id.menu_location:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a City, State, or a Zip Code");
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        builder.setView(et);
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
          setLocation(et.getText().toString());
        });
        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
      default:
        return false;
    }
  }

  private boolean checkNetworkConnection() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }

  private void openAbout() {
    Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
    startActivity(intent);
  }

  @SuppressLint("MissingPermission")
  private void setLocation(String s) {
    if (checkNetworkConnection()) {
      officialList.clear();
      if (s.trim().equals("")) {
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location currentLocation = null;
        if (bestProvider != null) {
          currentLocation = locationManager.getLastKnownLocation(bestProvider);
          Geocoder geocoder = new Geocoder(this, Locale.getDefault());
          try {
            List<Address> addresses = geocoder.getFromLocation(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude(),
                    1
            );
            String zip = "";
            for (Address ad : addresses) {
              zip = ad.getPostalCode() == null ? "" : ad.getPostalCode();
            }
            RepsDownloader rd = new RepsDownloader(this, zip);
            new Thread(rd).start();
          } catch (IOException e) {
            noNetwork();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
          }
        }
      } else {
        RepsDownloader rd = new RepsDownloader(this, s);
        new Thread(rd).start();
      }
      officialAdapter.notifyDataSetChanged();
    } else {
      noNetwork();
    }
  }

  public void displayLocation(String loc) {
    locationString = loc;
    locationTextView.setText(loc);
  }

  public void addOfficial(Official official) {
    officialList.add(official);
    officialAdapter.notifyDataSetChanged();
  }

  private void noNetwork() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("No Network Connection");
    builder.setMessage("Data cannot be accessed/loaded without an internet connection.");
    AlertDialog dialog = builder.create();
    dialog.show();
  }
}