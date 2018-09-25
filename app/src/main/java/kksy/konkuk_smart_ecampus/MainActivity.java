package kksy.konkuk_smart_ecampus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView profileName;
    TextView profileEmail;

    String userName;
    String userId;
    String userEmail;

    //==
    Switch beaconSwitch;
    //==
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Navigation Drawer 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // headerView의 사용자 이름과 이메일을 수정하기 위해 navigationView의 headerView를 가져옴
        View view = navigationView.getHeaderView(0);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userName = "defaultName";
        /*
        해당 userId의 이름을 DB에서 가져와 userName에 저장
         */
        userEmail = userId + "@" + getResources().getString(R.string.konkuk_email);

        // 프로필에 이름과 이메일 설정
        profileName = (TextView) view.findViewById(R.id.profile_name);
        profileEmail = (TextView) view.findViewById(R.id.profile_email);

        profileName.setText(userName);
        profileEmail.setText(userEmail);
        //==================
        beaconSwitch = (Switch) findViewById(R.id.switch1);
        beaconSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){//비콘 허용 on
                    //db드가서 허용 했다고 바꾸고
                    //현재 시간& 유저 정보 이용해서 과목 뭔지 알아내고
                    //이 과목 수업 하는 강의실 비콘 정보 알아내공
                    //이 비콘을 연결한다
                }
                else{//b==false //비콘 허용 off

                }
                Toast.makeText(MainActivity.this,"d"+b,Toast.LENGTH_SHORT).show();
            }
        });

        //==================

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_mail) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
