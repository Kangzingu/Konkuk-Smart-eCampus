package kksy.konkuk_smart_ecampus;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText editTextUserId;
    EditText editTextUserPw;
    Button buttonLogin;
    CheckBox checkBoxAutoLogin;

    String userId = "defaultId";
    String userPw = "defaultPw";
    Boolean isAutoLogin = false;

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
                /*
                로그인 버튼 클릭 시, userId가 DB에 존재하는지 확인 후, userPw와 비교
                */

                isAutoLogin = checkBoxAutoLogin.isChecked();
                if(isAutoLogin){
                    /*
                    자동 로그인을 선택하였을 때 실행 되는 코드
                     */
                }
                else{
                    /*
                    자동 로그인을 선택하지 않았을 때 실행 되는 코드
                     */
                }

                // 계정확인이 완료되면, userId 정보를 갖고 MainAcitivty로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });
    }
}
