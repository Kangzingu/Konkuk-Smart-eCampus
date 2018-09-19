package kksy.konkuk_smart_ecampus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//각자 기능을 수행할 수 있도록 버튼을 누르면 특정 엑티비티로 이동함

public class TempActivity extends AppCompatActivity {

    private Button gLoginActivity;
    private Button gAdminActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        gLoginActivity=(Button)findViewById(R.id.gotoLoginActivity);
        gAdminActivity=(Button)findViewById(R.id.gotoAdminActivity);
    }

    public void gotoLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoAdminActivity(View view) {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        finish();
    }
}
