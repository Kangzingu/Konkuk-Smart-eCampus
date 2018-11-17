package kksy.konkuk_smart_ecampus;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_POST_ID = "post id";
    private static final String SID_PID="sid_pid";

    // TODO: Rename and change types of parameters
    private int mPostId;

    private String mBoardId;/*="-LPkQXJjtAwvp1dJoMS0"*/; //boardID를 담고있음. fragment에서 fragment로 넘어올 때 넣을 것.
    String postType;
    String postTitle;
    String postDate;
    String postContent;

    TextView textViewPostType;
    TextView textViewPostTitle;
    TextView textViewPostDate;
    TextView textViewPostContent;

    View menuClass;

    //YEORI
    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;
    Query query;
    Board realBoard;    //realBoard는 query를 통해 찾은 board를 저장해 놓는 로컬 변수이다.
    String sid_pid;
    //
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
    public static PostFragment newInstance(String postId, String sidpid) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_POST_ID, postId);
        args.putString(SID_PID, sidpid);
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
            mBoardId = getArguments().getString(ARG_PARAM_POST_ID);
            sid_pid=getArguments().getString(SID_PID);
            Log.i("boardid", mBoardId);
        }

        mdatabase = FirebaseDatabase.getInstance();
        mdbRef=mdatabase.getReference("board");

        //다스리
        query = mdbRef.orderByKey().equalTo(sid_pid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    boolean check=false;
                    Iterable<DataSnapshot> data=snapshot.getChildren();

                    //강의자료
                    //Log.i("data5",  data.iterator().next().getKey());
                    //->강의자료의 모든 value들을 가져온다. 2018...
                    Iterable<DataSnapshot> data1=data.iterator().next().getChildren();  //2018-10-26 21:51:45, 2018-10-27...
                    for(Iterator<DataSnapshot> da = data1.iterator(); data1.iterator().hasNext();){
                        Board board=da.next().getChildren().iterator().next().getValue(Board.class);
                        if(mBoardId.equals(board.getBoardID())){
                            check=true;
                            realBoard=board;
                            break;
                        }
                        //Log.i("보쟈", da.next().getValue().toString()); //-LP11bhR2..., -LPIJ...
                    }

                    //공지
                    Iterable<DataSnapshot> data2=data.iterator().next().getChildren();
                    for(Iterator<DataSnapshot> da=data2.iterator();data2.iterator().hasNext();){
                        Board board=da.next().getChildren().iterator().next().getValue(Board.class);
                        if(check)
                            break;
                        else if(!check){
                            if(mBoardId.equals(board.getBoardID())){
                                check=true;
                                realBoard=board;
                                break;
                            }
                        }
                    }

                    //과제
                    Iterable<DataSnapshot> data3=data.iterator().next().getChildren();
                    for(Iterator<DataSnapshot> da=data3.iterator();data3.iterator().hasNext();){
                        Board board=da.next().getChildren().iterator().next().getValue(Board.class);
                        if(check)
                            break;
                        else if(!check){
                            if(mBoardId.equals(board.getBoardID())){
                                check=true;
                                realBoard=board;
                                break;
                            }
                        }
                        //Log.i("value", board.getTitle());
                    }

                    /*
                    - 여리
                    mPostId를 통해서 postType, postTitle, postDate, postContent에 입력
                    */
                    postType=realBoard.getType();
                    postTitle=realBoard.getTitle();
                    postDate=realBoard.getUploadDate();
                    postContent=realBoard.getContext();

                    textViewPostType.setText(postType);
                    textViewPostTitle.setText(postTitle);
                    textViewPostDate.setText(postDate);
                    textViewPostContent.setText(postContent);

                }

            }

            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });
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
