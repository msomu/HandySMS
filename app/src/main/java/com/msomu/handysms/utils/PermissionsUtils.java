package com.msomu.handysms.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by msomu on 13/03/16.
 */
public class PermissionsUtils {
    //region MY_PERMISSIONS_REQUEST
    public static final int MY_PERMISSIONS_REQUEST_EXAMPLE = 1;
    //endregion


    public static boolean checkAndRequest(@NonNull final Activity activity, @NonNull final String permission, final int requestCode, String messagePermission, DialogInterface.OnClickListener onCancelListener) {
        boolean result = false;
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {


                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(messagePermission).setPositiveButton(activity.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                    }
                }).setNegativeButton(activity.getResources().getString(android.R.string.cancel), onCancelListener).show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);


                // MY_PERMISSIONS_REQUEST is an app-defined int constant.
                // The callback method gets the result of the request.
            }
        } else {
            result = true;
        }
        return result;
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }
}
