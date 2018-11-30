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
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    //yeori
    public final static String AUTH_KEY_FCM = "AAAAo0mUWQs:APA91bFwpft44gLY_oSyqgMB0exl-PMVFtj6rx2FfuJ8Oq6bTiV2gNnUQmTmsUNAQnU6_OPpNNY-gnww5D9CF7d_ZX39jp0-hj4b9Bw7zY8J6WXWoyx18LMe7q8YhYH9AcZgN7yvzDVL";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    String Token;
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
        //FirebaseMessaging.getInstance()
        //FirebaseMessaging.getInstance().subscribeToTopic("ALL");
        Token=FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, Token);
        try {
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        pushFCMNotification(Token, e1.getText().toString(), e2.getText().toString(), e4.getText().toString());
                    }catch (Exception ex){
                        Log.i(TAG, ex.toString());
                    }
                }
            });
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, Token, Toast.LENGTH_SHORT).show();
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
    //yeori
    public static void pushFCMNotification(String userDeviceIdKey, String type, String title, String s1_p1) throws Exception{

        String authKey = AUTH_KEY_FCM; // You FCM AUTH key
        String FMCurl = API_URL_FCM;

        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization","key="+authKey);
        conn.setRequestProperty("Content-Type","application/json");

        String input = "{\"notification\" : {\"title\" : \"여기다가 제목 넣기\", \"body\" : \"여기다 내용 넣기\"}, \"to\":\"/topics/ALL\"}";


//        String input = "{\"notification\" : {\"title\" : \"여기다가 제목 넣기\"" +
//                ", \"body\" : \""+title+"\"}" +
//                ", \"to\":\"/topics/ALL\"}";

//        String input = "{\"notification\" : {\"title\" : \"" +type+
//                "upload\", " +
//                "\"body\" : \"" +title+
//                "\"}, " +s1_p1+
//                "\"to\":\"/topics/ALL\"}";

        OutputStream os = conn.getOutputStream();

        // 서버에서 날려서 한글 깨지는 사람은 아래처럼  UTF-8로 인코딩해서 날려주자
        os.write(input.getBytes("UTF-8"));
        os.flush();
        os.close();

        int responseCode=conn.getResponseCode();
        Log.i(TAG, responseCode+"");
        BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response=new StringBuffer();

        Log.i(TAG, response.toString());
        Log.i(TAG, "전송완료");
    }
}
