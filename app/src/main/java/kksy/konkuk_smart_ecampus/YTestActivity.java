package kksy.konkuk_smart_ecampus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class YTestActivity extends AppCompatActivity {

    private static final String TAG = "yeori";

    public final static String AUTH_KEY_FCM = "AAAAo0mUWQs:APA91bFwpft44gLY_oSyqgMB0exl-PMVFtj6rx2FfuJ8Oq6bTiV2gNnUQmTmsUNAQnU6_OPpNNY-gnww5D9CF7d_ZX39jp0-hj4b9Bw7zY8J6WXWoyx18LMe7q8YhYH9AcZgN7yvzDVL";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ytest);

        Token=FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, Token);
        try {
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        pushFCMNotification(Token);
                    }catch (Exception ex){
                        Log.i(TAG, ex.toString());
                    }
                }
            });
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, Token, Toast.LENGTH_SHORT).show();

    }
    public static void pushFCMNotification(String userDeviceIdKey) throws Exception{

        String authKey = AUTH_KEY_FCM; // You FCM AUTH key
        String FMCurl = API_URL_FCM;

        URL url = new URL(FMCurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization","key="+authKey);
        conn.setRequestProperty("Content-Type","application/json");

//        JSONObject json = new JSONObject();
//        json.put("to",userDeviceIdKey.trim());
//        JSONObject info = new JSONObject();
//        info.put("title", "Notificatoin Title"); // Notification title
//        info.put("body", "Hello Test notification"); // Notification body
//        json.put("notification", info);

        String input = "{\"notification\" : {\"title\" : \"여기다가 제목 넣기\", \"body\" : \"여기다 내용 넣기\"}, \"to\":\"/topics/ALL\"}";

        OutputStream os = conn.getOutputStream();

        // 서버에서 날려서 한글 깨지는 사람은 아래처럼  UTF-8로 인코딩해서 날려주자
        os.write(input.getBytes("UTF-8"));
        os.flush();
        os.close();

//        OutputStream wr = conn.getOutputStream();
//        wr.write(json.toString().getBytes("UTF-8"));
//        wr.flush();
//        wr.close();

        int responseCode=conn.getResponseCode();
        Log.i(TAG, responseCode+"");
        BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response=new StringBuffer();

        Log.i(TAG, response.toString());
        Log.i(TAG, "전송완료");
    }
}
