package com.hina.trial;

public class User {

    String name;
    String id;
    String email;
    String mob;
    String profile;

    public User() {

    }

    public User(String id, String name,String mob, String email,String profile) {
        this.name = name;
        this.mob = mob;
        this.email = email;
        this.id = id;
        this.profile = profile;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
