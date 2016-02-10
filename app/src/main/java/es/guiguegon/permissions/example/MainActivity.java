package es.guiguegon.permissions.example;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 10;
    public static final int REQUEST_CODE_PERMISSION_READ_CONTACTS = 11;
    public static final int REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION = 12;

    TextView writeExternalStorageStatusTextView;
    TextView readContactsStatusTextView;
    TextView locationTextView;

    int writeExternalStorageStatus;
    int readContactsStatus;
    int locationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button writeExternalStorageButton = (Button) findViewById(R.id.write_external_storage_button);
        Button readContactsButton = (Button) findViewById(R.id.read_contacts_button);
        Button locationGroupButton = (Button) findViewById(R.id.location_button);

        writeExternalStorageStatusTextView = (TextView) findViewById(R.id.write_external_storage_status);
        readContactsStatusTextView = (TextView) findViewById(R.id.read_contacts_status);
        locationTextView = (TextView) findViewById(R.id.location_status);

        writeExternalStorageButton.setOnClickListener(this);
        readContactsButton.setOnClickListener(this);
        locationGroupButton.setOnClickListener(this);
    }

    private void updatePermissionText(TextView textView, String permission, int permissionStatus) {
        switch (permission) {
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                writeExternalStorageStatus = permissionStatus;
                break;
            case Manifest.permission.READ_CONTACTS:
                readContactsStatus = permissionStatus;
                break;
            case Manifest.permission.ACCESS_FINE_LOCATION:
                locationStatus = permissionStatus;
                break;
        }

        switch (permissionStatus) {
            case PackageManager.PERMISSION_GRANTED:
                textView.setText(R.string.permission_status_granted);
                textView.setTextColor(Color.GREEN);
                break;
            case PackageManager.PERMISSION_DENIED:
                if (shouldShowRequestPermissionRationale(permission)) {
                    textView.setText(R.string.permission_status_denied);
                    textView.setTextColor(Color.YELLOW);
                } else {
                    textView.setText(R.string.permission_status_permanently_denied);
                    textView.setTextColor(Color.RED);
                }
                break;
        }
    }

    private void showRationaleDialog(final String permission) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setMessage(String.format(getString(R.string.permission_dialog_text), permission));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.action_continue),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (permission) {
                            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE);
                                break;
                            case Manifest.permission.READ_CONTACTS:
                                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PERMISSION_READ_CONTACTS);
                                break;
                            case Manifest.permission.ACCESS_FINE_LOCATION:
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
                                break;

                        }
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_external_storage_button:
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE);
                break;
            case R.id.read_contacts_button:
                requestPermission(Manifest.permission.READ_CONTACTS, REQUEST_CODE_PERMISSION_READ_CONTACTS);
                break;
            case R.id.location_button:
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION);
                break;
        }
    }

    private void requestPermission(String permission, int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            showRationaleDialog(permission);
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE:
                updatePermissionText(writeExternalStorageStatusTextView, Manifest.permission.WRITE_EXTERNAL_STORAGE, grantResults[0]);
                break;
            case REQUEST_CODE_PERMISSION_READ_CONTACTS:
                updatePermissionText(readContactsStatusTextView, Manifest.permission.READ_CONTACTS, grantResults[0]);
                break;
            case REQUEST_CODE_PERMISSION_ACCESS_FINE_LOCATION:
                updatePermissionText(locationTextView, Manifest.permission.ACCESS_FINE_LOCATION, grantResults[0]);
                break;
        }
    }
}
