package kksy.konkuk_smart_ecampus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
현재는 포탈에서 학생 정보를 가지고 올 수 없으므로
학생은 관리자 페이지에서 임시로 등록하고 삭제할 수 있도록 구현한다.
 */
public class AdminActivity extends AppCompatActivity {

    Spinner selectTable ;
    Button regi;//학생 등록 버튼
    EditText ptID;//포탈 아이디
    EditText sName;//학생 이름
    EditText ptPW;//포탈 비밀번호
    EditText stID;//학번
    EditText department;//부서
    //ImageView imgURL;//이미지 URL

    //데이터베이스
    MyDBHandler myDBHandler;
    Student student;
    Professor professor;
    Subject subject;

    //
    int pos=0;
    static final String TAG="AdminActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        selectTable=(Spinner)findViewById(R.id.selectTable);
        regi=(Button)findViewById(R.id.regi);
        ptID=(EditText)findViewById(R.id.ptID);
        ptPW=(EditText)findViewById(R.id.ptPW);
        stID=(EditText)findViewById(R.id.stID);
        department=(EditText)findViewById(R.id.department);
        sName=(EditText)findViewById(R.id.sName);

        selectTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    public void regiStudent(){
        //객체 생성
        myDBHandler=new MyDBHandler("student");
       student=new Student(ptID.getText().toString(),ptPW.getText().toString(),
                stID.getText().toString(),department.getText().toString()
                ,"",sName.getText().toString());

    }
    public void regiProfessor(){
        myDBHandler=new MyDBHandler("professor");
        professor=new Professor(ptID.getText().toString(),sName.getText().toString());

    }
    public void regiSubject(){
        myDBHandler=new MyDBHandler("subject");
        subject=new Subject(ptID.getText().toString(),sName.getText().toString());
    }
    public void regiLecture(){

    }
    public void regiSugang(){

    }
    public void regiTimeLine(){

    }

    public void regi(View view) { //학생 정보를 파이어베이스에 등록함
        //객체를 DB에 등록
        switch (pos){
            case 0://학생
                regiStudent();
                //객체를 DB에 등록
                myDBHandler.newStudent(student);
                break;
            case 1://교수
                regiProfessor();
                myDBHandler.newProfessor(professor);
                break;
            case 2://과목
                regiSubject();
                myDBHandler.newSubject(subject);
                break;
            case 3://수강
                regiSugang();
                break;
            case 4://강의
                regiLecture();
                break;
            case 5://타임라인
                regiTimeLine();
                break;
        }

        ptID.setText("");ptPW.setText("");stID.setText("");department.setText("");sName.setText("");

        //나중에 여기 지우기
        Toast.makeText(getApplicationContext(),"등록완료^.<",Toast.LENGTH_SHORT).show();

    }
}
