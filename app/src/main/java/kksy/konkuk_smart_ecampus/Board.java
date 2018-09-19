package kksy.konkuk_smart_ecampus;

public class Board {
    private String type;//게시글 유형 : 과제, 공지, 강의자료
    private String index;
    private String context;//게시글 내용
    private String title;//게시글 제목
    private String subName;//해당 게시글이 속한 과목
    //private String file;//file url

    public Board(String type, String index, String title,String context,String subName){
        this.title=title;
        this.type = type;
        this.index = index;
        this.context = context;
        this.subName=subName;
    }
    public Board(){

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
    public String getContext(){
        return this.context;
    }
    public void setContext(String context){
        this.context=context;
    }
    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getSubName(){
        return this.subName;
    }
    public void setSubName(String subName){
        this.subName=subName;
    }
}
