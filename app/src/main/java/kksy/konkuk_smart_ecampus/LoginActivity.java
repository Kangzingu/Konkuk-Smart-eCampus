package kksy.konkuk_smart_ecampus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText editTextUserId;
    EditText editTextUserPw;
    Button buttonLogin;

    String userId = "defaultId";
    String userPw = "defaultPw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserId = findViewById(R.id.editTextUserId);
        editTextUserPw = findViewById(R.id.editTextUserPw);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                로그인 버튼 클릭 시, userId가 DB에 존재하는지 확인 후, userPw와 비교
                */

                // 계정확인이 완료되면, userId 정보를 갖고 MainAcitivty로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }
}
