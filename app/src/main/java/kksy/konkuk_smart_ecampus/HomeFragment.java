package kksy.konkuk_smart_ecampus;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static kksy.konkuk_smart_ecampus.ExpandableListAdapter.CHILD2;
import static kksy.konkuk_smart_ecampus.ExpandableListAdapter.HEADER;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    /*
    (다슬 수정)
     */
    ArrayList<TimeLine> timeLines = new ArrayList<TimeLine>();
    HashMap<String, String> boardID = new HashMap();//key="강의자료","과제","공지"
    Lecture lecture;
    String sugang_subject_ID = "s2"; //현재 검색하려는 과목 리스트
    String now_StudentID = "201611233";//현재 로그인 한 학생 ID
    public String sugang_professor_ID = "noinfo";//다슬이가 불러와야됨->(여리요청 완료)//sugang_subject_ID가 s2라면, sugang_professor_ID는 무조건 p2


    CircleProgressBar circleProgressBar;

    RecyclerView recyclerView;
    TimelineListAdapter adapter;

    List<TimelineListAdapter.Item> timelineList;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Activity activity;
    AlertDialog.Builder builder;

    /*YEORI*/


    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;
    Query query;
    static boolean on = false;
    ArrayList<Board> boards = new ArrayList<Board>();
    HashMap<String, String> subjects = new HashMap();
    //String userID="201611210";  //MainActivity에서 HomeFragment로 접근할 때 넘겨줄 것(열->자영언니에게 말하기)
    HashMap<String, String> subject;    //MainActivity에서 HomeFragment로 접근할 때 넘겨줄 것(열->자영언니에게 말하기
    //수강과목은 여러개 일 수 있으니까 나중에는 배열로 고칠 것.

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mdatabase = FirebaseDatabase.getInstance();
        View view = inflater.inflate(R.layout.fragment_home, null);
        circleProgressBar = (CircleProgressBar) view.findViewById(R.id.progressBarNotice);
        recyclerView = (RecyclerView) view.findViewById(R.id.listViewTimeline);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        activity = getActivity();

        initProgressBar();
        initTimeline();
       // postTimeLine();
        return view;
    }


    public void initProgressBar(){
        circleProgressBar.setProgressWithAnimation(60);
    }

    /*
         (다슬 수정)
         : Lecture를 등록해서 사용해야 된다면, 이 함수를 이용하시오
          */
    public void regiLecture() {

        MyDBHandler myDBHandler = new MyDBHandler("lecture");
        List<String> str = Arrays.asList(new String[]{"08:00", "08:10"});
        List<String> str2 = Arrays.asList(new String[]{"08:11", "08:30"});

        Lecture lecture = new Lecture("p2", "s2", "beconinfo", str, str2);
        myDBHandler.newLecture(lecture);
    }

//    public void postTimeLine() {
//        findProfessorID();
//        findBoardID();
//        searchBoard();
//    }
    public void findBoardID() {

        /*
        (다슬 수정)
        1, sugang에서 s1과목을 듣는 학생 s1-201611210 정보를 가져옴
        2. 해당 학생의 timeLine 정보를 가져온 후
        3. boardID를 타입별(과제, 강의자료, 공지)로 모두 가지고옴
         */

        mdbRef = mdatabase.getReference("sugang");
        query = mdbRef.orderByChild("subID_studentID").equalTo(sugang_subject_ID + "-" + now_StudentID);//sugang_subject_ID+"-"+userID
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("HomeFragment", "call findBoardID()");
                if (dataSnapshot != null) {
                    Sugang sugang = dataSnapshot.getValue(Sugang.class);
                    TimeLine timeLine = sugang.getTimeLine();
                    //material
                    for (int i = 0; i < timeLine.getMaterials().size(); i++) {
                        TimeLineBoardFormat timeLineBoardFormat = timeLine.getMaterials().get(i);
                        boardID.put("강의자료", timeLineBoardFormat.getBoardID());
                        timeLines.add(timeLine);
                        //Log.i("HomeFragment : 강의자료",boardID.get("강의자료"));
                    }
                    //homework
                    for (int i = 0; i < timeLine.getHomework().size(); i++) {
                        TimeLineBoardFormat timeLineBoardFormat = timeLine.getHomework().get(i);
                        boardID.put("과제", timeLineBoardFormat.getBoardID());
                        timeLines.add(timeLine);
                        // Log.i("HomeFragment : 과제",boardID.get("과제"));
                    }
                    //notice
                    for (int i = 0; i < timeLine.getNotice().size(); i++) {
                        TimeLineBoardFormat timeLineBoardFormat = timeLine.getNotice().get(i);
                        boardID.put("공지", timeLineBoardFormat.getBoardID());
                        timeLines.add(timeLine);
                        //  Log.i("HomeFragment : 공지",boardID.get("공지"));
                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void findProfessorID() {
           /*
        (다슬 수정)
        - 여리 요청사항 : 과목 코드만으로 professor id 검색
         */

        mdbRef = mdatabase.getReference("lecture");
        query = mdbRef.orderByChild("subID").equalTo(sugang_subject_ID);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("HomeFragment", "call findProfessorID()");
                sugang_professor_ID = dataSnapshot.getKey().split("-")[0];
                //Log.i("HomeFragment",sugang_professor_ID);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void searchBoard() {
         /*
        (다슬 수정)
        1. board 릴레이션에 접근해서 과목ID 만으로 검색 진행
        2. 게시글을 타입(강의자료, 공지, 과제)별로 가져옴
         */

        mdbRef = mdatabase.getReference("board").child("s2-p2");
        query = mdbRef.orderByKey();//query=mdbRef.orderByKey().equalTo(sugang_subject_ID+"-"+sugang_professor_ID);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("HomeFragment", "call searchBoard()");
                //Log.i("HomeFragment","sugang_professor_ID :"+sugang_professor_ID);
                // Log.i("HomeFragment",dataSnapshot.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Board board = snapshot.getChildren().iterator().next().getValue(Board.class);
                    //Log.i("HomeFragment \n",board.getBoardID());
                    //boardid 비교
                    if (boardID.containsValue(board.getBoardID()) == true) {
                        // Log.i("HomeFragment \n", "true");
                        timelineList.add(new TimelineListAdapter.Item(
                                "0",
                                "산학협력프로젝트1",
                                board.getTitle(),
                                board.getUploadDate(),
                                false, false, board.getSubID_proID(),
                                board.getBoardID()
                        ));

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void initTimeline() {
        Log.d("HomeFragment", "1");
        timelineList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        ////////////////////////////////////////////////////////////////////////////////////////////
        //(다슬 수정)//
        findProfessorID();
        findBoardID();
        searchBoard();
        /////////////////////////////////////////////////////////////////////////////////////////////



        /*
        - 여리
        TimelineListAdapter.Item 객체 생성을 통해 Timeline의 Item들 생성
        생성한 Item들을 data에 추가

        - TimelineListAdapter.Item
            TimelineListAdapter.Item(강의명, 게시물 제목, 게시물 업로드 날짜, 게시물 확인 여부, 게시물 북마크 여부)
        - timelineList : Timeline의 List
         */

//        timelineList.add(new TimelineListAdapter.Item(
//                "0",
//                "산학협력프로젝트2(종합설계)",
//                "[10/2] 미팅 요약서",
//                "2018.10.02 오후 11:59",
//                false, false,"s1_p1",
//                "LP1JCk"
//        ));

//        timelineList.add(new TimelineListAdapter.Item(
//                "0",
//                "산학협력프로젝트2(종합설계)",
//                "[10/4] 2차 요구사항 분석서 제출",
//                "2018.10.05 오후 11:59",
//                false, true,"s1_p1",
//                "LP1JCk"
//        ));
//        timelineList.add(new TimelineListAdapter.Item(
//                "0",
//                "발명과특허",
//                "자기소개서",
//                "2018.10.07 오후 11:59",
//                true, false,"s1_p1",
//                "LP1JCk"
//        ));

        // timeline에서 보여질 item 정렬

        /*Collections.sort(data, sortByDate);*/
        Collections.sort(timelineList, sortByChecked);
        Collections.sort(timelineList, sortByBookmark);

        adapter = new TimelineListAdapter(timelineList);
        adapter.setItemPickClick(new TimelineListAdapter.TimelineItemPickClick() {
            @Override
            public void onPickClick(View view, int postion) {
                if (!timelineList.get(postion).isNotPick) {
                    timelineList.get(postion).isNotPick = true;

                    /*
                    - 여리
                    Bookmark 해제 시, bookmark 여부 DB에도 반영
                     */
//                    TimeLine timeLine;
//                    timeLine.getHomework().get(0).setIsread(true);
                } else {

                    timelineList.get(postion).isNotPick = false;

                    /*
                    - 여리
                    Bookmark 등록 시, bookmark 여부 DB에도 반영
                     */
                    DatabaseReference relation_table;
                    mdbRef = mdatabase.getReference("sugang");

                    String subid_proid = timelineList.get(postion).subid_proid;
                    String[] temp = subid_proid.split("_");

//                    relation_table=mdbRef.child(temp[0]+"-"+userID).child("timeLine").push();
//                    TimelineListAdapter.Item item=timelineList.get(postion);
//                    TimeLineBoardFormat temp2=new TimeLineBoardFormat();
//                    temp2.setBoardID(item.boardID);
//                    temp2.setIsread(item.isOpen);
//                    temp2.setWantTop(item.isCheck);
                    //relation_table.setValue()
                }

                Collections.sort(timelineList, sortByChecked);
                Collections.sort(timelineList, sortByBookmark);
                adapter.notifyDataSetChanged();
            }
        });


        adapter.setItemClick(new TimelineListAdapter.TimelineItemClick() {
            @Override
            public void onClick(View view, int position) {
                if (!timelineList.get(position).isCheck) { // 게시물 미확인 -> 게시물 확인
                    timelineList.get(position).isCheck = true;

                    /*
                    - 여리
                    게시물 확인 시, 게시물 확인 여부 DB에도 반영
                     */
                }
                adapter.notifyDataSetChanged();

                /*
                게시물 클릭 시, 해당 게시물 ID를 통해 게시물 fragment로 이동
                 */

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, PostFragment.newInstance(timelineList.get(position).boardID, timelineList.get(position).subid_proid));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Snackbar.make(view, "해당 게시물로 이동", Snackbar.LENGTH_SHORT)
                        .setAction("해당 게시물로 이동", null).show();
            }
        });

        adapter.setItemLongClick(new TimelineListAdapter.TimelineItemLongClick() {
            @Override
            public void onLongClick(final View view, final int position) {

                /*
                - 여리
                게시물 삭제 시, Timeline DB에서도 삭제
                 */

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getContext());
                }
                builder.setTitle("타임라인 삭제")
                        .setMessage("해당 게시물을 삭제하시겠습니까?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timelineList.remove(position);
                                adapter.notifyDataSetChanged();

                                Snackbar.make(view, "게시물 삭제", Snackbar.LENGTH_SHORT)
                                        .setAction("게시물 삭제", null).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                adapter.notifyDataSetChanged();
            }

        });

        recyclerView.setAdapter(adapter);
    }

    // Bookmark 설정 된 게시글이 위로 가도록 정렬
    private final static Comparator<TimelineListAdapter.Item> sortByBookmark = new Comparator<TimelineListAdapter.Item>() {
        @Override
        public int compare(TimelineListAdapter.Item o1, TimelineListAdapter.Item o2) {
            return Boolean.compare(o1.isNotPick, o2.isNotPick);
        }
    };

    // 읽지 않은 게시글이 위로 가도록 정렬
    private final static Comparator<TimelineListAdapter.Item> sortByChecked = new Comparator<TimelineListAdapter.Item>() {
        @Override
        public int compare(TimelineListAdapter.Item o1, TimelineListAdapter.Item o2) {
            if (Boolean.compare(o1.isCheck, o2.isCheck) == 0)
                return 1;
            return Boolean.compare(o1.isCheck, o2.isCheck);
        }
    };

    // 최근 업로드 게시글이 위로 가도록 정렬
    private final static Comparator<TimelineListAdapter.Item> sortByDate = new Comparator<TimelineListAdapter.Item>() {
        @Override
        public int compare(TimelineListAdapter.Item o1, TimelineListAdapter.Item o2) {
            return 0;
        }
    };
}
