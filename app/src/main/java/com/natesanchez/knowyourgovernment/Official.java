package com.natesanchez.knowyourgovernment;

import java.io.Serializable;

public class Official implements Serializable {

  private String name;
  private String seat;
  private String party;
  private String address;
  private String number;
  private String email;
  private String website;
  private String imageUrl;

  public Official(String name, String seat, String party, String address, String number, String email, String website, String image) {
    this.name = name;
    this.seat = seat;
    this.party = party;
    this.address = address;
    this.number = number;
    this.email = email;
    this.website = website;
    this.imageUrl = image;
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

  public String getAddress() {
    return address;
  }

  public String getNumber() {
    return number;
  }

  public String getEmail() {
    return email;
  }

  public String getWebsite() {
    return website;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
