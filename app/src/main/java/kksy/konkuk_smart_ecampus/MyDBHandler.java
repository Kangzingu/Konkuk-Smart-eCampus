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
        tableNames=board.getProID_subID();


        //board id 지정( set boardID() ) : 현재 type 의 id 중 마지막 아이디를 가지고 온다.-> +1을 한 결과를 set해줌
        //select * from Board where type=board.getType() ;
        Query query1;
        query1 = mdbRef.child(tableNames).child(board.getType()).orderByChild("boardID").limitToLast(1);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            /*
            OnDataChange 함수는 초기 값으로 한 번 호출되며, 이 위치의 데이터가 업데이트 될때마다 다시 호출됨.
             */
            @Override public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                     // Log.i("MyDBHandler",snapshot.getValue().toString());

                      board.setBoardID(snapshot.getValue(Board.class).getBoardID()+1);
                      
                     // Log.i("MyDBHandler","boardid "+board.getBoardID());
                    }

                //등록
                mdbRef.child(board.getProID_subID()).child(board.getType()).child(board.getBoardID()+"").setValue(board);

               // Log.i("MyDBHandler","등록?");
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
