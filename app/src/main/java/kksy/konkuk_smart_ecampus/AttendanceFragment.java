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
    private String subNum;
    private String stdId;

    AttendanceListAdapter adapter;
    RecyclerView recyclerView;
    List<AttendanceListAdapter.Item> data;

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
            subNum = getArguments().getString(ARG_SUBNUM);
            stdId = getArguments().getString(ARG_STDID);
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

    public void initAttendance(){
        data = new ArrayList<>();

        ArrayList<AttendanceContentsAdapter.Item> contents1 = new ArrayList<>();
        contents1.add(new AttendanceContentsAdapter.Item(ATTEND, "2018.09.03(월)"));
        contents1.add(new AttendanceContentsAdapter.Item(LATE, "2018.09.05(수)"));
        data.add(new AttendanceListAdapter.Item(1, contents1));

        ArrayList<AttendanceContentsAdapter.Item> contents2 = new ArrayList<>();
        contents2.add(new AttendanceContentsAdapter.Item(LATE, "2018.09.10(월)"));
        contents2.add(new AttendanceContentsAdapter.Item(ABSENT, "2018.09.12(수)"));
        data.add(new AttendanceListAdapter.Item(2, contents2));

        ArrayList<AttendanceContentsAdapter.Item> contents3 = new ArrayList<>();
        contents3.add(new AttendanceContentsAdapter.Item(ATTEND, "2018.09.17(월)"));
        contents3.add(new AttendanceContentsAdapter.Item(ABSENT, "2018.09.19(수)"));
        data.add(new AttendanceListAdapter.Item(3, contents3));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new AttendanceListAdapter(getContext(), data);
        recyclerView.setAdapter(adapter);
    }

}
