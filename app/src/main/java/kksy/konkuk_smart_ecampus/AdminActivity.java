package kksy.konkuk_smart_ecampus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
현재는 포탈에서 학생 정보를 가지고 올 수 없으므로
학생은 관리자 페이지에서 임시로 등록하고 삭제할 수 있도록 구현한다.
 */
public class AdminActivity extends AppCompatActivity {

    Button regi;//학생 등록 버튼
    EditText ptID;//포탈 아이디
    EditText ptPW;//포탈 비밀번호
    EditText stID;//학번
    EditText department;//부서
    //ImageView imgURL;//이미지 URL

    //데이터베이스
    MyDBHandler myDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        regi=(Button)findViewById(R.id.regi);
        ptID=(EditText)findViewById(R.id.ptID);
        ptPW=(EditText)findViewById(R.id.ptPW);
        stID=(EditText)findViewById(R.id.stID);
        department=(EditText)findViewById(R.id.department);


    }

    public void regi(View view) {
        //학생 정보를 파이어베이스에 등록함
        myDBHandler=new MyDBHandler("student");

        //객체 생성
        Student student=new Student(ptID.getText().toString(),ptPW.getText().toString(),
                stID.getText().toString(),department.getText().toString()
                ,"");

        //객체를 DB에 등록
        myDBHandler.newStudent(student);

    }
}
