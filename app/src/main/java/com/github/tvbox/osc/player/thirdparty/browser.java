package com.github.tvbox.osc.player.thirdparty;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.github.tvbox.osc.base.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class browser {
    public static final String TAG = "ThirdParty.browser";

    private static final String PACKAGE_NAME = "com.android.browser";
    private static final String PLAYBACK_ACTIVITY = "com.android.browser.BrowserActivity";

    private static class browserPackageInfo {
        final String packageName;
        final String activityName;

        browserPackageInfo(String packageName, String activityName) {
            this.packageName = packageName;
            this.activityName = activityName;
        }
    }

    private static final browserPackageInfo[] PACKAGES = {
            new browserPackageInfo(PACKAGE_NAME, PLAYBACK_ACTIVITY),
    };

    public static browserPackageInfo getPackageInfo() {
        for (browserPackageInfo pkg : PACKAGES) {
            try {
                ApplicationInfo info = App.getInstance().getPackageManager().getApplicationInfo(pkg.packageName, 0);
                if (info.enabled)
                    return pkg;
                else
                    Log.v(TAG, "browser package `" + pkg.packageName + "` is disabled.");
            } catch (PackageManager.NameNotFoundException ex) {
                Log.v(TAG, "browser package `" + pkg.packageName + "` does not exist.");
            }
        }
        return null;
    }

    public static boolean run(Activity activity, String url, String title, String subtitle, HashMap<String, String> headers) {
        browserPackageInfo packageInfo = getPackageInfo();
        if (packageInfo == null)
            return false;

        Intent intent1 = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(packageInfo.packageName);
        intent.setComponent(new ComponentName(packageInfo.packageName, packageInfo.activityName));
        intent1.setData(Uri.parse(url));
        intent.putExtra("title", title);
        intent.putExtra("name", title);
        intent.putExtra("browser.extra.title", title);
        if (headers != null && headers.size() > 0) {
            try {
                JSONObject json = new JSONObject();
                for (String key : headers.keySet()) {
                    json.put(key, headers.get(key).trim());
                }
                intent.putExtra("browser.extra.http_header", json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (subtitle != null && !subtitle.isEmpty()) {
            intent.putExtra("browser.extra.subtitle", subtitle);
        }
        try {
            activity.startActivity(Intent.createChooser(intent1,getString(R.string.about_cherry_choice_browser)));
            return true;
        } catch (ActivityNotFoundException ex) {
            Log.e(TAG, "Can't run browser", ex);
            return false;
        }
    }
}