package kksy.konkuk_smart_ecampus;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    private String subID; //과목번호 (private key)
    private String subName; //과목이름

    public Subject(){

    }

    public Subject(String subID, String subName) {
        this.subID = subID;
        this.subName = subName;
    }

    public String getSubID() {
        return subID;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }


}
