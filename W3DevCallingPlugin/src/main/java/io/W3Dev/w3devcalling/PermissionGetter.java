package io.W3Dev.w3devcalling;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionGetter {

    private static final int RC_CALL = 111;

    public void RequestPermissions(Context context) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString() + "\nPlease grant all the permissions to move forward", Toast.LENGTH_SHORT).show();
                RequestPermissions(context);
            }
        };
        TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.MODIFY_AUDIO_SETTINGS
                        , Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.BLUETOOTH)
                .check();
    }


    @AfterPermissionGranted(RC_CALL)
    public void getperms(Context context, String[] permissionList) {
        if (EasyPermissions.hasPermissions(context, permissionList)) {
            Toast.makeText(context, "Permission Already Granted", Toast.LENGTH_SHORT).show();


        } else {
            EasyPermissions.requestPermissions((Activity) context, "Need some permissions", RC_CALL, permissionList);
        }


    }

}
