package kksy.konkuk_smart_ecampus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static kksy.konkuk_smart_ecampus.ExpandableListAdapter.CHILD2;
import static kksy.konkuk_smart_ecampus.ExpandableListAdapter.HEADER;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ExpandableListAdapter adapter;

    List<ExpandableListAdapter.Item> timelineList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.listViewTimeline);

        initTimeline();

        return view;
    }

    public void initTimeline(){
        /*
        Timeline 데이터 가져와서 적용해야 함
         */

        List<ExpandableListAdapter.Item> data = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        ExpandableListAdapter.Item header1 = new ExpandableListAdapter.Item(
                HEADER,
                "[과제][10/2] 미팅 요약서"
        );
        header1.invisibleChildren = new ArrayList<>();
        header1.invisibleChildren.add(new ExpandableListAdapter.Item(
                CHILD2,
                "산학협력프로젝트2(종합설계)",
                "[10/2] 미팅 요약서",
                "2018.10.02 오후 11:59"
        ));
        ExpandableListAdapter.Item header2 = new ExpandableListAdapter.Item(
                HEADER,
                "[과제][10/4] 2차 요구사항 분석서 제출"
        );
        header2.invisibleChildren = new ArrayList<>();
        header2.invisibleChildren.add(new ExpandableListAdapter.Item(
                CHILD2,
                "산학협력프로젝트2(종합설계)",
                "[10/4] 2차 요구사항 분석서 제출",
                "2018.10.05 오후 11:59"
        ));
        ExpandableListAdapter.Item header3 = new ExpandableListAdapter.Item(
                HEADER,
                "[과제]자기소개서"
        );
        header3.invisibleChildren = new ArrayList<>();
        header3.invisibleChildren.add(new ExpandableListAdapter.Item(
                CHILD2,
                "발명과특허",
                "자기소개서",
                "2018.10.07 오후 11:59"
        ));

        data.add(header1);
        data.add(header2);
        data.add(header3);

        recyclerView.setAdapter(new ExpandableListAdapter(data));
    }

}

//    TimelineListAdapter.Item item1 = new TimelineListAdapter.Item(
//            "산학협력프로젝트2(종합설계)",
//            "[10/2] 미팅 요약서",
//            "2018.10.02 오후 11:59"
//    );
//    TimelineListAdapter.Item item2 = new TimelineListAdapter.Item(
//            "산학협력프로젝트2(종합설계)",
//            "[10/4] 2차 요구사항 분석서 제출",
//            "2018.10.05 오후 11:59"
//    );
//    TimelineListAdapter.Item item3 = new TimelineListAdapter.Item(
//            "발명과특허",
//            "자기소개서",
//            "2018.10.07 오후 11:59"
//    );
