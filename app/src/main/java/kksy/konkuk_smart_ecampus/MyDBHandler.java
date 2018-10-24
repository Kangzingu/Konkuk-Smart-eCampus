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

import java.util.logging.Handler;

public class MyDBHandler {

    String check;
    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;

    Handler handler;

    public MyDBHandler(String TABLE){//default constructor
        check="";
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

    public void newBoard(Board board){

        /*
        1. 게시판 릴레이션에 추가
         */
        String tableNames;
        tableNames=board.getProID_subID();

        DatabaseReference relation_table;

        //board id 지정( set boardID() ) : 현재 type 의 id 중 마지막 아이디를 가지고 온다.-> +1을 한 결과를 set해줌
        //select * from Board where type=board.getType() ;
        Query query;
        query = mdbRef.child(tableNames).child(board.getType()).orderByChild("boardID").limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                      Log.i("MyDBHandler",snapshot.getValue().toString());
                      Board LastBoard=snapshot.getValue(Board.class);
                      check=LastBoard.getBoardID();
                    }

            }
            @Override public void onCancelled(DatabaseError databaseError) {

            }
        });

       if(check!=""){
           Log.i("MyDBHandler","check에 값이 채워짐");
           board.setBoardID((Integer.parseInt(check)+1)+"");
           relation_table=mdbRef.child(tableNames).child(board.getType()).child(board.getBoardID());
       }else{
           Log.i("MyDBHandler","check는계쏙 0임");
           board.setBoardID(0+"");
           relation_table=mdbRef.child(tableNames).child(board.getType()).child(board.getBoardID());
       }

        //등록
       relation_table.setValue(board);

        /*
        2. 강의 릴레이션에 추가
         */

        //select * from Lecture where proID_subID=tableNames;
        mdbRef=mdatabase.getReference("lecture");

       query = mdbRef.orderByChild("proID_subID").equalTo("p25787542-s184325");//"p25787542-s184325" 로 임의 지정test

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){



                }
            }
            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });

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
        relation_table=mdbRef.child(tableNames);

        //등록
        relation_table.setValue(attendance);
    }
}
