package com.example.fancy_lightcontrols;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
{
    private BluetoothAdapter myBluetooth;
    private ProgressDialog progress;
    UUID myUUID;
    Context context;
    String address;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    ImageView changableImage;

    private boolean ConnectSuccess = true; //if it's here, it's almost connected
    public ConnectBT(BluetoothAdapter myBluetooth, Context context, String address, ImageView iv){
        this.myBluetooth = myBluetooth;
        this.context = context;
        this.address = address;
        myUUID = UUID.fromString(Data.getMyUUID());
        changableImage = iv;
    }
    public ConnectBT(BluetoothAdapter myBluetooth, Context context, String address){
        this.myBluetooth = myBluetooth;
        this.context = context;
        this.address = address;
        myUUID = UUID.fromString(Data.getMyUUID());
        changableImage = null;
    }

    @Override
    protected void onPreExecute() {
        if(btSocket != null && btSocket.isConnected()) Disconnect(btSocket, context);
        progress = ProgressDialog.show(context, "Connecting...", "Please wait!!!");  //show a progress dialog
    }

    @Override
    protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
    {
        try {
            if (btSocket == null || !isBtConnected) {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
            }
        } catch (IOException e) {
            ConnectSuccess = false;//if the try failed, you can check the exception here
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
    {
        super.onPostExecute(result);

        if (!ConnectSuccess) {
            Toast.makeText(context, "Connection Failed. Is it a SPP Bluetooth? Try again.", Toast.LENGTH_SHORT).show();
            if(changableImage != null)
            changableImage.setImageResource(R.drawable.ic_bluetooth_disconnected);
        } else {
            Toast.makeText(context, "Connected.", Toast.LENGTH_SHORT).show();
            isBtConnected = true;
            Data.setIsConnected(true);
            Data.setBtSBackup(btSocket);
            if(changableImage != null)
            changableImage.setImageResource(R.drawable.ic_bluetooth_connected);
        }
        progress.dismiss();
    }
    void Disconnect(BluetoothSocket btSocket, Context context){
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
                Data.setIsConnected(false);
            }
            catch (IOException e) {
                Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
