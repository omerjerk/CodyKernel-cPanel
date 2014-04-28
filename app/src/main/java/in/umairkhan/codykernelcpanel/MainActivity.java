package in.umairkhan.codykernelcpanel;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private CheckBoxPreference mainDt2wPref;
    private CheckBoxPreference mainS2wPref;
    private CheckBoxPreference swipeLeftToRight;
    private CheckBoxPreference swipeRightToLeft;
    private ListPreference swipeL2RList;
    private ListPreference swipeR2LList;

    private static final String KEY_MAIN_DT2W = "dt2w_enabled";
    private static final String KEY_MAIN_S2W = "s2w_enabled";
    private static final String KEY_SWIPE_L2R = "swipe_l2r_app_enabled";
    private static final String KEY_SWIPE_R2L = "swipe_r2l_app_enabled";
    private static final String KEY_SWIPE_L2R_APP = "swipe_l2r_app";
    private static final String KEY_SWIPE_R2L_APP = "swipe_r2l_app";

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_prefs);

        HashMap<String, String> installedApps = new HashMap<String, String>();
        final PackageManager pm = getApplicationContext().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo info : packages) {
            if (pm.getLaunchIntentForPackage(info.packageName) != null) {
                installedApps.put(pm.getApplicationLabel(info).toString(), info.packageName);
            }
        }

        mainDt2wPref = (CheckBoxPreference) findPreference(KEY_MAIN_DT2W);
        mainS2wPref = (CheckBoxPreference) findPreference(KEY_MAIN_S2W);
        swipeLeftToRight = (CheckBoxPreference) findPreference(KEY_SWIPE_L2R);
        swipeRightToLeft = (CheckBoxPreference) findPreference(KEY_SWIPE_R2L);
        swipeL2RList = (ListPreference) findPreference(KEY_SWIPE_L2R_APP);
        swipeR2LList = (ListPreference) findPreference(KEY_SWIPE_R2L_APP);

        swipeL2RList.setEntries(installedApps.keySet().toArray(new String[] {}));
        swipeR2LList.setEntries(installedApps.keySet().toArray(new String[] {}));
        swipeL2RList.setEntryValues(installedApps.values().toArray(new String[] {}));
        swipeR2LList.setEntryValues(installedApps.values().toArray(new String[] {}));

        swipeLeftToRight.setEnabled(mainS2wPref.isChecked());
        swipeL2RList.setEnabled(mainS2wPref.isChecked());
        swipeRightToLeft.setEnabled(mainS2wPref.isChecked());
        swipeR2LList.setEnabled(mainS2wPref.isChecked());

        mainDt2wPref.setOnPreferenceChangeListener(this);
        mainS2wPref.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference pref, Object o) {
        if (pref.getKey().equals(KEY_MAIN_DT2W)) {
            try{
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

                if ((Boolean) o) {
                    outputStream.writeBytes("echo 1 > /sys/android_touch/doubletap2wake\n");
                } else {
                    outputStream.writeBytes("echo 0 > /sys/android_touch/doubletap2wake\n");
                }
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

        if (pref.getKey().equals(KEY_MAIN_S2W)) {
            try{
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

                swipeLeftToRight.setEnabled((Boolean) o);
                swipeL2RList.setEnabled((Boolean) o);
                swipeRightToLeft.setEnabled((Boolean) o);
                swipeR2LList.setEnabled((Boolean) o);
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
        return true;
    }

}
