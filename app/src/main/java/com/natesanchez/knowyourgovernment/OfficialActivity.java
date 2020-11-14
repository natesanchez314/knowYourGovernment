package com.natesanchez.knowyourgovernment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class OfficialActivity extends AppCompatActivity {

  private TextView location;
  private TextView seat;
  private TextView name;
  private TextView party;
  private ImageView photo;
  private ImageView partyIcon;
  private TextView address;
  private TextView phone;
  private TextView email;
  private TextView website;
  private ConstraintLayout constraintLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_official);

    location = findViewById(R.id.official_location);
    seat = findViewById(R.id.official_seat);
    name = findViewById(R.id.official_name);
    party = findViewById(R.id.official_party);
    photo = findViewById(R.id.official_photo);
    partyIcon = findViewById(R.id.official_party_icon);
    address = findViewById(R.id.official_address);
    phone = findViewById(R.id.official_phone);
    email = findViewById(R.id.official_email);
    website = findViewById(R.id.official_website);
    constraintLayout = findViewById(R.id.official_layout);

    Intent intent = getIntent();
    if (getIntent().hasExtra("location")) {
      String loc = (String) intent.getSerializableExtra("location");
      location.setText(loc);
    }
    if (intent.hasExtra("official")) {
      Official official = (Official) intent.getSerializableExtra("official");
      seat.setText(official.getSeat());
      name.setText(official.getName());
      party.setText(official.getParty());
      address.setText(official.getAddress());
      phone.setText(official.getNumber());
      email.setText(official.getEmail());
      website.setText(official.getWebsite());
      if (official.getParty().equals("Democratic Party")) {
        partyIcon.setImageResource(R.drawable.dem_logo);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.blue_democrat));
      } else if (official.getParty().equals("Republican Party")) {
        partyIcon.setImageResource(R.drawable.rep_logo);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.red_republican));
      }
    }
  }

  public void clickImage(View v) {
    Intent intent = new Intent(getApplicationContext(), PhotoDetailActivity.class);
    startActivity(intent);
  }

  public void clickIcon(View v) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    startActivity(intent);
  }

  public void clickAddress(View v) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    startActivity(intent);
  }

  public void clickPhone(View v) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    startActivity(intent);
  }

  public void clickEmail(View v) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    startActivity(intent);
  }

  public void clickWebsite(View v) {
    String url = website.getText().toString();
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    startActivity(intent);
  }
}
