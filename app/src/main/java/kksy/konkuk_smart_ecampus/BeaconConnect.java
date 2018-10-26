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
import com.google.firebase.database.Query;
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
    String subID_pID;
    DataSnapshot currentObject;//디비 읽으면서 임시 저장 하는 넘
    DataSnapshot studentObject;
    DataSnapshot lectureObject;
    DataSnapshot attendanceObject;

    long timeAsNum;
    Date timeAsDate;
    SimpleDateFormat timeAsformat;

    FirebaseDatabase mdatabase;
    DatabaseReference mdbRef;
    Query query;

    @Override
    public void onCreate() {
        super.onCreate();
        mdatabase = FirebaseDatabase.getInstance();
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"),
                        40001, 15402));
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onEnteredRegion(Region region, final List<com.estimote.sdk.Beacon> list) {
                //Beacon에 접근 성공 시
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        studentID = "201611210";
                        //임시로 해놓은거 원래 여기에 사용자 ID 들어와야행
                        Iterator<DataSnapshot> childSnap = dataSnapshot.child("student").getChildren().iterator();
                        while (childSnap.hasNext()) {
                            currentObject = childSnap.next();
                            if ((currentObject.child("studentID").getValue().toString()).equals(studentID)) {
                                studentObject=currentObject;
                            }
                        }
                        childSnap = dataSnapshot.child("lecture").getChildren().iterator();
                        while (childSnap.hasNext()) {
                            currentObject = childSnap.next();//스냅샷 저장해두고
                            if ((list.get(0).getProximityUUID().toString()).equals(currentObject.child("beconInfo").getValue().toString())) {
                                lectureObject=currentObject;
                            }
                        }
                        subID_pID = lectureObject.child("subID").getValue().toString() + "-" + lectureObject.child("proID").getValue().toString();
                        timeAsNum = System.currentTimeMillis();
                        timeAsDate = new Date(timeAsNum);
                        timeAsformat=new SimpleDateFormat("yyyy-MM-dd");
                        String dateToday= timeAsformat.format(timeAsDate);
                        childSnap=dataSnapshot.child("attendance").child(subID_pID).child(studentID).child(dateToday).getChildren().iterator();
                        while (childSnap.hasNext()) {
                            currentObject = childSnap.next();
                            attendanceObject = currentObject;
                        }
                        Toast.makeText(getApplicationContext(), attendanceObject.getKey().toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), attendanceObject.child("state").getValue().toString(), Toast.LENGTH_LONG).show();
                        String attendStartTime=lectureObject.child("attendTime").child("0").getValue().toString();
                        String attendEndTime=lectureObject.child("attendTime").child("1").getValue().toString();
                        String lateStartTime=lectureObject.child("lateTime").child("0").getValue().toString();
                        String lateEndTime=lectureObject.child("lateTime").child("1").getValue().toString();
                        //시간 비교
                        timeAsNum = System.currentTimeMillis();
                        timeAsDate = new Date(timeAsNum);
                        timeAsformat= new SimpleDateFormat("HH:mm");
                        String currentTime = timeAsformat.format(timeAsDate);
                        //현재시간
                        if((currentTime.compareTo(attendStartTime)>=0)&&(currentTime.compareTo(attendEndTime)<=0)) {
                            //08:00 ~ 08:10
                            //정상 출쳌
                            Toast.makeText(getApplicationContext(), "출석", Toast.LENGTH_LONG).show();
                            attendanceObject.child("state").getRef().setValue("출석");
                        }
                        else if((currentTime.compareTo(lateStartTime)>=0)&&(currentTime.compareTo(lateEndTime)<=0)){
                            //08:11 ~ 08:30
                            //지각
                            Toast.makeText(getApplicationContext(), "지각", Toast.LENGTH_LONG).show();
                            attendanceObject.child("state").getRef().setValue("지각");
                        }
                        else if((currentTime.compareTo(lateEndTime)>0)){
                            //결석
                            Toast.makeText(getApplicationContext(), "결석", Toast.LENGTH_LONG).show();
                            attendanceObject.child("state").getRef().setValue("결석");
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "수업 시간이 아닙니다", Toast.LENGTH_LONG).show();
                            attendanceObject.child("state").getRef().setValue("데헿");
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
