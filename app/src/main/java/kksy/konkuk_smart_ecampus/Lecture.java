package kksy.konkuk_smart_ecampus;

import java.util.ArrayList;
import java.util.List;

public class Lecture {

    //private key는 교원번호-과목번호
    private String proID_subID;

    private String proID; //교원번호 (key)
    private String subID; //과목번호 (key)

    //껍데기 : 강의계획서, 온라인 강의, 시험, 질의응답, 투표, 설문

    List<Board> materials;//강의자료
    List<Board> notice;//공지사항
    List<Board> homework;//과제

    private String beconInfo;//해당 강의에서 출결에 사용되는 비콘 정보

    private boolean attendCheck;//출석 활성화 여부

    //출결 인정 시간
    private List<String> attendTime;//출석 인정
    private List<String> lateTime;//지각 처리


    /*
    Constructor
     */
    public Lecture(){

    }

    public Lecture(String proID, String subID, String beconInfo, List<String> attendTime, List<String> lateTime) {

        this.proID_subID=proID+"-"+subID;

        this.proID = proID;
        this.subID = subID;

        this.beconInfo = beconInfo;

        this.attendCheck=false;

        this.attendTime=new ArrayList<String>();
        this.lateTime=new ArrayList<String>();

        this.attendTime=attendTime;
        this.lateTime=lateTime;

    }

    /*
    Getter and Setter
     */

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

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }

    public String getSubID() {
        return subID;
    }

    public void setSubID(String subID) {
        this.subID = subID;
    }

    public String getBeconInfo() {
        return beconInfo;
    }

    public void setBeconInfo(String beconInfo) {
        this.beconInfo = beconInfo;
    }

    public String getProID_subID() {
        return proID_subID;
    }

    public void setProID_subID(String proID_subID) {
        this.proID_subID = proID_subID;
    }
}
