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

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.scf.michael.solarcarbasic.api.Auth;
import com.scf.michael.solarcarbasic.api.TeamLocation;
import com.scf.michael.solarcarbasic.locations.MyLocationService;

public class MainActivity extends Activity {


    private LocationManager mLocationManager = null;
    private static final int REQUEST_CODE_LOCATION = 2;

    Intent locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

  /*      Configuration dbConfiguration = new Configuration.Builder(this)
                .setDatabaseName("SolarCar.db")
                .addModelClass(Auth.class)
                .addModelClass(TeamLocation.class)
                .create();

        ActiveAndroid.initialize(dbConfiguration);

        Auth defaultUser = Auth.getInstance();
        defaultUser.setUsername("tom");
        defaultUser.setPassword("1992joy321");
        defaultUser.login();*/
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

        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_CODE_LOCATION );
        }
        startService(locationService);
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
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // success!
                //Location myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }

}
