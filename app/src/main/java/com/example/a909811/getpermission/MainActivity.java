package com.example.a909811.getpermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this,permissions[0]) != PackageManager.PERMISSION_GRANTED){
                showDialogTipUserRequestPermission();
            }

        }
    }

    private void showDialogTipUserRequestPermission() {
        new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("需要权限")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       finish();
                    }
                }).setCancelable(false).show();
    }

    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this,permissions,321);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b){
                        showDialogTipUserGoToAppSetting();
                    }else {
                        finish();
                    }
                }
            }
        }
    }

    private void showDialogTipUserGoToAppSetting() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许应用使用存储权限赖保存用户数据")
                .setPositiveButton("立刻开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        startActivityForResult(intent,123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ContextCompat.checkSelfPermission(this,permissions[0]) != PackageManager.PERMISSION_GRANTED){
                    showDialogTipUserGoToAppSetting();
                }else {
                    if (dialog != null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    Toast.makeText(this,"权限获取成功",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
