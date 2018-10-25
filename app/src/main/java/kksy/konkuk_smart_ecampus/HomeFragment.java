package kksy.konkuk_smart_ecampus;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static kksy.konkuk_smart_ecampus.ExpandableListAdapter.CHILD2;
import static kksy.konkuk_smart_ecampus.ExpandableListAdapter.HEADER;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    TimelineListAdapter adapter;

    List<TimelineListAdapter.Item> timelineList;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.listViewTimeline);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        initTimeline();

        return view;
    }

    public void initTimeline(){
        timelineList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        /*
        - 여리
        TimelineListAdapter.Item 객체 생성을 통해 Timeline의 Item들 생성
        생성한 Item들을 data에 추가

        - TimelineListAdapter.Item
            TimelineListAdapter.Item(강의명, 게시물 제목, 게시물 업로드 날짜, 게시물 확인 여부, 게시물 북마크 여부)
        - timelineList : Timeline의 List
         */

        timelineList.add(new TimelineListAdapter.Item(
                "0",
                "산학협력프로젝트2(종합설계)",
                "[10/2] 미팅 요약서",
                "2018.10.02 오후 11:59",
                false, false
        ));
        timelineList.add(new TimelineListAdapter.Item(
                "0",
                "산학협력프로젝트2(종합설계)",
                "[10/4] 2차 요구사항 분석서 제출",
                "2018.10.05 오후 11:59",
                false, true
        ));
        timelineList.add(new TimelineListAdapter.Item(
                "0",
                "발명과특허",
                "자기소개서",
                "2018.10.07 오후 11:59",
                true, false
        ));

        // timeline에서 보여질 item 정렬

        /*Collections.sort(data, sortByDate);*/
        Collections.sort(timelineList, sortByChecked);
        Collections.sort(timelineList, sortByBookmark);

        adapter = new TimelineListAdapter(timelineList);
        adapter.setItemPickClick(new TimelineListAdapter.TimelineItemPickClick() {
            @Override
            public void onPickClick(View view, int postion) {
                if(!timelineList.get(postion).isNotPick){
                    timelineList.get(postion).isNotPick = true;

                    /*
                    - 여리
                    Bookmark 해제 시, bookmark 여부 DB에도 반영
                     */
                }
                else{
                    timelineList.get(postion).isNotPick = false;

                    /*
                    - 여리
                    Bookmark 등록 시, bookmark 여부 DB에도 반영
                     */
                }

                Collections.sort(timelineList, sortByChecked);
                Collections.sort(timelineList, sortByBookmark);
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setItemClick(new TimelineListAdapter.TimelineItemClick() {
            @Override
            public void onClick(View view, int position) {
                if(!timelineList.get(position).isCheck){ // 게시물 미확인 -> 게시물 확인
                    timelineList.get(position).isCheck = true;

                    /*
                    - 여리
                    게시물 확인 시, 게시물 확인 여부 DB에도 반영
                     */
                }

                Collections.sort(timelineList, sortByChecked);
                Collections.sort(timelineList, sortByBookmark);
                adapter.notifyDataSetChanged();

                /*
                게시물 클릭 시, 해당 게시물 ID를 통해 게시물 fragment로 이동
                 */

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, PostFragment.newInstance(timelineList.get(position).timeline_post_id));
                fragmentTransaction.commit();

                Snackbar.make(view, "해당 게시물로 이동", Snackbar.LENGTH_SHORT)
                        .setAction("해당 게시물로 이동", null).show();
            }
        });
        adapter.setItemLongClick(new TimelineListAdapter.TimelineItemLongClick() {
            @Override
            public void onLongClick(View view, int position) {

                /*
                - 여리
                게시물 삭제 시, Timeline DB에서도 삭제
                 */

                timelineList.remove(position);
                adapter.notifyDataSetChanged();

                Snackbar.make(view, "아이템 삭제", Snackbar.LENGTH_SHORT)
                        .setAction("아이템 삭제", null).show();
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
            if(Boolean.compare(o1.isCheck, o2.isCheck) == 0)
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
