package kksy.konkuk_smart_ecampus;

public class Attendance {

    public String subID_pID;//과목코드-교원번호(key)
    public String studentID;//학번

    public String round;//회차
    public String date;//yyyy-mm-dd hh:mm

    public String state;//지각, 결석, 출석

    public Attendance(String subID_pID, String studentID, String round, String date, String state) {
        this.subID_pID = subID_pID;
        this.studentID = studentID;
        this.round = round;
        this.date = date;
        this.state = state;
    }

    public String getSubID_pID() {
        return subID_pID;
    }

    public void setSubID_pID(String subID_pID) {
        this.subID_pID = subID_pID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
