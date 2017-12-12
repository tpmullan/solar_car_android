package com.scf.michael.solarcarbasic;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.scf.michael.solarcarbasic.api.Auth;
import com.scf.michael.solarcarbasic.api.TeamLocation;
import com.scf.michael.solarcarbasic.locations.MyLocationService;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.fabric.sdk.android.Fabric;

import static com.orm.util.Collection.list;

public class MainActivity extends BaseActivity {


    private LocationManager mLocationManager = null;
    private static final int REQUEST_CODE_LOCATION = 2;
    private String Phone_ID;

    Intent locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        //String Phone_ID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        Phone_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        // Get the LocationManager object from the System Service LOCATION_SERVICE
        mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        locationService = new Intent(MainActivity.this, MyLocationService.class);
        final TextView txt_Phone_ID = (TextView) findViewById(R.id.Phone_ID);
        txt_Phone_ID.setText(Phone_ID);


        final Button button_start = (Button) findViewById(R.id.start_service);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button_start.getText() == getString(R.string.stop_service)) {
                    button_start.setText(getString(R.string.start_service));
                    stop_service();
                    sendMassDataHandler.removeCallbacks(runnableCodeMassData); //stop the runner
                } else {
                    button_start.setText(getString(R.string.stop_service));
                    enable_service();
                    sendMassDataHandler.postDelayed(runnableCodeMassData,30000); //start the mass data in 30 sec
                }

            }
        });

        final Button button_load_data = (Button) findViewById(R.id.upload_data);
        button_load_data.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (button_load_data.getText() == "DO NOT CLICK") {
                    button_load_data.setText("Why U Click?!?");
                    //enable_service_data();
                    SendManyData sender = new SendManyData(sendMassDataHandler, runnableCodeMassData);
                    sender.setShouldRunAgain(false);
                    sender.SendData(Phone_ID);
                    //DataDumptoCSV();

                } else {
                    button_load_data.setText("DO NOT CLICK");
                    stop_service_data();
                }
            }


        });


        Auth defaultUser = Auth.getInstance();
        //realm.beginTransaction();
        defaultUser.setUsername("tom");
        defaultUser.setPassword("1992joy321");
        //realm.commitTransaction();
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
            //handler.post(runnableCode);
            sendMassDataHandler.removeCallbacks(runnableCodeMassData);
            sendMassDataHandler.postDelayed(runnableCodeMassData,30000);
        }
    }

    private void stop_service()
    {
        stopService(locationService);
        //handler.removeCallbacks(runnableCode);
        //stopService(myTimer);
        sendMassDataHandler.removeCallbacks(runnableCodeMassData);
    }


    private void enable_service_data()
    {
        //new SendDataToServer().sendDatum(getApplicationContext());
        //new SendDataToServer().sendData(getApplicationContext(),100);
        new SendDataToServer().sendSlowData(getApplicationContext(),1000);
    }

    private void stop_service_data()
    {
        //stopService(SendMoreData);
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
                                                    public void onClick(View view) {enable_service();
                                                    }
                                                });
                tp.show();
            }
        }
    }
    // Create the Handler object (on the main thread by default)
    Handler handler = new Handler();
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            //Log.d("Handlers", "Called on main thread");
            // Repeat this the same runnable code block again another 2 seconds
            //Toast.makeText(getApplicationContext(), "wakka wakka", Toast.LENGTH_SHORT).show();
            new SendDataToServer().sendData(getApplicationContext(),50);
            handler.postDelayed(runnableCode, 35000);
        }
    };


    Handler sendMassDataHandler = new Handler();
    private Runnable runnableCodeMassData = new Runnable(){
        @Override
        public void run(){
            SendManyData sender = new SendManyData(sendMassDataHandler, runnableCodeMassData);
            sender.SendData(Phone_ID);
        }
    };

    public void DataDumptoCSV(){
        final List<TeamLocation> tmpLocations = Select.from(TeamLocation.class).list();
        for (TeamLocation loc:tmpLocations) {
            createFile(loc.getuuid("a").toString(), loc.getCreatedAt(), loc.getLatitude().toString()
                    , loc.getLongitude().toString(), loc.getRemoteId().toString());

        }

    }
    public void createFile(String Phone_ID, String Collect_Time, String Lat, String Longitude, String Response) {
    //public void createFile(String string) {
        //public void createFile(String string) {
        Calendar calendar = Calendar.getInstance();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // you can go on

            //String Phone_ID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            //Calendar calendar = Calendar.getInstance();
            SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd");
            //SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            String filename= "SolarCarTrackerHistory-" + Phone_ID + "-" + tf.format(calendar.getTime()) +".txt";

            final File myDir = Environment.getExternalStorageDirectory().getAbsoluteFile();

            try {
                File myFile = new File(myDir.getPath(), filename);

                if(!myDir.exists()){
                    myDir.mkdirs(); //make the folders where we write folders
                }

                if ( !myFile.exists()) {
                    myFile.createNewFile();  //make the file where we write information

                    FileOutputStream fOut = new FileOutputStream(myFile, true); //true means we append
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append("Phone ID, Collect Time, Latitude, Longitude, Response");
                    myOutWriter.append("\n");
                    myOutWriter.close();
                    fOut.close();
                }

                FileOutputStream fOut = new FileOutputStream(myFile, true); //true means we append
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(Phone_ID+", "+Collect_Time+", "+Lat+", "+Longitude+", "+Response);
                myOutWriter.append("\n");
                myOutWriter.close();
                fOut.close();

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


}
