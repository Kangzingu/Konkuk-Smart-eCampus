package kksy.konkuk_smart_ecampus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_POST_ID = "post id";

    // TODO: Rename and change types of parameters
    private int mPostId;

    String postType;
    String postTitle;
    String postDate;
    String postContent;

    TextView textViewPostType;
    TextView textViewPostTitle;
    TextView textViewPostDate;
    TextView textViewPostContent;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String postId) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*
            - 여리
            여기서 PostFragment로 전달한 게시글의 ID 설정
            DB 가져오기 편한대로 수정해도 OK

            수정 하려면 위에 mPostId 처럼 변수 만들고,
            ClassFragment 또는 HomeFragment에서  PostFragment에게 파라미터 넘겨줘야 함.
             */
            mPostId = getArguments().getInt(ARG_PARAM_POST_ID);
        }

        /*
        - 여리
        mPostId를 통해서 postType, postTitle, postDate, postContent에 입력
         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_post, null);

        textViewPostType = (TextView) view.findViewById(R.id.textViewPostType);
        textViewPostTitle = (TextView) view.findViewById(R.id.textViewPostTitle);
        textViewPostDate = (TextView) view.findViewById(R.id.textViewPostDate);
        textViewPostContent = (TextView) view.findViewById(R.id.textViewPostContent);

        /*
        - 여리
        아직 String들 한테 데이터가 안들어가서 setText 주석 했음
        테스팅 할 때는 꼭 주석 풀고 테스팅!
         */
//        textViewPostType.setText(postType);
//        textViewPostTitle.setText(postTitle);
//        textViewPostDate.setText(postDate);
//        textViewPostContent.setText(postContent);

        return view;
    }

}
