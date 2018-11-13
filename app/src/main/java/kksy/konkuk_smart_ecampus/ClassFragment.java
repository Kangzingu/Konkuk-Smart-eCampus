package kksy.konkuk_smart_ecampus;


import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_NUM = "subject number";

    // TODO: Rename and change types of parameters
    /*
    - 여리
    mSubNum : 선택한 과목의 수강번호 -> 과목의 ID 역할
     */
    private String mSubNum="s1";   //현재 수강번호가 3041 형식으로 주어져 있지 않기 때문에 설정함.
    //수강 릴레이션이 만들어지면 수정할 예정임(열)

    BoardListAdapter adapterNotice;
    BoardListAdapter adapterLectureData;
    BoardListAdapter adapterAssignment;

    RecyclerView recyclerViewNotice;
    RecyclerView recyclerViewLectureData;
    RecyclerView recyclerViewAssignment;

    List<Board> noticeList;
    List<Board> lectureDataList;
    List<Board> assignmentList;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //여리 - Board 가져오기
    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;
    Query query;
    Thread t;
    Handler handler=null;
    int ind=0;

    public ClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassFragment newInstance(String subNum) {
        ClassFragment fragment = new ClassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_NUM, subNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*
            - 여리
            여기서 ClassFragment로 전달한 과목의 수강번호 설정
            DB 가져오기 편한대로 수정해도 OK

            수정 하려면 위에 mSubNum 처럼 변수 만들고,
            MainActivity에서 ClassFragment에게 파라미터 넘겨줘야 함.
             */
            mSubNum = getArguments().getString(ARG_PARAM_NUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class, null);
        recyclerViewNotice = (RecyclerView) view.findViewById(R.id.listViewNotice);
        recyclerViewLectureData = (RecyclerView) view.findViewById(R.id.listViewLectureData);
        recyclerViewAssignment = (RecyclerView) view.findViewById(R.id.listViewAssignment);

        recyclerViewNotice.setNestedScrollingEnabled(false);
        recyclerViewLectureData.setNestedScrollingEnabled(false);
        recyclerViewAssignment.setNestedScrollingEnabled(false);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        initBoardMain();

        return view;
    }

    public void initBoardMain(){
        noticeList = new ArrayList<>();
        lectureDataList = new ArrayList<>();
        assignmentList = new ArrayList<>();

        /*
        - 여리
        Board 객체 생성을 통해 각 List의 Item 생성
        생성한 Item들을 공지사항, 강의자료, 과제로 분류하여 추가

        - noticeList : 공지사항 List
        - lectureDataList : 강의자료 List
        - assignmentList : 과제 List
         */
        mdatabase = FirebaseDatabase.getInstance();
        mdbRef=mdatabase.getReference("board");
        query = mdbRef.orderByKey().equalTo("s1-p1");
        handler=new Handler();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    ind++;
                    Log.i("ind", ind+"");

                    Iterable<DataSnapshot> data=snapshot.getChildren();
                    //강의자료
                    //Log.i("data5",  data.iterator().next().getKey());
                    //->강의자료의 모든 value들을 가져온다. 2018...
                    Iterable<DataSnapshot> data1=data.iterator().next().getChildren();  //2018-10-26 21:51:45, 2018-10-27...
                    for(Iterator<DataSnapshot> da=data1.iterator();data1.iterator().hasNext();){
                        Board board=da.next().getChildren().iterator().next().getValue(Board.class);
                        lectureDataList.add(board);
                        Log.i("value", board.getTitle());
                        //Log.i("보쟈", da.next().getValue().toString()); //-LP11bhR2..., -LPIJ...
                    }

                    //공지
                    Iterable<DataSnapshot> data2=data.iterator().next().getChildren();
                    for(Iterator<DataSnapshot> da=data2.iterator();data2.iterator().hasNext();){
                        Board board=da.next().getChildren().iterator().next().getValue(Board.class);
                        noticeList.add(board);
                        Log.i("value", board.getTitle());
                    }

                    //과제
                    Iterable<DataSnapshot> data3=data.iterator().next().getChildren();
                    for(Iterator<DataSnapshot> da=data3.iterator();data3.iterator().hasNext();){
                        Board board=da.next().getChildren().iterator().next().getValue(Board.class);
                        assignmentList.add(board);
                        Log.i("value", board.getTitle());
                    }

                    adapterNotice.notifyDataSetChanged();
                    adapterLectureData.notifyDataSetChanged();
                    adapterAssignment.notifyDataSetChanged();

//                    adapter = new BoardListAdapter(noticeList);
//                    recyclerViewNotice.setAdapter(adapter);
//
//                    adapter = new BoardListAdapter(lectureDataList);
//                    recyclerViewLectureData.setAdapter(adapter);
                }
//            //UI작업
//                t=new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                //UI작업 수행
//                                Log.i("result1", lectureDataList.get(0).getTitle());
//                                Log.i("result2", noticeList.get(0).getTitle());
//                                Log.i("result3", assignmentList.get(0).getTitle());
////                                adapter.notifyDataSetChanged();
//                            }
//                        });
//                    }
//                });
//                t.start();
            }

            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });

//        noticeList.add(new Board(0.+"", "공지사항", "팀프로젝트 조구성"));
//        noticeList.add(new Board(0+"","공지사항", "실습 설정 dll 오류 나는 문제"));
//        noticeList.add(new Board(0+"","공지사항", "실습 이미지 영상"));
//        noticeList.add(new Board(0+"","공지사항", "(10/25 수정)컴퓨터 비전 실습 공지"));
//        noticeList.add(new Board(0+"","공지사항", "과제 공지"));
//
//        lectureDataList.add(new Board(0+"","강의자료", "9-10주차 강의 노트"));
//        lectureDataList.add(new Board(0+"","강의자료", "3주차 Binary image analysis 2"));
//        lectureDataList.add(new Board(0+"","강의자료", "2주차 Binary image analysis 1"));
//
//        assignmentList.add(new Board(0+"","과제", "[10/4] 2차 요구사항 분석서 제출"));
//        assignmentList.add(new Board(0+"","과제", "[10/2] 미팅 요약서"));
//        assignmentList.add(new Board(0+"","과제", "[9/28] 미팅 요약서"));

        /*
        - 여리
        여기서 부터는 recycerView와 adapter 설정하는 곳 -> 안만져도 됨
         */
        recyclerViewNotice.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapterNotice = new BoardListAdapter(noticeList);
        adapterNotice.setBoardItemClick(new BoardListAdapter.BoardItemClick() {
            @Override
            public void onClick(View view, int position) {
                // 공지사항
                fragmentTransaction.replace(R.id.fragmentContainer, PostFragment.newInstance(noticeList.get(position).getBoardID()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        recyclerViewNotice.setAdapter(adapterNotice);

        recyclerViewLectureData.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapterLectureData = new BoardListAdapter(lectureDataList);
        adapterLectureData.setBoardItemClick(new BoardListAdapter.BoardItemClick() {
            @Override
            public void onClick(View view, int position) {
                // 강의자료
                fragmentTransaction.replace(R.id.fragmentContainer, PostFragment.newInstance(lectureDataList.get(position).getBoardID()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        recyclerViewLectureData.setAdapter(adapterLectureData);

        recyclerViewAssignment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapterAssignment = new BoardListAdapter(assignmentList);
        adapterAssignment.setBoardItemClick(new BoardListAdapter.BoardItemClick() {
            @Override
            public void onClick(View view, int position) {
                // 과제
                fragmentTransaction.replace(R.id.fragmentContainer, PostFragment.newInstance(assignmentList.get(position).getBoardID()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        recyclerViewAssignment.setAdapter(adapterAssignment);
    }
}
