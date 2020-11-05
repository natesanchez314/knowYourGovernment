package com.natesanchez.knowyourgovernment;

public class Official {

  private String name;
  private String seat;
  private String party;

  public Official(String name, String seat, String party) {
    this.name = name;
    this.seat = seat;
    this.party = party;
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
}
