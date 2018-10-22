package kksy.konkuk_smart_ecampus;

public class Student {
    private String studentName;//학생 이름
    private String potalID; //ID
    private String potalPW; //PW
    private String studentID; //학번 (key)
    private String department; //학과
    private boolean beconCheck; //비콘 ON OFF 유무 default false
    private boolean autoLoginCheck; //자동 로그인 체크 유므 default false
    private String imgURL;//학생 사진

    /*
    constructor
     */
    public Student(){//default constructor

    }
    public Student(String potalID, String studentName,String potalPW, String studentID, String department, String imgURL){

        this.studentName=studentName;
        this.potalID = potalID;
        this.potalPW = potalPW;
        this.studentID = studentID;
        this.department = department;
        this.imgURL = imgURL;

        this.autoLoginCheck=false;//default : false (자동로그인 활성화 안됨)
        this.beconCheck=false;//default : false (비콘 활성화 안됨)
    }

    /*
    get, set 함수
     */
    public String getPotalID() {
        return potalID;
    }

    public void setPotalID(String potalID) {
        this.potalID = potalID;
    }

    public String getPotalPW() {
        return potalPW;
    }

    public void setPotalPW(String potalPW) {
        this.potalPW = potalPW;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isBeconCheck() {
        return beconCheck;
    }

    public void setBeconCheck(boolean beconCheck) {
        this.beconCheck = beconCheck;
    }

    public boolean isAutoLoginCheck() {
        return autoLoginCheck;
    }

    public void setAutoLoginCheck(boolean autoLoginCheck) {
        this.autoLoginCheck = autoLoginCheck;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }


}
