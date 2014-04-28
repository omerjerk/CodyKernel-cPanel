package in.umairkhan.codykernelcpanel.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by omerjerk on 23/4/14.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean dt2w_enabled = prefs.getBoolean("dt2w_enabled", false);
        if (dt2w_enabled) {
            try{
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

                outputStream.writeBytes("echo 1 > /sys/android_touch/doubletap2wake\n");
                outputStream.flush();

                outputStream.writeBytes("exit\n");
                outputStream.flush();
                su.waitFor();
            }catch(IOException e){
                e.printStackTrace();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        boolean s2w_enabled = prefs.getBoolean("s2w_enabled", false);
        if (s2w_enabled) {
            try{
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

                outputStream.writeBytes("echo 1 > /sys/android_touch/sweep2wake\n");
                outputStream.flush();

                outputStream.writeBytes("exit\n");
                outputStream.flush();
                su.waitFor();
            }catch(IOException e){
                e.printStackTrace();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
