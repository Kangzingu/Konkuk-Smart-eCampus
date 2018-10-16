package kksy.konkuk_smart_ecampus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_NUM = "subject number";

    TextView textView;

    // TODO: Rename and change types of parameters
    private String mSubNum;


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
            mSubNum = getArguments().getString(ARG_PARAM_NUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class, null);
        textView = (TextView) view.findViewById(R.id.subjectNumClass);

        textView.setText(mSubNum);

        return view;
    }

}
