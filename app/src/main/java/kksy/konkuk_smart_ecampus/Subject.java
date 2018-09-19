package kksy.konkuk_smart_ecampus;

public class Subject {

    private String subID; //과목번호 (key)
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
