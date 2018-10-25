package kksy.konkuk_smart_ecampus;

import java.util.ArrayList;
import java.util.List;

public class TimeLine {

    public List<String> materials;//강의자료 게시글 번호
    public List<String> notice;//공지사항
    public List<String> homework;//과제

    public TimeLine(){
        this.materials=new ArrayList<String>();
        this.notice=new ArrayList<String>();
        this.homework=new ArrayList<String>();
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public List<String> getNotice() {
        return notice;
    }

    public void setNotice(List<String> notice) {
        this.notice = notice;
    }

    public List<String> getHomework() {
        return homework;
    }

    public void setHomework(List<String> homework) {
        this.homework = homework;
    }
}
