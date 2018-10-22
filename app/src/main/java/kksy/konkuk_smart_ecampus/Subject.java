package kksy.konkuk_smart_ecampus;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    private String subID; //과목번호 (key)
    private String subName; //과목이름

    private boolean attendCheck;//출석 활성화 여부

    //출결 인정 시간
    private List<String> attendTime;//출석 인정
    private List<String> lateTime;//지각 처리

    public Subject(){

    }

    public Subject(String subID, String subName,List<String> attendTime, List<String> lateTime) {
        this.subID = subID;
        this.subName = subName;
        this.attendCheck=false;
        this.attendTime=new ArrayList<String>();
        this.lateTime=new ArrayList<String>();

        this.attendTime=attendTime;
        this.lateTime=lateTime;
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

    public boolean isAttendCheck() {
        return attendCheck;
    }

    public void setAttendCheck(boolean attendCheck) {
        this.attendCheck = attendCheck;
    }

    public List<String> getAttendTime() {
        return attendTime;
    }

    public void setAttendTime(List<String> attendTime) {
        this.attendTime = attendTime;
    }

    public List<String> getLateTime() {
        return lateTime;
    }

    public void setLateTime(List<String> lateTime) {
        this.lateTime = lateTime;
    }
}
