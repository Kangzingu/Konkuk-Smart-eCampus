package kksy.konkuk_smart_ecampus;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.SystemRequirementsChecker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
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
    Boolean userIsBeaconCheck;
    ArrayList<Subject> userSubjectList;
    List<ExpandableListAdapter.Item> data;
    ExpandableListAdapter adapter;

    Toolbar toolbar;
    NavigationView navigationView;
    SwitchCompat switchBeacon;
    View contentMain;
    View menuClass;
    RecyclerView recyclerView;
    MenuItem nowMenuItem;
    LinearLayout btnAttendance;

    String nowClass;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private BackPressCloseHandler backPressCloseHandler;

    //Zingu
    Switch beaconSwitch;
    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    //

    //YEORI
    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;
    Query query;
    Handler handler=null;
    Thread t;
    Query query2;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

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

        mdatabase = FirebaseDatabase.getInstance();
        handler=new Handler();



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
            userId = "201611210"/*student.getStudentID()*/; //자동로그인에서 mainActivity로 넘어갈 때 intent로 학번을 줄 생각임(열)
            userEmail = userId + "@" + getResources().getString(R.string.konkuk_email);
            userImgURL = null;/*student.getImgURL()*/;
            userIsBeaconCheck = false;/*student.isBeconCheck()*/;

            mdbRef=mdatabase.getReference("student");
            query = mdbRef.orderByChild("studentID").equalTo(userId);
            Log.i("query log 검사", query.toString());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Student student=snapshot.getValue(Student.class);//객체 담기
                        Log.i("student 검사", student.getStudentName());
                        userName = student.getStudentName();
                        userId = student.getStudentID();
                        userEmail = userId + "@" + getResources().getString(R.string.konkuk_email);
                        userImgURL = student.getImgURL();
                        userIsBeaconCheck = student.isBeconCheck();

                        t=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //UI작업 수행
                                        profileName.setText(userName);
                                        profileEmail.setText(userEmail);
                                    }
                                });
                            }
                        });
                        t.start();
                    }

                }

                @Override public void onCancelled(DatabaseError databaseError) {
                }
            });

        }
        else{
            // 현재 로그인한 사용자가 교수일 경우 다음 수행
        }
    }

    public void initSwitchBeacon(){
        BeaconConnect beaconConnect=(BeaconConnect) getApplication();
        beaconConnect.SetUserID(userId);
        // Beacon Switch 초기 설정
        switchBeacon = (SwitchCompat) findViewById(R.id.switchBeacon);
        switchBeacon.setChecked(userIsBeaconCheck);
        if(userIsBeaconCheck==true){
            //디비에 있는 사용자 설정값 따라 첫 구동시 블루투스 온오프
            bluetoothAdapter.enable();
        }
        else{
            bluetoothAdapter.disable();
        }
        switchBeacon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                //스위치 변경 시
                if(isChecked){
                    Snackbar.make(buttonView, getResources().getText(R.string.beacon_on), Snackbar.LENGTH_SHORT)
                            .setAction("Beacon On", null).show();
                    bluetoothAdapter.enable();
                }//블루투스 켜기
                else{
                    Snackbar.make(buttonView, getResources().getText(R.string.beacon_off), Snackbar.LENGTH_SHORT)
                            .setAction("Beacon Off", null).show();
                    bluetoothAdapter.disable();
                }//블루투스 끄기
                mdbRef=mdatabase.getReference("student");
                query = mdbRef.orderByChild("studentID").equalTo(userId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            snapshot.child("beconCheck").getRef().setValue(isChecked);
                            //디비의 블루투스(비콘) 허용 여부 설정 또한 같이 업데이트
                        }

                    }

                    @Override public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });
    }

    public void initSubjectList(){
        Log.d("Firebase-test", "initSubjectList1");
        // 수강 과목 List 초기 설정
        userSubjectList = new ArrayList<>();

        /*
        - 여리
        현재 로그인한 student가 수강하는 강의 목록 추가
        userSubjectList : Student가 수강하는 강의 목록 -> Subject 객체 생성을 통해 등록
        Subject : Subject(과목번호, 강의이름)으로 객체 생성
         */
        mdbRef=mdatabase.getReference("sugang");
        query = mdbRef.orderByChild("studentID").equalTo(userId);

        Log.i("sugang", query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Sugang sugang=snapshot.getValue(Sugang.class);
                    String subID=sugang.getSubID();
                    mdbRef=mdatabase.getReference("subject");
                    query2=mdbRef.orderByChild("subID").equalTo(subID);

                    /*(열)
                    * 이중 쿼리를 쓴 이유는
                    * 1) 학생이 수강하고 있는 과목을 알려면, 학번으로 sugang에 접근 한 뒤 subID를 알아내고,
                    * 2) 이를 가지고 subject의 subName에 접근해야 하기 때문.*/

                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot1:dataSnapshot.getChildren()){
                                //subName : 졸프, subID : s1
                                //Log.i("subject", snapshot1.getValue().toString());
                                Subject subject=snapshot1.getValue(Subject.class);
                                userSubjectList.add(subject);
                            }

                            /*
                            * 자영
                            * 동기식으로 해결하고 싶다면 for문 밖의 이곳에 넣으면 됩니당.
                            * 이미 userSubjectList에는 요소가 들어있는 상태임.
                            * adapter.notifyDataSetChanged();
                            */

                            // 수강 목록을 수강 번호로 정렬
                            Collections.sort(userSubjectList, new Comparator<Subject>() {
                                @Override
                                public int compare(Subject o1, Subject o2) {
                                    return o1.getSubID().compareTo(o2.getSubID());
                                }
                            });

                            data.add(new ExpandableListAdapter.Item(HEADER, getResources().getString(R.string.sugang_title)));
                            for(int i=0; i<userSubjectList.size(); i++){
                                data.add(new ExpandableListAdapter.Item(CHILD, userSubjectList.get(i).getSubName(), userSubjectList.get(i).getSubID()));
                            }

                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    Log.i("sugang", snapshot.getValue().toString());

                }

            }

            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });
        //비동기 문제 해결 할 때에는 이거 주석처리 해놓고 하시면 될 것 같아용(열)
        userSubjectList.add(new Subject("s2", "산학협력프로젝트2(종합설계)"));
//        userSubjectList.add(new Subject("2222", "과학사"));
//        userSubjectList.add(new Subject("1111", "클라우드웹서비스"));
        userSubjectList.add(new Subject("s3", "졸업프로젝트2(종합설계)"));

        recyclerView = (RecyclerView) findViewById(R.id.listviewSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        data = new ArrayList<>();

        adapter = new ExpandableListAdapter(data);
        adapter.setItemClick(new ExpandableListAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle); // toolbar title
                toolbarTitle.setText(data.get(position).text); // toolbar title을 선택한 강의명으로 바꿈

                menuClass.setVisibility(View.VISIBLE); // 강의실 menu 목록을 보이게 함
                nowClass = data.get(position).number;

                fragmentTransaction = fragmentManager.beginTransaction();
                /*
                - 여리
                바로 밑에 있는 코드가 MainActivity에서 ClassFragment로 파라미터 넘기는 부분
                ClassFragment.newInstance(data.get(position).number)) -> 여기서 넘겨짐

                현재 과목의 수강번호가 넘겨지도록 설정.

                만약, 다른 방법으로 바꾸고 싶다면 여기서 파라미터 넘기는 부분을 수정하고,
                ClassFragment 에서 따로 주석 설명 해둔곳을 수정해야 함.
                 */
                fragmentTransaction.replace(R.id.fragmentContainer, ClassFragment.newInstance(data.get(position).number));
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

        // 강의실 입장 시 보이는 Menu 초기 설정
        initClassMenu();
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

    public void initClassMenu(){
        menuClass = findViewById(R.id.menu_classroom);
        btnAttendance = findViewById(R.id.btn_attendance);

        menuClass.setVisibility(View.GONE);
        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClass){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, AttendanceFragment.newInstance(nowClass, userId));
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(fragmentManager.getBackStackEntryCount() == 0){
            backPressCloseHandler.onBackPressed();
        }
        else{
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
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

        menuClass.setVisibility(View.GONE); // 강의실이 아닌 다른 곳일 경우 강의실 Menu를 보이지 않게 함

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

    public void getStudent(){

        mdbRef=mdatabase.getReference("student");
        query = mdbRef.orderByChild("studentID").equalTo("201611210");
        Log.i("query log 검사", query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Student student=snapshot.getValue(Student.class);//객체 담기
                    Log.i("student 검사", student.getStudentName());
                    userName = student.getStudentName();
                    userId = student.getStudentID();
                    userEmail = student.getPotalID() + "@" + getResources().getString(R.string.konkuk_email);
                    userImgURL = student.getImgURL();
                    userIsBeaconCheck = student.isBeconCheck();

                    t=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //UI작업 수행
                                    profileName.setText(userName);
                                    profileEmail.setText(userEmail);
                                }
                            });
                        }
                    });
                    t.start();
                }

            }

            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
