package com.example.beamagayver.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class LikesModel implements Serializable {

    private int number;
    private ArrayList<String> users;

    public LikesModel() {
    }

    public LikesModel(int number, ArrayList<String> users) {
        this.number = number;
        this.users = users;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public void setSingleUser(String id) {
        users.add(id);
    }

    public void removeSingleUser(String id) {
        users.remove(id);
    }
}
