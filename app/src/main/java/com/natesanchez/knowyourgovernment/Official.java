package com.natesanchez.knowyourgovernment;

import java.io.Serializable;

public class Official implements Serializable {

  private String name;
  private String seat;
  private String party;
  private String address;
  private String number;
  private String website;
  private String imageUrl;

  public Official(String name, String seat, String party, String address, String number, String website, String image) {
    this.name = name;
    this.seat = seat;
    this.party = party;
    if (address != null) {
      this.address = address;
    }
    if (number != null) {
      this.number = number;
    }
    if (website != null) {
      this.website = website;
    }
    if (imageUrl != null) {
      this.imageUrl = image;
    }
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

  public String getWebsite() {
    return website;
  }

  public String getImageUrl() {
    return imageUrl;
  }
}
