package com.natesanchez.knowyourgovernment;

import java.io.Serializable;
import java.util.List;

public class Official implements Serializable {

  private String name;
  private String seat;
  private String party;
  private List<String> address;
  private List<String> number;
  private List<String> email;
  private List<String> website;
  private String imageUrl;
  private String facebook;
  private String twitter;
  private String youtube;

  public Official(String name, String seat, String party, List<String> address,
                  List<String> number, List<String> email, List<String> website, String image,
                  String facebook, String twitter, String youtube) {
    this.name = name;
    this.seat = seat;
    this.party = party;
    this.address = address;
    this.number = number;
    this.email = email;
    this.website = website;
    this.imageUrl = image;
    this.facebook = facebook;
    this.twitter = twitter;
    this.youtube = youtube;
  }

  public String getName() {
    return name;
  }

  public String getSeat() {
    return seat;
  }

  public String getParty() {
    return party;
  }

  public List<String> getAddress() {
    return address;
  }

  public List<String> getNumber() {
    return number;
  }

  public List<String> getEmail() {
    return email;
  }

  public List<String> getWebsite() {
    return website;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getFacebook() {
    return facebook;
  }

  public String getTwitter() {
    return twitter;
  }

  public String getYoutube() {
    return youtube;
  }
}
