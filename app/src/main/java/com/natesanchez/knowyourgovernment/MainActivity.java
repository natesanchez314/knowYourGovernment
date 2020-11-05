package com.natesanchez.knowyourgovernment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private RecyclerView recyclerView;
  private OfficialAdapter officialAdapter;
  private final ArrayList<Official> officialList = new ArrayList<Official>();
  private String locationString;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    officialList.add(new Official("test1", "test1", "test1"));
    officialList.add(new Official("test2", "test2", "test2"));
    officialList.add(new Official("test3", "test3", "test3"));
    officialList.add(new Official("test4", "test4", "test4"));
    officialList.add(new Official("test5", "test5", "test5"));
    recyclerView = findViewById(R.id.recycler_view);
    officialAdapter = new OfficialAdapter(officialList, this);
    recyclerView.setAdapter(officialAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public void onClick(View view) {
    Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Enter a City, State, or a Zip Code");
    final EditText et = new EditText(this);
    et.setInputType(InputType.TYPE_CLASS_TEXT);
    et.setGravity(Gravity.CENTER_HORIZONTAL);
    et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
    builder.setView(et);
    builder.setPositiveButton("OK", (dialogInterface, i) -> {
      updateLocation();
    });
    builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {

    });
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_about:
        openAbout();
        return true;
      case R.id.menu_location:
        Toast.makeText(this, "location", Toast.LENGTH_SHORT).show();
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

  private void updateLocation() {

  }
}