package kksy.konkuk_smart_ecampus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class IntroActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Boolean autoLoginCheck=false;

    Handler handler=new Handler();
    Runnable r=new Runnable() {
        @Override
        public void run() {
            //몇초 뒤에 다음화면으로 넘어가기 Handler 사용
            if(!autoLoginCheck) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);  //다음화면으로 넘어가기
                finish();
            }else{
                //autologin이 되어있을 경우
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);  //다음화면으로 넘어가기
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // ActionBar 숨기기
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        pref=getSharedPreferences("pref", 0);

        if(pref.getString("id","").equals(null)||pref.getString("pw", "").equals(null)){
            //가입을 처음 하거나, id랑 pw를 자동로그인 하지 않았을 경우
            //autologin이 되어 있지 않을 경우
            autoLoginCheck=false;
        }else{
            //autologin이 되어 있을 경우
            autoLoginCheck=true;
        }

    }

    //올바른 아이디와 비밀번호가 입력되는지 확인하는 기능
    //아이디와 비밀번호가 일치하는지 확인하는 함수이다.
    private boolean loginValidation(String id, String password) {
        if(pref.getString("id","").equals(id) && pref.getString("pw","").equals(password)) {
            // login success, 로그인 성공 시
            return true;
        } else if (pref.getString("id","").equals(null)){
            // sign in first, 가입 처음할 때
            Toast.makeText(this, "Please Sign in first", Toast.LENGTH_LONG).show();
            return false;
        } else {
            // login failed, 로그인 실패시
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //다음 화면에 들어와있을 때 예약 걸어주기
        handler.postDelayed(r, 4000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //화면을 벗어나면, handler에 예약해놓은 작업을 취소.
        handler.removeCallbacks(r); //예약 취소
    }
}
