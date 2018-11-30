package kksy.konkuk_smart_ecampus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG="MyFirebaseMessagingServ";
    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;
    Query query;

    String rtitle;
    String rbody;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log.i("yeori3", remoteMessage.getNotification().getTitle());
        //Log.i("yeori3", remoteMessage.getNotification().getBody());
        sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

    }
    // [END receive_message]

    public void checkSubjectSugang(final String subjectID){
        //파이어 베이스에 접근해서 이를 알아온다.
        mdatabase = FirebaseDatabase.getInstance();
        mdbRef=mdatabase.getReference("sugang");
        query = mdbRef.orderByChild("subID").equalTo(subjectID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Sugang sugang=snapshot.getValue(Sugang.class);
                    Log.i("yeori3", sugang.getClass().toString());
                    if(sugang.getStudentID().equals(MainActivity.UserID)){
                        Log.i("yeori3", "안녕나는 수강하는 아이야.");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,
                                PendingIntent.FLAG_ONE_SHOT);

                        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(rtitle) // 이부분은 어플 켜놓은 상태에서 알림 메세지 받으면 저 텍스트로 띄워준다.
                                .setContentText(rbody)
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent);

                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void sendNotification(String title, String messageBody) {

        rtitle=title;

        String[] temp=messageBody.split(",");
        Log.i("yeori3", temp[1]);
        String[] temp2=temp[1].split("-");
        Log.i("yeori3", temp2[0]);

        String subjectID=temp2[0];
        rbody=messageBody;

        //파베 부르기
        checkSubjectSugang(subjectID);

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Board Upload") // 이부분은 어플 켜놓은 상태에서 알림 메세지 받으면 저 텍스트로 띄워준다.
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}