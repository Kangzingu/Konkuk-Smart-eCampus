package kksy.konkuk_smart_ecampus;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

public class MyDBHandler {

    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;

    Handler handler;

    public MyDBHandler(String TABLE){//default constructor
        mdatabase = FirebaseDatabase.getInstance();
        mdbRef=mdatabase.getReference(TABLE);

        if(mdbRef==null){
            //table이 없으면 생성
            mdbRef.child(TABLE);
        }else{
            //table이 있으면 진행하지 않음
        }

    }

    public void newStudent(Student student){
        String tableNames;
        tableNames=student.getStudentID();

        DatabaseReference relation_table;
        relation_table=mdbRef.child(tableNames);

        //등록
        relation_table.setValue(student);
    }

    public void newProfessor(Professor professor){
        String tableNames;
        tableNames=professor.getProID();

        DatabaseReference relation_table;
        relation_table=mdbRef.child(tableNames);

        //등록
        relation_table.setValue(professor);
    }

    public void newSubject(Subject subject){
        String tableNames;
        tableNames=subject.getSubID();

        DatabaseReference relation_table;
        relation_table=mdbRef.child(tableNames);

        //등록
        relation_table.setValue(subject);
    }

    public void newBoard(final Board board){

        /*
        1. 게시판 릴레이션에 추가
         */
        String tableNames;
        tableNames=board.getSubID_proID();

        DatabaseReference relation_table;
        relation_table=mdbRef.child(tableNames).child(board.getType()).child(board.getUploadDate()).push();
        board.setBoardID(relation_table.getKey());
        Log.i("MyDBHandler",board.getBoardID());
        //등록
        relation_table.setValue(board);

        /*
        게시글이 추가되는 경우 관련학생들의 time line 수정 필요
         */

        newTimeLine(board);




/*
이거 일단 남겨둘거얌
 */
        //board id 지정( set boardID() ) : 현재 type 의 id 중 마지막 아이디를 가지고 온다.-> +1을 한 결과를 set해줌
        //select * from Board where type=board.getType() ;
//        Query query1;
//        query1 = mdbRef.child(tableNames).child(board.getType()).orderByChild("boardID").limitToLast(1);
//
//        query1.addListenerForSingleValueEvent(new ValueEventListener() {
//            /*
//            OnDataChange 함수는 초기 값으로 한 번 호출되며, 이 위치의 데이터가 업데이트 될때마다 다시 호출됨.
//             */
//            @Override public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                     // Log.i("MyDBHandler",snapshot.getValue().toString());
//
//                      board.setBoardID(snapshot.getValue(Board.class).getBoardID()+1);
//
//                     // Log.i("MyDBHandler","boardid "+board.getBoardID());
//                    }
//
//                //등록
//                mdbRef.child(board.getProID_subID()).child(board.getType()).child(board.getBoardID()+"").setValue(board);
//
//               // Log.i("MyDBHandler","등록?");
//            }
//            @Override public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }

    public void newLecture(Lecture lecture){
        String tableNames;
        tableNames=lecture.getProID_subID();

        DatabaseReference relation_table;
        relation_table=mdbRef.child(tableNames);

        //등록
        relation_table.setValue(lecture);
    }

    public void newAttendance(Attendance attendance){
        String tableNames;
        tableNames=attendance.getSubID_pID();

        DatabaseReference relation_table;
        relation_table=mdbRef.child(tableNames).child(attendance.studentID).child(attendance.date).child(attendance.round);

        //등록
        relation_table.setValue(attendance);
    }

    public void newTimeLine(final Board board){
       /*
        게시글이 추가되는 경우,
        수강db에서 board.getProID_subID()에서 subID값을 가져옴

        subID가 있는 모든 데이터에 대하여

        getType()에 맞는 material, notice, homework중 선택하여 값을 넣어준다.
         */

        //1. 수강db에서 board.getSubID_proID()에서 subID값을 가져옴
        String board_subID=board.getSubID_proID().split("-")[0];
       // Log.i("MyDBHandler","board과목번호: "+board_subID);//s1

        //2. subID가 있는 모든 데이터에 대하여
        Query query;
        query = mdatabase.getReference("sugang").orderByChild("subID").equalTo(board_subID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                   /*

                  로그 :  Log. i ("MyDBHandler",snapshot.getKey());
                  결과 :  201611210

                  로그 :  Log. i ("MyDBHandler",snapshot.getValue().toString());
                  결과 : {imgURL=, beconCheck=true, autoLoginCheck=false, studentID=201611210, potalPW=1928374, potalID=kwisjr, studentName=다스리, department=sw}

                  */

                    //3. getType()에 맞는 material, notice, homework중 선택하여 값을 넣어준다.
                    Sugang tmpSugang;
                    if (snapshot.getValue(Sugang.class).getTimeLine()!=null){
                         tmpSugang=new Sugang(snapshot.getValue(Sugang.class).getSubID_studentID(),
                                snapshot.getValue(Sugang.class).getSubID(),
                                snapshot.getValue(Sugang.class).getStudentID(),
                                snapshot.getValue(Sugang.class).getTimeLine());//객체 담기
                    }else{
                         tmpSugang=new Sugang(snapshot.getValue(Sugang.class).getSubID_studentID(),
                                snapshot.getValue(Sugang.class).getSubID(),
                                snapshot.getValue(Sugang.class).getStudentID());
                    }


                    if (board.getType().equals("과제")){
                        tmpSugang.getTimeLine().getHomework().add(new TimeLineBoardFormat(board.getBoardID()));
                       // Log.i("MyDBHandler","1 "+tmpSugang.getTimeLine().getHomework().get(0).getBoardID());
                    }else if(board.getType().equals("강의자료")){
                        tmpSugang.getTimeLine().getMaterials().add(new TimeLineBoardFormat(board.getBoardID()));
                        //Log.i("MyDBHandler","2" +tmpSugang.getTimeLine().getMaterials().get(0).getBoardID());
                    }else if(board.getType().equals("공지")){
                        tmpSugang.getTimeLine().getNotice().add(new TimeLineBoardFormat(board.getBoardID()));
                       // Log.i("MyDBHandler","3 "+tmpSugang.getTimeLine().getNotice().get(0).getBoardID());
                    }else //Log.i("MyDBHandler","4 "+board.getType());

                    if (tmpSugang.getTimeLine()==null){
                        Log.i("MyDBHandler","timeline이 null");
                    }
                    Log.i("MyDBHandler",snapshot.getValue().toString());

                    mdatabase.getReference("sugang").child(tmpSugang.getSubID_studentID())
                            .setValue(tmpSugang);

                }

            }

            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void newSugang( Sugang sugang){
        String tableNames;
        tableNames=sugang.getSubID_studentID();

        DatabaseReference relation_table;
        relation_table=mdbRef.child(tableNames);

        //등록
        relation_table.setValue(sugang);
    }
}
