package kksy.konkuk_smart_ecampus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText editTextUserId;
    EditText editTextUserPw;
    Button buttonLogin;
    CheckBox checkBoxAutoLogin;

    String userId = "defaultId";
    String userPw = "defaultPw";
    Boolean isAutoLogin = false;

    Boolean loginChecked=false;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ActionBar 숨기기
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        editTextUserId = findViewById(R.id.editTextUserId);
        editTextUserPw = findViewById(R.id.editTextUserPw);
        buttonLogin = findViewById(R.id.buttonLogin);
        checkBoxAutoLogin = findViewById(R.id.checkBoxAutoLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = editTextUserId.getText().toString();
                userPw = editTextUserPw.getText().toString();
                /*
                로그인 버튼 클릭 시, userId가 DB에 존재하는지 확인 후, userPw와 비교
                */
                autoLogin();
//                isAutoLogin = checkBoxAutoLogin.isChecked();
//                if(isAutoLogin){
//                    /*
//                    자동 로그인을 선택하였을 때 실행 되는 코드
//                     */
//                }
//                else{
//                    /*
//                    자동 로그인을 선택하지 않았을 때 실행 되는 코드
//                     */
//                }

//                // 계정확인이 완료되면, userId 정보를 갖고 MainAcitivty로 이동
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("userId", userId);
//                startActivity(intent);
//                finish();
            }
        });
    }
    public void autoLogin(){
        pref=getSharedPreferences("pref", 0);

        // autoLogin이 체크되어있을 경우, 값을 가져온다.
        if (pref.getBoolean("autoLogin", false)) {
            editTextUserId.setText(pref.getString("id", ""));
            editTextUserPw.setText(pref.getString("pw", ""));
            checkBoxAutoLogin.setChecked(true);
            // goto mainActivity
            // 계정확인이 완료되면, userId 정보를 갖고 MainAcitivty로 이동
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();
        } else {
            // autoLogin이 체크되어 있지 않을 경우

            String id = editTextUserId.getText().toString();
            String password = editTextUserPw.getText().toString();
            Boolean validation = loginValidation(id, password);

            if(validation) {
                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                // id와 비밀번호를 DB에 저장하도록 한다.

                if(loginChecked) {
                    //autologin이 체크되어 있을 경우에는, 값을 저장한다.
                    editor.putString("id", id);
                    editor.putString("pw", password);
                    editor.putBoolean("autoLogin", true);
                    editor.commit();
                }
                // goto mainActivity
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);


            } else {
                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                // goto LoginActivity
            }
        }
        // set checkBoxListener
        //자동로그인 체크박스의 값을 받아오는 리스너 생성.
        checkBoxAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //자동 로그인이 check 되어 있으면 true, 아니면 preference에 저장된 모든 값들을 삭제한다.
                if(isChecked) {
                    loginChecked = true;
                } else {
                    // 자동로그인이 체크되어 있지 않다면 모두 remove한다.
                    loginChecked = false;
                    editor.clear();
                    editor.commit();
                }
            }
        });
    }
    //올바른 아이디와 비밀번호가 입력되는지 확인하는 기능
    //아이디와 비밀번호가 일치하는지 확인하는 함수이다.
    private boolean loginValidation(String id, String password) {
        if(pref.getString("id","").equals(id) && pref.getString("pw","").equals(password)) {
            // login success, 로그인 성공 시
            return true;
        } else if (pref.getString("id","").equals(null)){
            // sign in first, 가입 처음할 때
            Toast.makeText(LoginActivity.this, "Please Sign in first", Toast.LENGTH_LONG).show();
            return false;
        } else {
            // login failed, 로그인 실패시
            return false;
        }
    }
}
