package com.example.cts;

public class users {

    String username, age, gender, dt, status, id;
    public users(){
    }

    public users(String username,String age, String gender, String dt, String status, String id) {
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.dt = dt;
        this.status=status;
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getAge()
    {
        return age;
    }

    public String getGender()
    {
        return gender;
    }

    public String getDt()
    {
        return dt;
    }

    public String getStatus()
    {
        return status;
    }

    public String getId()
    {
        return id;
    }
}
