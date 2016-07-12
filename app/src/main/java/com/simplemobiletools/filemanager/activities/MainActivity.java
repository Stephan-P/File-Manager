package com.simplemobiletools.filemanager.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.simplemobiletools.filemanager.R;
import com.simplemobiletools.filemanager.Utils;
import com.simplemobiletools.filemanager.models.FileDirItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tryInitFileManager();
    }

    private void tryInitFileManager() {
        if (Utils.hasStoragePermission(getApplicationContext())) {
            initializeFileManager();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
        }
    }

    private void initializeFileManager() {
        List<FileDirItem> items = getItems();
        Collections.sort(items);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeFileManager();
            } else {
                Utils.showToast(getApplicationContext(), R.string.no_permissions);
                finish();
            }
        }
    }

    private List<FileDirItem> getItems() {
        final List<FileDirItem> items = new ArrayList<>();
        final String path = Environment.getExternalStorageDirectory().toString();
        final File root = new File(path);
        File[] files = root.listFiles();
        for (File file : files) {
            final String curPath = file.getAbsolutePath();
            final String curName = Utils.getFilename(curPath);
            items.add(new FileDirItem(curPath, curName, file.isDirectory()));
        }
        return items;
    }
}
