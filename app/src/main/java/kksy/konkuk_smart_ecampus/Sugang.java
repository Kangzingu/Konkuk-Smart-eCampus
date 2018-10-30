package kksy.konkuk_smart_ecampus;

import java.util.List;

public class Sugang {
    //prImary key
    private String subID_studentID;

    private String subID; //수강하는 과목의 과목번호(외래키)
    private String studentID; //수강하는 학생의 학번(외래키)

    //과목별 진행 상황==타임라인
    private TimeLine timeLine;

    public Sugang(){
        this.timeLine =new TimeLine();
    }

    public Sugang(String subID_studentID, String subID, String studentID ,TimeLine timeLine) {
        this.subID_studentID = subID_studentID;
        this.subID = subID;
        this.studentID = studentID;
        this.timeLine=timeLine;
    }

    public Sugang(String subID_studentID, String subID, String studentID ) {
        this.subID_studentID = subID_studentID;
        this.subID = subID;
        this.studentID = studentID;
        this.timeLine =new TimeLine();
    }

    public String getSubID_studentID() {
        return subID_studentID;
    }

    public void setSubID_studentID(String subID_studentID) {
        this.subID_studentID = subID_studentID;
    }

    public String getSubID() {
        return subID;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public TimeLine getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(TimeLine timeLine) {
        this.timeLine = timeLine;
    }
}
