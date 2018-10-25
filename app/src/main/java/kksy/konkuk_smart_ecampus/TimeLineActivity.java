package kksy.konkuk_smart_ecampus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TimeLineActivity extends AppCompatActivity {

    /*
     * 과제
     * 공지
     * 강의자료
     * 등을 타임라인 형식으로 보여줘야 함.
     *
     * 쿼리에서 가져오는 것*/

    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        mdatabase = FirebaseDatabase.getInstance();

        DatabaseReference table;
        mdbRef=mdatabase.getReference("board");
        table=mdbRef;

        Query query = table.equalTo("board").equalTo("p25787542-s184325");
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
}
