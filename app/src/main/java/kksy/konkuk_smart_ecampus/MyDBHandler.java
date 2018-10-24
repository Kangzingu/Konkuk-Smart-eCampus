package kksy.konkuk_smart_ecampus;

import android.util.Log;
import android.widget.Toast;

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

    public void newBoard(Board board){

        /*
        1. 게시판 릴레이션에 추가
         */
        String tableNames;
        tableNames=board.getProID_subID();

        DatabaseReference relation_table;

        //board id 지정(set)
        //현재 type 의 id 중 마지막 아이디를 가지고 온다.-> +1을 한 결과를 set해줌
        relation_table=mdbRef.child(tableNames).child(board.getType()).child(board.getBoardID());

        //등록
        relation_table.setValue(board);

        /*
        2. 강의 릴레이션에 추가
         */
        //select * from Lecture where proID_subID=tableNames;
        DatabaseReference table;
        mdbRef=mdatabase.getReference("lecture");
        table=mdbRef;

        Query query = table.equalTo(tableNames).equalTo("p25787542-s184325");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Lecture temp_lecture=snapshot.getValue(Lecture.class);

                    if (temp_lecture!=null)
                    Log. i ("MyDBHandler",temp_lecture.getProID());
                    else
                        Log. i ("MyDBHandler","error");
                }


            }
            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void newLecture(Lecture lecture){
        String tableNames;
        tableNames=lecture.getProID()+"-"+lecture.getSubID();

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
