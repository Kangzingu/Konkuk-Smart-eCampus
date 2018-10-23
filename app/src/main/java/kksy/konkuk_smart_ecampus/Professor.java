package kksy.konkuk_smart_ecampus;

public class Professor {
    private String proID; //교원번호(private key)
    private String proName; //교수이름

    public Professor(){

    }
    public Professor(String proID, String proName) {
        this.proID = proID;
        this.proName = proName;
    }

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }


}
