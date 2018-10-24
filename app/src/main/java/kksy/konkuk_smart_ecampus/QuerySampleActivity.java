package kksy.konkuk_smart_ecampus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class QuerySampleActivity extends AppCompatActivity {

    static final String TABLE="student";//임시로 지정

    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;
    Query query;

    Button btn_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_sample);

        mdatabase = FirebaseDatabase.getInstance();
        mdbRef=mdatabase.getReference(TABLE);
    }

    public void queryTest(View view) {
        //select * from Lecture where proID_subID=tableNames;

        query = mdbRef.orderByChild("studentID").equalTo("201611210");

        Log. i ("MyDBHandler",query.toString());//201611210-> com.google.firebase.database.Query@f860247 /com.google.firebase.database.Query@e1fbb9d

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){


                   /*

                  로그 :  Log. i ("MyDBHandler",snapshot.getKey());
                  결과 :  201611210

                  로그 :  Log. i ("MyDBHandler",snapshot.getValue().toString());
                  결과 : {imgURL=, beconCheck=true, autoLoginCheck=false, studentID=201611210, potalPW=1928374, potalID=kwisjr, studentName=다스리, department=sw}

                  */

                   Student student=snapshot.getValue(Student.class);//객체 담기
                   Log.i("MyDBHandler",student.getStudentName());//출력 예상 결과 : 다스리

                }

            }

            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });

        Toast.makeText(this,"완료",Toast.LENGTH_SHORT).show();
    }
}
