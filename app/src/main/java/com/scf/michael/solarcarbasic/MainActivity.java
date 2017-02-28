package com.scf.michael.solarcarbasic;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.scf.michael.solarcarbasic.api.Auth;
import com.scf.michael.solarcarbasic.api.TeamLocation;
import com.scf.michael.solarcarbasic.locations.MyLocationService;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

public class MainActivity extends BaseActivity {


    private LocationManager mLocationManager = null;
    private static final int REQUEST_CODE_LOCATION = 2;

    Intent locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        // Get the LocationManager object from the System Service LOCATION_SERVICE
        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        locationService = new Intent(MainActivity.this, MyLocationService.class);

        final Button button_start = (Button) findViewById(R.id.start_service);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button_start.getText() == getString(R.string.stop_service)) {
                    button_start.setText(getString(R.string.start_service));
                    stop_service();
                } else {
                    button_start.setText(getString(R.string.stop_service));
                    enable_service();
                }
            }
        });

        Realm realm = getRealm();
        Auth defaultUser = Auth.getInstance();
        realm.beginTransaction();
        defaultUser.setUsername("tom");
        defaultUser.setPassword("1992joy321");
        realm.commitTransaction();

        defaultUser.login();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //When the screen rotates, OnDestroy is called and we need to remove the updates.
    //http://developer.android.com/reference/android/app/Activity.html#ActivityLifecycle
    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void enable_service()
    {
        //If GPS is not enabled, open ask user if they want to open it
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OpenLocationSettings();
        }

        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_CODE_LOCATION );
        } else {
            startService(locationService);
        }
    }

    private void stop_service()
    {
        stopService(locationService);
    }

    //Call this to ask user to set location settings
    public void OpenLocationSettings(){

        //////////AlertDialog prompting location settings
        AlertDialog.Builder NoLocationAlertDialog = new AlertDialog.Builder(MainActivity.this);
        // Setting Dialog Title
        NoLocationAlertDialog.setTitle("GPS not Enabled!");
        // Setting Dialog Message
        NoLocationAlertDialog.setMessage("GPS is not enabled, do you want to open settings?");
        // Setting Positive "Yes" Button
        NoLocationAlertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Activity transfer to location settings
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        // Setting Negative "NO" Button
        NoLocationAlertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        //Show AlertDialog
        NoLocationAlertDialog.show();

    }


    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length >= 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // success!
                enable_service();
            } else {
                // Permission was denied or request was cancelled
                Toast tp = Toast.makeText(getApplicationContext(), "This app will not work without that permission", Toast.LENGTH_LONG);
                tp.getView().setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        enable_service();
                                                    }
                                                }
                );
                tp.show();
            }
        }
    }

}
