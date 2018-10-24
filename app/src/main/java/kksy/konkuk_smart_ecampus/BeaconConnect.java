package kksy.konkuk_smart_ecampus;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Date;

public class BeaconConnect extends Application {
    private BeaconManager beaconManager;
    DatabaseReference databaseReference;
    String studentID;//인텐트로 받아야행^^ 학생 키임
    DataSnapshot currentObject;//디비 읽으면서 임시 저장 하는 넘
    DataSnapshot studentObject;
    DataSnapshot lectureObject;

    long timeAsNum;
    Date timeAsDate;
    SimpleDateFormat timeAsformat=new SimpleDateFormat("HH:mm");

    @Override
    public void onCreate() {
        super.onCreate();
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"),
                        40001, 15402));
                //이 비콘과 연결하겠다그닥
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //databaseReference.child("student").child("beconCheck").setValue(true);
        //연결되면 student의 beaconCheck를 true로 바꿈여
        //databaseReference.child("");
        //DB에게 이 학생은 현재 출쳌 완료 했당 이걸 알려주는겅미

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onEnteredRegion(Region region, final List<com.estimote.sdk.Beacon> list) {
                //**********************************************얘 원래 final 아님^^*****************************************
                //Beacon에 접근 성공 시
                //data 보낸다 check ok로
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> childSnap = dataSnapshot.child("student").getChildren().iterator();
                        //학생 정보 가져온당
                        studentID = "201611210";
                        //임시로 해놓은거 원래 여기에 사용자 ID 들어와야행
                        while (childSnap.hasNext()) {
                            currentObject = childSnap.next();//스냅샷 저장해두고
                            if ((currentObject.child("studentID").getValue().toString()).equals(studentID)) {
                                //만약 지금 사용자의 디비 정보를 성공적으로 가져왔을 경우
                                currentObject.child("beconCheck").getRef().setValue(true);//이 부분은 필요가 없을듯
                                //요 사용자가 출첵(비콘인식) 완료한걸 디비에 알려줌
                                studentObject=currentObject;
                            }
                        }
                        childSnap = dataSnapshot.child("lecture").getChildren().iterator();
                        //이제 비콘 리스트를 쭉 볼거임 지금 이 사용자가 연결한 비콘이랑 맞는 비콘있는지
                        while (childSnap.hasNext()) {
                            currentObject = childSnap.next();//스냅샷 저장해두고
                            //    if(child.next().getKey().equals(checkId))
                            if ((list.get(0).getProximityUUID().toString()).equals(currentObject.child("beconInfo").getValue().toString())) {
                                //현재 과목의 비콘 아이디 일치
                                lectureObject=currentObject;
                                String attendStartTime=currentObject.child("attendTime").child("0").getValue().toString();
                                String attendEndTime=currentObject.child("attendTime").child("1").getValue().toString();
                                String lateStartTime=currentObject.child("lateTime").child("0").getValue().toString();
                                String lateEndTime=currentObject.child("lateTime").child("1").getValue().toString();
                                //시간 비교
                                timeAsNum = System.currentTimeMillis();
                                timeAsDate = new Date(timeAsNum);
                                String currentTime = "08:31";//timeAsformat.format(timeAsDate);
                                //현재시간
                                if((currentTime.compareTo(attendStartTime)>=0)&&(currentTime.compareTo(attendEndTime)<=0)){
                                    //08:00 ~ 08:10
                                    //정상 출쳌
                                    Toast.makeText(getApplicationContext(), "정상", Toast.LENGTH_LONG).show();
                                }
                                else if((currentTime.compareTo(lateStartTime)>=0)&&(currentTime.compareTo(lateEndTime)<=0)){
                                    //08:11 ~ 08:30
                                    //지각
                                    Toast.makeText(getApplicationContext(), "지각", Toast.LENGTH_LONG).show();
                                }
                                else if((currentTime.compareTo(lateEndTime)>0)){
                                    //결석
                                    Toast.makeText(getApplicationContext(), "결석", Toast.LENGTH_LONG).show();
                                }
                                Toast.makeText(getApplicationContext(), lectureObject.child("proID").getValue().toString(), Toast.LENGTH_LONG).show();
                                //else는 너무 일찍 온 경우

                                //이제 이 과목의 출석 완료 댓다고 출석부에 업뎃

                                //studentObject : 현재 사용자
                                //lectureObject : 현재 과목

                                //int r1 = time1.compareTo(time2);//-2
                                //int r2 = time2.compareTo(time1);//2
                                //Toast.makeText(getApplicationContext(), String.valueOf(r1), Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), String.valueOf(r2), Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), time1, Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), time2, Toast.LENGTH_LONG).show();

                                //만약 내가 방금 인식한 비콘이 뭔지 찾았다면
                                //수강에 들어가서
                                //내가 지금 수강하고있는 과목들의 비콘이랑 다 비교 해서
                                //시간 비교해서 넣으면 될듯???
                            }
                            return;
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                showNotification("들어옴", "비콘 연결eeeeeeeeee됨" + list.get(0).getProximityUUID());
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onExitedRegion(Region region) {
                showNotification("나감", "비콘 연결 끊김");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
