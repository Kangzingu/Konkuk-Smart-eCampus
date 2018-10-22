package kksy.konkuk_smart_ecampus;

public class Board {

    //private key를 일단 push로 둠

    private String type;//게시글 유형 : 과제, 공지, 강의자료
    //private String index;(autoincrement)

    private String context;//게시글 내용
    private String title;//게시글 제목
    //private String file;//file url

    private String subID;//해당 게시글이 속한 과목의 과목번호(key)



    public Board(String type, /*String index,*/ String title,String context,String subID){
        this.title=title;
        this.type = type;
        //this.index = index;
        this.context = context;
        this.subID=subID;
    }
    public Board(){

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public String getIndex() {
//        return index;
//    }
//
//    public void setIndex(String index) {
//        this.index = index;
//    }
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
    public String getsubID(){
        return this.subID;
    }
    public void setsubID(String subName){
        this.subID=subName;
    }
}
