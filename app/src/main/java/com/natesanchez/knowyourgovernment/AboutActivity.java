package com.natesanchez.knowyourgovernment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AboutActivity extends AppCompatActivity {

  private TextView linkTextView;
  private String link = "https://developers.google.com/civic-information/\n";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);

    linkTextView = findViewById(R.id.google_civics_link);
  }

  public void openGoogleCivics(View v) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(link));
    startActivity(intent);
  }
}
