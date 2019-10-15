package com.example.fancy_lightcontrols;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    BluetoothSocket btSocket;
    ImageView btImage;
    ImageView animImage;
    AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;
        btImage = findViewById(R.id.btConnected);
        updateConnectionImage(btSocket);

        animImage = findViewById(R.id.effects_demo);
        animImage.setBackgroundResource(R.drawable.butt_anim);
        animationDrawable = (AnimationDrawable) animImage.getBackground();
        ImageView sendImage = findViewById(R.id.goto_send_data);
        TextView sendImage_label = findViewById(R.id.butt_sett_command);

        sendImage.setOnClickListener(image_text_Click);
        sendImage_label.setOnClickListener(image_text_Click);

        Data.setContext(this);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null){
            Toast.makeText(this, "Bluetooth device error!", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            if (mBluetoothAdapter.isEnabled()){

            }else{
                //Ask to the user turn the bluetooth on
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);
            }
        }
        String btAddress;
        try {
            btAddress = Data.getLastConnectedAddress();
        }catch (Exception e){ btAddress = ""; }
        if (!btAddress.equals("")){
            BluetoothDevice btDevice = mBluetoothAdapter.getRemoteDevice(btAddress);
            try {
                ConnectBT con = new ConnectBT(mBluetoothAdapter, context, Data.getLastConnectedAddress(), btImage);
                con.execute();
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        btSocket = Data.getBtSBackup();
    }
    View.OnClickListener image_text_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                if (view.getId() == R.id.butt_sett_command || view.getId() == R.id.goto_send_data) {
                    Intent intent = new Intent(getApplicationContext(), led_control.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };
    public void goToPreferences(View v){
        Intent intent = new Intent(v.getContext(), PreferencesActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateConnectionImage(btSocket);
    }

    void updateConnectionImage(BluetoothSocket btSocket){
        btSocket = Data.getBtSBackup();
        if (btSocket != null){
            if(btSocket.isConnected()){
                btImage.setImageResource(R.drawable.ic_bluetooth_connected);
            }else{
                btImage.setImageResource(R.drawable.ic_bluetooth_disconnected);
            }
        }else{
            btImage.setImageResource(R.drawable.ic_bluetooth_disconnected);
        }
    }

    @Override
    protected void onDestroy() {
        if(btSocket != null)
        Disconnect();
        super.onDestroy();
    }

    void Disconnect(){
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
            animationDrawable.start();
    }
}
