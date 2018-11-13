package kksy.konkuk_smart_ecampus;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static kksy.konkuk_smart_ecampus.AttendanceContentsAdapter.ABSENT;
import static kksy.konkuk_smart_ecampus.AttendanceContentsAdapter.ATTEND;
import static kksy.konkuk_smart_ecampus.AttendanceContentsAdapter.LATE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SUBNUM = "subject number";
    private static final String ARG_STDID = "student id";

    // TODO: Rename and change types of parameters
    /*
    - 진구

    우선은 AttendanceFragment로 넘어올 때, 과목과 학생정보만 가져오도록 함
    혹시나 더 필요한게 있으면 자영에게 문의

    - subNum : 과목 아이디 (수강신청 할 때 쓰는 4자리 과목 번호)
    - stdId : 학생 아이디
     */
    private String subNum;
    private String stdId;
    private String profId;
    AttendanceListAdapter adapter;
    RecyclerView recyclerView;
    List<AttendanceListAdapter.Item> data;

    //징구의 디비 가져오기
    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;
    Query query;
    DataSnapshot currentObject;
    int countAttendance;
    ArrayList<AttendanceContentsAdapter.Item> contents;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBNUM, param1);
        args.putString(ARG_STDID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //subNum = getArguments().getString(ARG_SUBNUM);
            //지금 s2인데 출석부에 없어서 s1으로 일단 바꿔놓을게욤
            subNum = "s184325";
            stdId = getArguments().getString(ARG_STDID);
            //이거 바꿔야해용 이 과목의 프로페서 아이디가 필요해욤
            profId = "p25787542";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.listViewAttendance);

        initAttendance();

        return view;
    }

    public void initAttendance() {
        data = new ArrayList<>();
        contents = new ArrayList<>();
        mdatabase = FirebaseDatabase.getInstance();
        mdbRef = mdatabase.getReference("attendance");
        //출석 가져왔죵
        //        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        //        ////////////////////////////////////////// 다슬 subNum에 있졍 //////////////////////////////////////////
        //        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        query = mdbRef.orderByKey().equalTo(subNum + "-" + profId);
        countAttendance = 0;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                boolean isConditionOk = false;
                while (iter.hasNext()) {
                    currentObject = iter.next();
                    if (currentObject.hasChild(stdId)) {
                        iter = currentObject.child(stdId).getChildren().iterator();
                        isConditionOk = true;
                        break;
                    }
                }
                while (iter.hasNext() && isConditionOk) {
                    //내 학번에 해당하는 애 정보를 성공적으로 가져왔을때
                    currentObject = iter.next();
                    Iterator<DataSnapshot> childIter = currentObject.getChildren().iterator();
                    while (childIter.hasNext()) {
                        currentObject = childIter.next();
                        countAttendance++;
                        int state;
                        switch (currentObject.child("state").getValue().toString()) {
                            case "출석":
                                state = ATTEND;
                                break;
                            case "지각":
                                state = LATE;
                                break;
                            case "결석":
                                state = ABSENT;
                                break;
                            default:/*이거 바꺼야해용*/
                                state = ABSENT;
                                break;
                        }
                        contents.add(new AttendanceContentsAdapter.Item(state, currentObject.child("date").getValue().toString()));
                    }
                }
                showAttendance();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        - 진구

        출석부 데이터가 들어가야 할 곳은 여기!
        출석부 UI는 RecyclerView 안에 RecyclerView 가 있는 이중적인 구조임
        따라서, Vertical 한 RecyclerView 안에 Horizontal 한 RecyclerView 가 있음

        - data : Vertical 한 RecyclerView 안에 들어가는 데이터 ArrayList (1주차, 2주차 같은거에 해당)
            data 에 AttendanceListAdapter.Item 객체가 들어감
        - AttendanceListAdapter.Item : 몇 주차 인지에 대한 정보(attendance_week)와 그 주차의 출석 정보 ArrayList(attendance_contents)를 포함함
            attendance_contents 에 AttendanceContentsAdapter.Item 객체가 들어감
        - AttendanceContentsAdapter.Item : 출석 날짜(date)와 그 날짜의 출석 여부(state)
            state 는 ATTEND(출석), LATE(지각), ABSENT(결석)으로 구분
         */
//        data = new ArrayList<>();
//
//        ArrayList<AttendanceContentsAdapter.Item> contents1 = new ArrayList<>();
//        contents1.add(new AttendanceContentsAdapter.Item(ATTEND, "2018.09.03(월)"));
//        contents1.add(new AttendanceContentsAdapter.Item(LATE, "2018.09.05(수)"));
//        data.add(new AttendanceListAdapter.Item(1, contents1));
//
//        ArrayList<AttendanceContentsAdapter.Item> contents2 = new ArrayList<>();
//        contents2.add(new AttendanceContentsAdapter.Item(ATTEND, "2018.09.10(월)"));
//        contents2.add(new AttendanceContentsAdapter.Item(ATTEND, "2018.09.12(수)"));
//        data.add(new AttendanceListAdapter.Item(2, contents2));
//
//        ArrayList<AttendanceContentsAdapter.Item> contents3 = new ArrayList<>();
//        contents3.add(new AttendanceContentsAdapter.Item(ATTEND, "2018.09.17(월)"));
//        contents3.add(new AttendanceContentsAdapter.Item(ABSENT, "2018.09.19(수)징구쨩"));
//        data.add(new AttendanceListAdapter.Item(3, contents3));
         }

    public void showAttendance() {

        ArrayList<AttendanceContentsAdapter.Item> weeklyContents[] = new ArrayList[countAttendance/2];
        for(int i=0;i<countAttendance/2;i++) {
            weeklyContents[i]=new ArrayList<>();
        }
        for(int i=0;i<countAttendance;i++){
            weeklyContents[i/2].add(contents.get(i));
        }
        for(int i=0;i<countAttendance/2;i++){
            data.add(new AttendanceListAdapter.Item(i+1, weeklyContents[i]));
        }
        //data.add(new AttendanceListAdapter.Item(countAttendance, contents));
        adapter = new AttendanceListAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
        //Toast.makeText(getActivity(), currentObject.getKey().toString(), Toast.LENGTH_SHORT).show();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new AttendanceListAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
    }
}