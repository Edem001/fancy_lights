package com.example.fancy_lightcontrols;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;

public class Data {
    private static String CURR_DEVICE_ADDRESS;
    private static boolean isConnected;
    private static String lastConnectedAddress;
    private static String myUUID;
    private static Context context;
    private static BluetoothSocket btSBackup;

    public static void setCurrDeviceAddress(String currDeviceAddress) {
        CURR_DEVICE_ADDRESS = currDeviceAddress;
    }
    public static String getCurrDeviceAddress() {
        return CURR_DEVICE_ADDRESS;
    }

    public static void setMyUUID(String newUUID) {
        newUUID = "00001101-0000-1000-8000-00805F9B34FB";
        myUUID = newUUID;
        prefWorker pref = new prefWorker("Bluetooth",context);
        pref.setStringValue(newUUID, 0);
    }
    public static String getMyUUID() {
        prefWorker pref = new prefWorker("Bluetooth",context);
        myUUID = pref.getStringValue(0);
        if(myUUID.equals("")) myUUID = "00001101-0000-1000-8000-00805F9B34FB";
        return myUUID;
    }

    public static void setLastConnectedAddress(String lastConnectedAddress) {
        Data.lastConnectedAddress = lastConnectedAddress;
        prefWorker pref = new prefWorker("Bluetooth",context);
        pref.setStringValue(lastConnectedAddress, 1);
    }
    public static String getLastConnectedAddress() {
        prefWorker pref = new prefWorker("Bluetooth",context);
        return  pref.getStringValue(1);
    }
    public static void setContext(Context context){
        Data.context = context;
    }

    public static void setIsConnected(boolean bool){
        isConnected = bool;
    }
    public static boolean isConnected(){
        return isConnected;
    }
    public static void setBtSBackup(BluetoothSocket btSocket){
        Data.btSBackup = btSocket;
    }
    public static BluetoothSocket getBtSBackup() {
        return btSBackup;
    }
}
