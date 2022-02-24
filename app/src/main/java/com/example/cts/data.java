package com.example.cts;

public class data {

    int SI_NO;
    String Name;
    String Email;

    public data(int SI_NO, String Name, String Email){

        this.SI_NO = SI_NO;
        this.Name = Name;
        this.Email = Email;

    }

    public int getSI_NO() {
        return SI_NO;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }
}
