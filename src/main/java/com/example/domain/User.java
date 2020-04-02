package com.example.domain;


public class User {

  private long id;
  private String username;
  private String password;
  private long enabled;
  private long locked;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public long getEnabled() {
    return enabled;
  }

  public void setEnabled(long enabled) {
    this.enabled = enabled;
  }


  public long getLocked() {
    return locked;
  }

  public void setLocked(long locked) {
    this.locked = locked;
  }

}
