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
      String locationString = String.format("%s %s %s",
              normalizedInput.getString("city") + ", ",
              normalizedInput.getString("state"),
              normalizedInput.getString("zip")
      );
      mainActivity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          mainActivity.displayLocation(locationString);
        }
      });
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
          List<String> address = new ArrayList<>();
          List<String> phones = new ArrayList<>();
          List<String> email = new ArrayList<>();
          List<String> urls = new ArrayList<>();
          String photosUrl = "x";
          String facebook = "";
          String twitter = "";
          String youtube = "";

          if (official.has("address")){
            JSONArray addressArray = official.getJSONArray("address");
            for (int k = 0; k < addressArray.length(); k++) {
              StringBuilder sb = new StringBuilder();
              JSONObject ad = addressArray.getJSONObject(0);
              sb.append(ad.getString("line1")).append("\n");
              sb.append(ad.getString("city"));
              if (!ad.getString("state").equals("DC")) {
                sb.append(", ");
              } else {
                sb.append(" ");
              }
              sb.append(ad.getString("state")).append("\n");
              sb.append(ad.getString("zip")).append("\n");
              address.add(sb.toString());
            }
          }
          if (official.has("phones")){
            JSONArray phoneArray = official.getJSONArray("phones");
            for (int k = 0; k < phoneArray.length(); k++) {
              phones.add(phoneArray.getString(k));
            }
          }
          if (official.has("emails")) {
            JSONArray emailsArray = official.getJSONArray("emails");
            for (int k = 0; k < emailsArray.length(); k++) {
              email.add(emailsArray.getString(k));
            }
          }
          if (official.has("urls")){
            JSONArray urlsArray = official.getJSONArray("urls");
            for (int k = 0; k < urlsArray.length(); k++) {
              urls.add(urlsArray.getString(k));
            }
          }
          if (official.has("photoUrl")) {
            photosUrl = official.getString("photoUrl");
          }
          if (official.has("channels")) {
            JSONArray channelsArray = official.getJSONArray("channels");
            for (int k = 0; k < channelsArray.length(); k++) {
              JSONObject channel = channelsArray.getJSONObject(k);
              if (channel.getString("type").equals("Facebook")) {
                facebook = channel.getString("id");
              } else if (channel.getString("type").equals("Twitter")) {
                twitter = channel.getString("id");
              } else if (channel.getString("type").equals("YouTube")) {
                youtube = channel.getString("id");
              }
            }
          }

          Official newOfficial = new Official(
                  name,
                  seat,
                  party,
                  address,
                  phones,
                  email,
                  urls,
                  photosUrl,
                  facebook,
                  twitter,
                  youtube
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
