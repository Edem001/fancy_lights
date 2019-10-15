package com.example.fancy_lightcontrols;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class led_control extends AppCompatActivity {
Button sendData;
String address, text;
BluetoothAdapter myBluetooth = null;
BluetoothSocket btSocket = null;
EditText editText;
static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_control);
        sendData = findViewById(R.id.butt_sendData);
        editText = findViewById(R.id.editText);
        address = Data.getCurrDeviceAddress();
        if (!Data.isConnected()) {
            Toast.makeText(this, "Seems like you not connected", Toast.LENGTH_SHORT).show();
            finish();
        }else btSocket = Data.getBtSBackup();
        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    text = editText.getText().toString();
                }catch (Exception e){

                }
                try {
                    btSocket.getOutputStream().write(text.toString().getBytes());
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

