package in.umairkhan.codykernelcpanel.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by omerjerk on 29/4/14.
 */
public class SwipeUpToDownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean("swipe_u2d_app_enabled", false)) {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(
                    prefs.getString("swipe_u2d_app", "com.android.vending")
            );
            context.startActivity(launchIntent);
        }
    }
}
