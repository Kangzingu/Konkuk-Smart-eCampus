package kksy.konkuk_smart_ecampus;

public class Board {

    //private key
    private String boardID;//게시글번호(key) 과제, 공지, 강의자료

    private String type;//게시글 유형 : 과제, 공지, 강의자료
    //private String index;(autoincrement)

    private String context;//게시글 내용
    private String title;//게시글 제목
    //private String file;//file url

    private String proID_subID;//해당 게시글이 속한 강의의 key( 교원번호-과목번호 )



    public Board(String type, /*String index,*/ String title,String context,String proID_subID){
        this.title=title;
        this.type = type;
        //this.index = index;
        this.context = context;
        this.proID_subID=proID_subID;
        this.boardID=boardID;
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

    public String getProID_subID() {
        return proID_subID;
    }

    public void setProID_subID(String proID_subID) {
        this.proID_subID = proID_subID;
    }

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }
}
