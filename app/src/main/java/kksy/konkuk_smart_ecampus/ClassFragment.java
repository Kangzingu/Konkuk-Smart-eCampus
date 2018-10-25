package kksy.konkuk_smart_ecampus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
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
    private String mSubNum;

    BoardListAdapter adapter;

    RecyclerView recyclerViewNotice;
    RecyclerView recyclerViewLectureData;
    RecyclerView recyclerViewAssignment;

    List<Board> noticeList;
    List<Board> lectureDataList;
    List<Board> assignmentList;

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

        noticeList.add(new Board(0, "공지사항", "팀프로젝트 조구성"));
        noticeList.add(new Board(0,"공지사항", "실습 설정 dll 오류 나는 문제"));
        noticeList.add(new Board(0,"공지사항", "실습 이미지 영상"));
        noticeList.add(new Board(0,"공지사항", "(10/25 수정)컴퓨터 비전 실습 공지"));
        noticeList.add(new Board(0,"공지사항", "과제 공지"));

        lectureDataList.add(new Board(0,"강의자료", "9-10주차 강의 노트"));
        lectureDataList.add(new Board(0,"강의자료", "3주차 Binary image analysis 2"));
        lectureDataList.add(new Board(0,"강의자료", "2주차 Binary image analysis 1"));

        assignmentList.add(new Board(0,"과제", "[10/4] 2차 요구사항 분석서 제출"));
        assignmentList.add(new Board(0,"과제", "[10/2] 미팅 요약서"));
        assignmentList.add(new Board(0,"과제", "[9/28] 미팅 요약서"));

        /*
        - 여리
        여기서 부터는 recycerView와 adapter 설정하는 곳 -> 안만져도 됨
         */
        recyclerViewNotice.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new BoardListAdapter(noticeList);
        recyclerViewNotice.setAdapter(adapter);

        recyclerViewLectureData.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new BoardListAdapter(lectureDataList);
        recyclerViewLectureData.setAdapter(adapter);

        recyclerViewAssignment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new BoardListAdapter(assignmentList);
        recyclerViewAssignment.setAdapter(adapter);
    }
}
