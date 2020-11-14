package com.natesanchez.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.List;

public class OfficialActivity extends AppCompatActivity {

  private Official official;
  private boolean imageClickable;
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
  private ImageView facebookImageView;
  private String facebook;
  private ImageView twitterImageView;
  private String twitter;
  private ImageView youtubeImageView;
  private String youtube;
  private String partyLink;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_official);

    imageClickable = false;
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
    facebookImageView = findViewById(R.id.official_facebook);
    twitterImageView = findViewById(R.id.official_twitter);
    youtubeImageView = findViewById(R.id.official_youtube);

    Intent intent = getIntent();
    if (intent.hasExtra("location")) {
      String loc = (String) intent.getSerializableExtra("location");
      location.setText(loc);
    }
    if (intent.hasExtra("official")) {
      official = (Official) intent.getSerializableExtra("official");
      seat.setText(official.getSeat());
      name.setText(official.getName());
      party.setText(String.format("(%s)",official.getParty()));
      getImage(official.getImageUrl());
      if (!official.getAddress().isEmpty()) {
        List<String> addresses = official.getAddress();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < addresses.size(); i++) {
          sb.append(addresses.get(i)).append("\n");
        }
        address.setText(sb.toString());
      } else {
        address.setVisibility(View.GONE);
        findViewById(R.id.address).setVisibility(View.GONE);
      }
      if (!official.getNumber().isEmpty()) {
        List<String> numbers = official.getNumber();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numbers.size(); i++) {
          sb.append(numbers.get(i)).append("\n");
        }
        phone.setText(sb.toString());
      } else {
        phone.setVisibility(View.GONE);
        findViewById(R.id.phone).setVisibility(View.GONE);
      }
      if (!official.getEmail().isEmpty()) {
        List<String> emails = official.getEmail();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < emails.size(); i++) {
          sb.append(emails.get(i)).append("\n");
        }
        email.setText(sb.toString());
      } else {
        email.setVisibility(View.GONE);
        findViewById(R.id.email).setVisibility(View.GONE);
      }
      if (!official.getWebsite().isEmpty()) {
        List<String> websites = official.getWebsite();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < websites.size(); i++) {
          sb.append(websites.get(i)).append("\n");
        }
        website.setText(sb.toString());
      } else {
        website.setVisibility(View.GONE);
        findViewById(R.id.website).setVisibility(View.GONE);
      }
      if (official.getParty().equals("Democratic Party") || official.getParty().equals("Democratic")  ) {
        partyIcon.setImageResource(R.drawable.dem_logo);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.blue_democrat));
        partyLink = "https://democrats.org/";
      } else if (official.getParty().equals("Republican Party")) {
        partyIcon.setImageResource(R.drawable.rep_logo);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.red_republican));
        partyLink = "https://gop.com/";
      } else {
        partyIcon.setVisibility(View.GONE);
      }
      if (!official.getFacebook().equals("")) {
        facebook = official.getFacebook();
      } else {
        facebookImageView.setVisibility(View.GONE);
      }
      if (!official.getTwitter().equals("")) {
        twitter = official.getTwitter();
      } else {
        twitterImageView.setVisibility(View.GONE);
      }
      if (!official.getYoutube().equals("")) {
        youtube = official.getYoutube();
      } else {
        youtubeImageView.setVisibility(View.GONE);
      }
    }
  }

  public void clickImage(View v) {
    if (imageClickable) {
      Intent intent = new Intent(getApplicationContext(), PhotoDetailActivity.class);
      intent.putExtra("location", location.getText());
      intent.putExtra("seat", official.getSeat());
      intent.putExtra("name", official.getName());
      intent.putExtra("imageURL", official.getImageUrl());
      intent.putExtra("party", official.getParty());
      startActivity(intent);
    }
  }

  public void clickIcon(View v) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(partyLink));
    startActivity(intent);
  }

  public void clickAddress(View v) {
    Uri geoLocation = Uri.parse(String.format("geo:0,0?q=%s", official.getAddress()));
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(geoLocation);
    startActivity(intent);
  }

  public void clickPhone(View v) {
    Intent intent = new Intent(Intent.ACTION_DIAL);
    intent.setData(Uri.parse("tel:" + phone.getText()));
    startActivity(intent);
  }

  public void clickEmail(View v) {
    Intent intent = new Intent(Intent.ACTION_SENDTO);
    intent.setData(Uri.parse("mailto:" + official.getEmail()));
    //intent.putExtra(Intent.EXTRA_EMAIL, email.getText());
    startActivity(intent);
  }

  public void clickWebsite(View v) {
    String url = website.getText().toString();
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    startActivity(intent);
  }

  public void clickFacebook(View v) {
    String FACEBOOK_URL = "https://www.facebook.com/" + facebook;
    String urlToUse;
    PackageManager packageManager = getPackageManager();
    try {
      int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
      if (versionCode >= 3002850) {
        urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
      } else {
        urlToUse = "fb://page/" + facebook;
      }
    } catch (PackageManager.NameNotFoundException e) {
      urlToUse = FACEBOOK_URL;
    }
    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
    facebookIntent.setData(Uri.parse(urlToUse));
    startActivity(facebookIntent);
  }

  public void clickTwitter(View v) {
    Intent intent = null;
    try {
      getPackageManager().getPackageInfo("com.twitter.android", 0);
      intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter));
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    } catch (Exception e) {
      intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + twitter));
    }
    startActivity(intent);
  }

  public void clickYoutube(View v) {
    Intent intent = null;
    try {
      intent = new Intent(Intent.ACTION_VIEW);
      intent.setPackage("com.google.android.youtube");
      intent.setData(Uri.parse("https://www.youtube.com/" + youtube));
      startActivity(intent);
    } catch (ActivityNotFoundException e) {
      intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + youtube));
    }
    startActivity(intent);
  }

  private boolean checkNetworkConnection() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }

  private void getImage(final String imageURL) {
    if (checkNetworkConnection()) {
      Picasso.get().load(imageURL)
              .error(R.drawable.missing)
              .placeholder(R.drawable.placeholder)
              .into(photo,
                      new Callback() {
                        @Override
                        public void onSuccess() {
                          ((BitmapDrawable) photo.getDrawable()).getBitmap().getByteCount();
                          imageClickable = true;
                        }

                        @Override
                        public void onError(Exception e) {
                          Log.d("OfficialActivity", "onError: " + e.getMessage());
                        }
                      });
    } else {
      photo.setImageResource(R.drawable.brokenimage);
    }
  }
}
