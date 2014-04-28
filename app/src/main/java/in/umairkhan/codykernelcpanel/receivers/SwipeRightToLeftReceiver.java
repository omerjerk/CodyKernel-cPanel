package in.umairkhan.codykernelcpanel.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by omerjerk on 23/4/14.
 */
public class SwipeRightToLeftReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean("swipe_r2l_app_enabled", false)) {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(
                    prefs.getString("swipe_r2l_app", "com.android.vending")
            );
            context.startActivity(launchIntent);
        }
    }
}
