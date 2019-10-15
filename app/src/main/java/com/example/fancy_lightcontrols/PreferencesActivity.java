package com.example.fancy_lightcontrols;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

public class PreferencesActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter = null;
    Button btnPaired;
    ListView devicelist;
    Set<BluetoothDevice> pairedDevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        btnPaired = findViewById(R.id.pairedDevicesButton);

    // Getting Bluetooth adapter
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
        btnPaired.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            pairedDevicesList(v.getContext()); //method that will be called
        }
    });



}
    private void pairedDevicesList(final Context context)
    {
        AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
        {
            public void onItemClick (AdapterView av, View v, int arg2, long arg3)
            {
                // Get the device MAC address, the last 17 chars in the View
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);
                Data.setCurrDeviceAddress(address);
                try {
                    Data.setLastConnectedAddress(address);
                    ConnectBT con = new ConnectBT(mBluetoothAdapter, context, address);
                    con.execute();
                }catch (Exception e){
                    Toast.makeText(context, e.getLocalizedMessage() ,Toast.LENGTH_LONG).show();}
            }
        };
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {

            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }
        devicelist = findViewById(R.id.device_list);
        final ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener);

    }
}
