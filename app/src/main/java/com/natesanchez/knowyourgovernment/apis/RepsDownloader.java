package com.natesanchez.knowyourgovernment.apis;

import android.net.Uri;
import android.widget.TextView;

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
      String locationString = String.format("%s, %s %s",
              normalizedInput.getString("city"),
              normalizedInput.getString("state"),
              normalizedInput.getString("zip")
      );
      TextView mainLocation = mainActivity.findViewById(R.id.main_location);
      mainLocation.setText(locationString);
      JSONArray offices = jObjMain.getJSONArray("offices");
      JSONArray officials = jObjMain.getJSONArray("officials");
      for (int i = 0; i < offices.length(); i++) {
        JSONObject office = offices.getJSONObject(i);
        JSONArray officialIndices = office.getJSONArray("officialIndices");
        for (int j = 0; j < officialIndices.length(); j++) {
          int index = Integer.parseInt(officialIndices.get(j).toString());
          JSONObject official = officials.getJSONObject(index);

          String name = official.getString("name");
          String seat = office.getString("name");
          String party = official.getString("party");
          String address = "N\\A";
          String phones = "N\\A";
          String email = "N\\A";
          String urls = "N\\A";
          String photosUrl = "N\\A";

          if (official.has("address")){
            StringBuilder sb = new StringBuilder();
            JSONArray addressArray = official.getJSONArray("address");
            JSONObject ad = addressArray.getJSONObject(0);
            sb.append(ad.getString("line1")).append("\n");
            sb.append(ad.getString("city"));
            if (!ad.getString("state").equals("DC")) {
              sb.append("\n");
            } else {
              sb.append(" ");
            }
            sb.append(ad.getString("state")).append("\n");
            sb.append(ad.getString("zip")).append("\n");
            address = sb.toString();
          }
          if (official.has("phones")){
            StringBuilder sb = new StringBuilder();
            JSONArray phoneArray = official.getJSONArray("phones");
            for (int k = 0; k < phoneArray.length(); k++) {
              sb.append(phoneArray.getString(k)).append("\n");
            }
            phones = sb.toString();
          }
          if (official.has("emails")) {
            email = official.getString("emails");
          }
          if (official.has("urls")){
            urls = official.getString("urls");
          }
          if (official.has("photoUrl")) {
            photosUrl = official.getString("photoUrl");
          }

          Official newOfficial = new Official(
                  name,
                  seat,
                  party,
                  address,
                  phones,
                  email,
                  urls,
                  photosUrl
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
