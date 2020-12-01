package finance.pesa.sdk.utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import finance.pesa.sdk.PesaApplication;
import finance.pesa.sdk.view.UI.DashboardFragment;

public class PesaNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle1 = intent.getExtras();
            int notificationId = bundle1.getInt("notificationId");
            String msg = bundle1.getString("msg");
            String isNavigateToActivity = bundle1.getString(Constants.IS_NAVIGATE_Activity);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(notificationId);
            try {
                if (isNavigateToActivity.equalsIgnoreCase("true")) {
                    try {
                        if (DashboardFragment.Companion.getDashboardFragment() != null && !PesaApplication.Companion.getAppIsBackground()) {
                           /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(Constants.IS_NAVIGATE_Activity, true);
                            editor.apply();
                            Intent i = new Intent();
                            i.setClassName(Constants.PACKAGE_NAME, Constants.PACKAGE_NAME + ".MainActivity");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);*/
                            UserInterface.Companion.redirectToActivity();
                        } else {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(Constants.IS_NAVIGATE_Activity, true);
                            editor.apply();
                            Intent i = new Intent();
                            i.putExtra("redirect","isNavigateToActivity");
                            i.setClassName(Constants.PACKAGE_NAME, Constants.PACKAGE_NAME + ".MainActivity");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    } catch (Exception e) {
                        try {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(Constants.IS_NAVIGATE_Activity, true);
                            editor.apply();
                            Intent i = new Intent();
                            i.putExtra("redirect","isNavigateToActivity");
                            i.setClassName(Constants.PACKAGE_NAME, Constants.PACKAGE_NAME + ".MainActivity");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                } else {
                    if (DashboardFragment.Companion.getDashboardFragment() == null) {
                        Intent i = new Intent();
                        i.setClassName(Constants.PACKAGE_NAME, Constants.PACKAGE_NAME + ".MainActivity");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
