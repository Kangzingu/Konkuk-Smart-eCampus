package kksy.konkuk_smart_ecampus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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
        mdbRef=mdatabase.getReference("lecture");
        table=mdbRef;

        //Query query = table.equalTo();

    }
}
