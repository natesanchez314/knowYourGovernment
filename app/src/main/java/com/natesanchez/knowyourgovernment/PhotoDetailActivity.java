package com.natesanchez.knowyourgovernment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PhotoDetailActivity extends AppCompatActivity {

  private TextView location;
  private TextView seat;
  private TextView name;
  private ImageView photo;
  private ImageView icon;
  private ConstraintLayout cl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photo_detail);

    location = findViewById(R.id.photo_location);
    seat = findViewById(R.id.photo_seat);
    name = findViewById(R.id.photo_name);
    photo = findViewById(R.id.photo_image);
    icon = findViewById(R.id.photo_icon);
    cl = findViewById(R.id.photo_cl);

    Intent intent = getIntent();
    if (intent.hasExtra("location")) {
      String loc = intent.getSerializableExtra("location").toString();
      location.setText(loc);
    }
    if (intent.hasExtra("seat")) {
      String s = intent.getSerializableExtra("seat").toString();
      seat.setText(s);
    }
    if (intent.hasExtra("name")) {
      String n = intent.getSerializableExtra("name").toString();
      name.setText(n);
    }
    if (intent.hasExtra("imageURL")) {
      String i = intent.getSerializableExtra("imageURL").toString();
      getImage(i);
    }
    if (intent.hasExtra("party")) {
      String p = intent.getSerializableExtra("party").toString();
      if (p.equals("Democratic Party") || p.equals("Democratic")) {
        cl.setBackgroundColor(getResources().getColor(R.color.blue_democrat));
        icon.setImageResource(R.drawable.dem_logo);
      } else if (p.equals("Republican Party")) {
        cl.setBackgroundColor(getResources().getColor(R.color.red_republican));
        icon.setImageResource(R.drawable.rep_logo);
      } else {
        icon.setVisibility(View.GONE);
      }
    }
  }

  private void getImage(final String imageURL) {
    Picasso.get().load(imageURL)
            .error(R.drawable.missing)
            .placeholder(R.drawable.placeholder)
            .into(photo,
                    new Callback() {
                      @Override
                      public void onSuccess() {
                        ((BitmapDrawable) photo.getDrawable()).getBitmap().getByteCount();
                      }

                      @Override
                      public void onError(Exception e) {
                        Log.d("OfficialActivity", "onError: " + e.getMessage());
                      }
                    });
  }
}
