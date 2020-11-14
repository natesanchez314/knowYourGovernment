package com.natesanchez.knowyourgovernment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class OfficialActivity extends AppCompatActivity {

  private TextView location;
  private TextView seat;
  private TextView name;
  private TextView party;
  private ImageView photo;
  private ImageView partyIcon;
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
    constraintLayout = findViewById(R.id.official_layout);

    Intent intent = getIntent();
    if (intent.hasExtra("official")) {
      Official official = (Official) intent.getSerializableExtra("official");
      location.setText("---");
      seat.setText(official.getSeat());
      name.setText(official.getName());
      party.setText(official.getParty());
      if (official.getParty().equals("Democratic Party")) {
        partyIcon.setImageResource(R.drawable.dem_logo);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.blue_democrat));
      } else if (official.getParty().equals("Republican Party")) {
        partyIcon.setImageResource(R.drawable.rep_logo);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.red_republican));
      }
    }
  }
}
