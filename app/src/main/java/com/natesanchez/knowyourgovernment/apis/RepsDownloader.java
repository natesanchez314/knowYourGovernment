package com.natesanchez.knowyourgovernment.apis;

import android.net.Uri;

import com.natesanchez.knowyourgovernment.MainActivity;
import com.natesanchez.knowyourgovernment.Official;
import com.natesanchez.knowyourgovernment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RepsDownloader implements Runnable {

  private MainActivity mainActivity;
  public static HashMap<String, String> repsMap = new HashMap<>();
  final String REPS_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyC797zVheg8DufB8kiI7KSfyvLbiM4VXZc&address=";
  private String location;

  public RepsDownloader(MainActivity mainActivity, String loc) {
    this.mainActivity = mainActivity;
    this.location = loc;
  }

  @Override
  public void run() {
    Uri repsURI = Uri.parse(REPS_URL.concat(location));
    String urlToUse = repsURI.toString();
    StringBuilder sb = new StringBuilder();
    try {
      URL url = new URL(urlToUse);
      HttpsURLConnection uCon = (HttpsURLConnection) url.openConnection();
      uCon.setRequestMethod("GET");
      uCon.connect();
      if (uCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
        int x = uCon.getResponseCode();
        return;
      }
      InputStream is = uCon.getInputStream();
      BufferedReader br = new BufferedReader((new InputStreamReader(is)));
      String line;
      while((line = br.readLine()) != null) {
        sb.append(line).append('\n');
      }
    } catch (Exception e) {
      return;
    }
    process(sb.toString());
  }

  private void process(String s) {
    try {
      JSONObject jObjMain = new JSONObject(s);
      JSONObject normalizedInput = jObjMain.getJSONObject("normalizedInput");
      JSONArray offices = jObjMain.getJSONArray("offices");
      JSONArray officials = jObjMain.getJSONArray("officials");
      for (int i = 0; i < offices.length(); i++) {
        JSONObject office = offices.getJSONObject(i);
        JSONArray officialIndices = office.getJSONArray("officialIndices");
        for (int j = 0; j < officialIndices.length(); j++) {
          int index = Integer.parseInt(officialIndices.get(j).toString());
          JSONObject official = officials.getJSONObject(index);
          Official newOfficial = new Official(
                  official.getString("name"),
                  office.getString("name"),
                  official.getString("party")
          );
          mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              mainActivity.addOfficial(newOfficial);
            }
          });
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
