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
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/*
현재는 포탈에서 학생 정보를 가지고 올 수 없으므로
학생은 관리자 페이지에서 임시로 등록하고 삭제할 수 있도록 구현한다.
 */
public class AdminActivity extends AppCompatActivity {

    Spinner selectTable ;
    Button regi;//학생 등록 버튼
    EditText e1;//포탈 아이디
    EditText e2;//학생 이름
    EditText e3;//포탈 비밀번호
    EditText e4;//학번
    EditText e5;//부서
    //ImageView imgURL;//이미지 URL

    //데이터베이스
    MyDBHandler myDBHandler;
    Student student;
    Professor professor;
    Subject subject;
    Lecture lecture;
    Board board;
    Sugang sugang;

    //
    int pos=0;
    static final String TAG="AdminActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        selectTable=(Spinner)findViewById(R.id.selectTable);
        regi=(Button)findViewById(R.id.regi);

        e1=(EditText)findViewById(R.id.e1);
        e2=(EditText)findViewById(R.id.e2);
        e3=(EditText)findViewById(R.id.e3);
        e4=(EditText)findViewById(R.id.e4);
        e5=(EditText)findViewById(R.id.e5);


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
       student=new Student(e1.getText().toString(),e2.getText().toString(),
                e3.getText().toString(),e4.getText().toString()
                ,e5.getText().toString(),"");//imgURL 수정 필요

    }
    public void regiProfessor(){
        myDBHandler=new MyDBHandler("professor");
        professor=new Professor(e1.getText().toString(),e2.getText().toString());

    }

    public void regiSubject(){
        myDBHandler=new MyDBHandler("subject");
        subject=new Subject(e1.getText().toString(),e2.getText().toString());
    }
    public void regiLecture(){

        myDBHandler=new MyDBHandler("lecture");

        //출석 인정 시간 임의로 지정
        /*
        split 예제
        String str1;
        String word1 = str1.split(";")[0];
        String word2 = str1.split(";")[1];
         */

        String str1=e3.getText().toString();
        String str2=e4.getText().toString();

        List<String> list1=new ArrayList<String>();
        List<String> list2=new ArrayList<String>();

        list1.add(str1.split("~")[0]);
        list1.add(str1.split("~")[1]);
        list2.add(str2.split("~")[0]);
        list2.add(str2.split("~")[1]);

        String beconInfo="진구님 여기에 비콘 정보 넣어주세요.";
        lecture=new Lecture(e1.getText().toString(),e2.getText().toString(),beconInfo,list1,list2);// 시간 입력 방식  HH:mm

    }
    public void regiSugang(){
        myDBHandler=new MyDBHandler("sugang");

        String str1=e1.getText().toString();
        sugang=new Sugang(e1.getText().toString(),str1.split("-")[0],str1.split("-")[1]);

    }
    public void regiBoard(){
        myDBHandler=new MyDBHandler("board");
        board=new Board(e1.getText().toString(),e2.getText().toString(),
                e3.getText().toString(),e4.getText().toString());


        /*
        새로운 게시물이 올라오는 경우, 알람 설정
         */
        //알람 설정 여기서 하면 될것같음
    }

    public void regi(View view) { //학생 정보를 파이어베이스에 등록함
        //객체를 DB에 등록
        switch (pos){
            case 0://학생->완료
                regiStudent();
                //객체를 DB에 등록
                myDBHandler.newStudent(student);
                break;
            case 1://교수->완료
                regiProfessor();
                myDBHandler.newProfessor(professor);
                break;
            case 2://과목->완료
                regiSubject();
                myDBHandler.newSubject(subject);
                break;
            case 3://수강
                regiSugang();
                myDBHandler.newSugang(sugang);
                break;
            case 4://강의
                regiLecture();
                myDBHandler.newLecture(lecture);
                break;
            case 5://게시판
                regiBoard();
                myDBHandler.newBoard(board);
                break;
        }

        e1.setText("");e2.setText("");e3.setText("");e4.setText("");e5.setText("");

        //나중에 여기 지우기
        Toast.makeText(getApplicationContext(),"등록완료^.<",Toast.LENGTH_SHORT).show();

    }
}
