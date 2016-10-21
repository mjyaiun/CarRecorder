package com.mitu.carrecorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

public class ConnectedStateChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent arg1) {
        // TODO Auto-generated method stub


        if (Integer.parseInt(Build.VERSION.SDK) < 8)
            return;

        //
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //wifi״̬
        if (activeNetInfo != null) {
            if (activeNetInfo.getTypeName().equals("WIFI")) {
                WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                String name = String.valueOf(wm.getConnectionInfo().getSSID());


            }

//			Toast.makeText(context,"Active Network Type : " + activeNetInfo.getTypeName()+"--"+activeNetInfo.getState(),Toast.LENGTH_SHORT).show();

        }//

        if (mobNetInfo != null) {
//		       Toast.makeText(context, "Mobile Network Type : " + mobNetInfo.getTypeName()+"--"+mobNetInfo.getState(),Toast.LENGTH_SHORT).show(); 

        }
        if (wifiNetInfo != null) {
//	    	 Toast.makeText(context, "wifi Network Type : " + wifiNetInfo.getTypeName()+"--"+wifiNetInfo.getState(),Toast.LENGTH_SHORT).show();

        }
    }
}
