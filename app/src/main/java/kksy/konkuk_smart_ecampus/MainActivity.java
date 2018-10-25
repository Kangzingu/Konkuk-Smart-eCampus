package kksy.konkuk_smart_ecampus;

import android.bluetooth.BluetoothAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.SystemRequirementsChecker;

import java.util.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static kksy.konkuk_smart_ecampus.ExpandableListAdapter.CHILD;
import static kksy.konkuk_smart_ecampus.ExpandableListAdapter.HEADER;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView profileName;
    TextView profileEmail;
    ImageView profileImg;

    Boolean isStudent = true;
    Boolean isClass = false;

    String userName;
    String userId;
    String userEmail;
    String userImgURL;
    Boolean userIsBeaconOn;
    ArrayList<Subject> userSubjectList;
    ExpandableListAdapter adapter;

    Toolbar toolbar;
    NavigationView navigationView;
    SwitchCompat switchBeacon;
    View contentMain;
    RecyclerView recyclerView;
    MenuItem nowMenuItem;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //==
    Switch beaconSwitch;
    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    //==
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
            - 여리
            현재 로그인한 Student의 정보 가져와서 각 정보에 연결
            userName : student 이름
            userId : student 포탈 아이디
            userEmail : student의 건국 웹메일
            userImgURL : student의 프로필 사진
            userIsBeaconOn : student의 beacon 설정 여부
            */

            userName = "default"/*student.getStudentName()*/;
            userId = "default"/*student.getStudentID()*/;
            userEmail = userId + "@" + getResources().getString(R.string.konkuk_email);
            userImgURL = null;/*student.getImgURL()*/;
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
        if(userIsBeaconOn==true){
            bluetoothAdapter.enable();
        }
        else{
            bluetoothAdapter.disable();
        }
        switchBeacon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Snackbar.make(buttonView, getResources().getText(R.string.beacon_on), Snackbar.LENGTH_SHORT)
                            .setAction("Beacon On", null).show();
                    /*
                    - 진구
                    Beacon Switch를 On 하였을 경우 다음 수행
                     */
                    bluetoothAdapter.enable();
                    //DB드가서 사용자의 비콘 온오프 여부 바꺼야해
                }
                else{
                    Snackbar.make(buttonView, getResources().getText(R.string.beacon_off), Snackbar.LENGTH_SHORT)
                            .setAction("Beacon Off", null).show();
                    /*
                    - 진구
                    Beacon Switch를 Off 하였을 경우 다음 수행
                     */
                    bluetoothAdapter.disable();
                    //DB드가서 사용자의 비콘 온오프 여부 바꺼야해
                }
            }
        });
    }

    public void initSubjectList(){
        // 수강 과목 List 초기 설정
        userSubjectList = new ArrayList<>();

        /*
        - 여리
        현재 로그인한 student가 수강하는 강의 목록 추가
        userSubjectList : Student가 수강하는 강의 목록 -> Subject 객체 생성을 통해 등록
        Subject : Subject(과목번호, 강의이름)으로 객체 생성
         */

        userSubjectList.add(new Subject("0000", "산학협력프로젝트2(종합설계)"));
        userSubjectList.add(new Subject("2222", "과학사"));
        userSubjectList.add(new Subject("1111", "클라우드웹서비스"));
        userSubjectList.add(new Subject("3333", "졸업프로젝트2(종합설계)"));

        // 수강 목록을 수강 번호로 정렬
        Collections.sort(userSubjectList, new Comparator<Subject>() {
            @Override
            public int compare(Subject o1, Subject o2) {
                return o1.getSubID().compareTo(o2.getSubID());
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.listviewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final List<ExpandableListAdapter.Item> data = new ArrayList<>();

        /*
        - 여리
        data는 ExpandableListAdapter 적용을 위한 List 이므로 만지지 않아도 됨
         */

        data.add(new ExpandableListAdapter.Item(HEADER, getResources().getString(R.string.sugang_title)));
        for(int i=0; i<userSubjectList.size(); i++){
            data.add(new ExpandableListAdapter.Item(CHILD, userSubjectList.get(i).getSubName(), userSubjectList.get(i).getSubID()));
        }

        adapter = new ExpandableListAdapter(data);
        adapter.setItemClick(new ExpandableListAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle); // toolbar title
                toolbarTitle.setText(data.get(position).text); // toolbar title을 선택한 강의명으로 바꿈

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, ClassFragment.newInstance(data.get(position).text));
                fragmentTransaction.commit();
                if(isClass){ // 현재 화면이 강의실일 경우
                    String headerSubjectName = data.get(position).text;
                    String headerSubjectNum = data.get(position).number;
                    data.clear();
                    data.add(0, new ExpandableListAdapter.Item(HEADER, headerSubjectName)); // header를 sugang title로 변경
                    data.get(0).invisibleChildren = new ArrayList<>();
                    for(int i=0; i<userSubjectList.size(); i++){
                        if(!headerSubjectNum.equals(userSubjectList.get(i).getSubID())){
                            data.get(0).invisibleChildren.add(new ExpandableListAdapter.Item(CHILD, userSubjectList.get(i).getSubName(), userSubjectList.get(i).getSubID()));
                        }
                    }
                }
                else{ // 현재 화면이 HOME 일 경우
                    data.clear();
                    data.add(0, new ExpandableListAdapter.Item(HEADER, userSubjectList.get(position - 1).getSubName()));
                    data.get(0).invisibleChildren = new ArrayList<>();
                    for(int i=0; i<userSubjectList.size(); i++){
                        if(i != position - 1){
                            data.get(0).invisibleChildren.add(new ExpandableListAdapter.Item(CHILD, userSubjectList.get(i).getSubName(), userSubjectList.get(i).getSubID()));
                        }
                    }
                    isClass = true;
                }

                adapter.notifyDataSetChanged();

                nowMenuItem.setChecked(false);
                recyclerView.setBackgroundColor(getResources().getColor(R.color.colorMenu));

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        recyclerView.setAdapter(adapter);
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
        - 여리
        프로필 이미지 설정 코드 주석만 해제해주면 됨 (아마도?)
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
        nowMenuItem = item;
        recyclerView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        //classMenuItem.setChecked(false);
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

        isClass = false;
        initSubjectList();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        nowMenuItem = menu.findItem(R.id.nav_home);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onResume(){
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

    }
    protected void onPause(){
        super.onPause();
    }
}
