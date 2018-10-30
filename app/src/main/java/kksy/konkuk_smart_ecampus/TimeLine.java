package kksy.konkuk_smart_ecampus;

import java.util.ArrayList;
import java.util.List;

public class TimeLine {


    private List<TimeLineBoardFormat> materials;//강의자료 게시글 번호
    private List<TimeLineBoardFormat> notice;//공지사항
    private List<TimeLineBoardFormat> homework;//과제

    public TimeLine(){
        this.materials=new ArrayList<>();
        this.notice=new ArrayList<>();
        this.homework=new ArrayList<>();
    }

    public List<TimeLineBoardFormat> getMaterials() {
        return materials;
    }

    public void setMaterials(List<TimeLineBoardFormat> materials) {
        this.materials = materials;
    }

    public List<TimeLineBoardFormat> getNotice() {
        return notice;
    }

    public void setNotice(List<TimeLineBoardFormat> notice) {
        this.notice = notice;
    }

    public List<TimeLineBoardFormat> getHomework() {
        return homework;
    }

    public void setHomework(List<TimeLineBoardFormat> homework) {
        this.homework = homework;
    }
}
