package kksy.konkuk_smart_ecampus;

import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.estimote.sdk.SystemRequirementsChecker;


public class BeaconActivity extends AppCompatActivity {

    private Switch beaconSwitch;
    BluetoothAdapter bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        beaconSwitch = (Switch) findViewById(R.id.beaconSwitch);
        beaconSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    bluetoothAdapter.enable();
                }
                else{
                    bluetoothAdapter.disable();
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

    }
    protected void onPause(){
        super.onPause();
    }
}