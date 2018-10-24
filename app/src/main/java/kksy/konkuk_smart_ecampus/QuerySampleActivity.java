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

    static final String TABLE="lecture";//임시로 지정

    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;
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

        final Query query = mdbRef.orderByKey().equalTo("p25787542-s184325");

      //  Log. i ("MyDBHandler",query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    //Lecture temp_lecture=snapshot.getValue(Lecture.class);

                    if(dataSnapshot.getRef()!=query){
                        Log. i ("MyDBHandler","나야");
                    }
//                    if (temp_lecture!=null)
//                        Log. i ("MyDBHandler",temp_lecture.getProID());
//                    else
//                        Log. i ("MyDBHandler","error");
                }

//
//                if(dataSnapshot.getValue(Lecture.class)!=null){
//                    Log. i ("MyDBHandler",dataSnapshot.getValue(Lecture.class).getLateTime().toString());
//                }

//                Lecture temp_lecture=new Lecture();
//                temp_lecture=dataSnapshot.getValue(Lecture.class);
//
//                    if (temp_lecture!=null)
//                        Log. i ("MyDBHandler",temp_lecture.getProID());
//                    else
//                        Log. i ("MyDBHandler","error");
//
//
//                Log. i ("MyDBHandler",dataSnapshot.getKey());

            }
            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });

        Toast.makeText(this,"완료",Toast.LENGTH_SHORT).show();
    }
}
