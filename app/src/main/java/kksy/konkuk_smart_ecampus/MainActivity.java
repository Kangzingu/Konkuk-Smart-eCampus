package kksy.konkuk_smart_ecampus;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView profileName;
    TextView profileEmail;
    ImageView profileImg;

    Boolean isStudent = true;

    String userName;
    String userId;
    String userEmail;
    String userImgURL;
    Boolean userIsBeaconOn;
    ArrayList<Subject> userSubjectList;

    Toolbar toolbar;
    NavigationView navigationView;
    SwitchCompat switchBeacon;
    View contentMain;
    RecyclerView recyclerView;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUserInformation();
        initNavigationView();

        initContentMain();
    }

    public void initUserInformation(){
        // 사용자 정보 초기 설정
        Intent intent = getIntent();
        /*
        Intent로 사용자 객체 넘겨 줌
         */

        if(isStudent){
            // 현재 로그인한 사용자가 학생일 경우 다음 수행

            Student student = new Student();
            /*
            DB에서 Student 객체 가져와야 함
            */

            userName = "default"/*student.getStudentName()*/;
            userId = "default"/*student.getStudentID()*/;
            userEmail = userId + "@" + getResources().getString(R.string.konkuk_email);
            userImgURL = student.getImgURL();
            userIsBeaconOn = true/*student.isBeconCheck()*/;
        }
        else{
            // 현재 로그인한 사용자가 교수일 경우 다음 수행
        }
    }

    public void initSwitchBeacon(){
        // Beacon Switch 초기 설정
        switchBeacon = (SwitchCompat) findViewById(R.id.switchBeacon);
        switchBeacon.setChecked(userIsBeaconOn);

        switchBeacon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Snackbar.make(buttonView, getResources().getText(R.string.beacon_on), Snackbar.LENGTH_SHORT)
                            .setAction("Beacon On", null).show();
                    /*
                    Beacon Switch를 On 하였을 경우 다음 수행
                     */
                }
                else{
                    Snackbar.make(buttonView, getResources().getText(R.string.beacon_off), Snackbar.LENGTH_SHORT)
                            .setAction("Beacon Off", null).show();
                    /*
                    Beacon Switch를 Off 하였을 경우 다음 수행
                     */
                }
            }
        });
    }

    public void initSubjectList(){
        // 수강 과목 List 초기 설정
        userSubjectList = new ArrayList<>();

        /*
        userSugangList에 사용자가 수강 또는 강의하는 과목들 추가
         */

        userSubjectList.add(new Subject("1234", "산학협력프로젝트2(종합설계)"));
        userSubjectList.add(new Subject("1234", "과학사"));
        userSubjectList.add(new Subject("1234", "클라우드웹서비스"));
        userSubjectList.add(new Subject("1234", "졸업프로젝트2(종합설계)"));

//        RecyclerView recyclerView;
//        RecyclerView.LayoutManager layoutManager;
//        SubjectListAdapter adapter;
//        recyclerView = (RecyclerView) findViewById(R.id.listviewSubjects);
//        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new SubjectListAdapter(userSubjectList);
//        recyclerView.setAdapter(adapter);

        recyclerView = (RecyclerView) findViewById(R.id.listviewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, getResources().getString(R.string.sugang_title)));
        for(int i=0; i<userSubjectList.size(); i++){
            /*
            Subject 클래스에 수강번호 추가해서 적용
            */
            data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, userSubjectList.get(i).getSubName(), "1234"));
        }

        recyclerView.setAdapter(new ExpandableListAdapter(data));
    }

    public void initNavigationView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Toolbar Title 설정
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Navigation Drawer 초기 설정
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Navigation Header 초기 설정
        initNavigationHeader();

        // Beacon Switch 초기 설정
        initSwitchBeacon();

        // 수강 과목 List 초기 설정
        initSubjectList();
    }

    public void initNavigationHeader(){
        // Navigation Drawer의 Header 부분 설정
        // headerView의 사용자 이름과 이메일을 수정하기 위해 navigationView의 headerView를 가져옴
        View view = navigationView.getHeaderView(0);

        // 프로필에 이름과 이메일 설정
        profileName = (TextView) view.findViewById(R.id.profile_name);
        profileEmail = (TextView) view.findViewById(R.id.profile_email);
        profileImg = (ImageView) view.findViewById(R.id.profile_image);

        profileName.setText(userName);
        profileEmail.setText(userEmail);
        /*
        프로필 이미지 설정
        profileImg.setImageURI(Uri.parse(userImgURL));
        */
    }

    public void initContentMain(){
        // Main Content 초기 설정
        View view = findViewById(R.id.app_bar_main);
        contentMain = view.findViewById(R.id.content_main);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, new HomeFragment());
        fragmentTransaction.commit();
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
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);

        int id = item.getItemId();

        fragmentTransaction = fragmentManager.beginTransaction();

        if (id == R.id.nav_home) {
            toolbarTitle.setText(R.string.home_title);
            fragmentTransaction.replace(R.id.fragmentContainer, new HomeFragment());
        } else if (id == R.id.nav_message) {
            toolbarTitle.setText(R.string.message_title);
            fragmentTransaction.replace(R.id.fragmentContainer, new MessageFragment());
        } else if (id == R.id.nav_settings) {
            toolbarTitle.setText(R.string.settings_title);
            fragmentTransaction.replace(R.id.fragmentContainer, new SettingsFragment());
        }

        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
