package kksy.konkuk_smart_ecampus;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
}
