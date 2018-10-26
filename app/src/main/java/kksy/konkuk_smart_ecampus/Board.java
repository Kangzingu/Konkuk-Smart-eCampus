package kksy.konkuk_smart_ecampus;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Board {

    //private key
    private String boardID;//게시글번호(key) 과제, 공지, 강의자료
    private String uploadDate;//게시글 로드 시간

    private String type;//게시글 유형 : 과제, 공지, 강의자료
    //private String index;(autoincrement)

    private String context;//게시글 내용
    private String title;//게시글 제목
    //private String file;//file url

    private String proID_subID;//해당 게시글이 속한 강의의 key( 교원번호-과목번호 )


    public Board(String boardID, String type, String title) {
        this.boardID = boardID;
        this.type = type;
        this.title = title;
    }


    public Board(String type, /*String index,*/ String title,String context,String proID_subID){
        this.title=title;
        this.type = type;
        //this.index = index;
        this.context = context;
        this.proID_subID=proID_subID;

        /*
        게시판 업로드 시간 받아오기
         */
        //1. 현재 시간을 msec으로 계산
        long now=System.currentTimeMillis();
        //2. 현재시간을 date변수에 저장
        Date date=new Date(now);
        //3. 시간을 나타내는 포맷을 정함
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",java.util.Locale.getDefault());
        //4. uploadDate 변수에 값을 저장함
        this.uploadDate=dateFormat.format(date);

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

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}
