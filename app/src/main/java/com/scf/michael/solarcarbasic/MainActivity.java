package com.scf.michael.solarcarbasic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
//import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.BatteryManager;
import android.os.Environment;
import android.provider.Settings;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
//import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.TextView;
import android.widget.Toast;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends Activity implements LocationListener {


    public static final String BASE_URL = "https://solar.tpmullan.com/api/";
    protected LocationManager locationManager; //The Location Manager we use to get Lat and Long
    boolean GPSEnabled = true;
    private static final int REQUEST_CODE_LOCATION = 2;
    private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private ClosedTrackSolarApiEndpoint apiService = retrofit.create(ClosedTrackSolarApiEndpoint.class);

    //API to connect to Google Play
    private GoogleApiClient mGoogleApiClient;
    //public int REQUEST_CODE;
    //public int JudgeIndex;
    public int TeamIndex=-1;
    public static LocationListener myLocationListener;
    TeamLocation oldLoc = new TeamLocation();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //TextView TxtID = (TextView) findViewById(R.id.Phone_ID);
        // Get the LocationManager object from the System Service LOCATION_SERVICE
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        //Toast.makeText(getBaseContext(), "init settings", Toast.LENGTH_SHORT).show();
        initLocationSettings();
    }


    //Call this to initialize location settings
    public void initLocationSettings() {
        //Toast.makeText(this, "int settings", Toast.LENGTH_SHORT).show();
        GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //If GPS is not enabled, open ask user if they want to open it
        if (!GPSEnabled) {
            OpenLocationSettings();
        }

        //OpenLocationSettings();
        //if (canAccessLocation()) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Create a criteria object needed to retrieve the provider
            Criteria criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            //criteria.setAccuracy(Criteria.ACCURACY_FINE);
            // Get the name of the best available provider
            String provider = locationManager.getBestProvider(criteria, true);
            // We can use the provider immediately to get the last known location
            //Location location = locationManager.getLastKnownLocation(provider);
            //Remove existing updates before starting a new update
            locationManager.removeUpdates(this);
            // request that the provider send this activity GPS updates every 10 seconds
            //locationManager.requestSingleUpdate(provider, this, null);
            locationManager.requestLocationUpdates(provider, 2000, 0, this );  //Update every 2 sec
            //locationManager.requestLocationUpdates(provider, 10000, 30, this ); //30 meters gets rid of noise from stop lights




        } else {
            // Request missing location permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION);

            //request23Permissions(LOCATION_PERMS, LOCATION_REQUEST);
        }
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Remove existing updates before starting a new update
            locationManager.removeUpdates(this);
        }
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
    //Every time the location changes (which is checked either every 2 or 10 secs)
    //get the team location, set it to the txt box and send it to the server
    @Override
    public void onLocationChanged(Location location) {

        // if the accuracy is really bad, back out
        if (location.getAccuracy()>100) {
            //Toast.makeText(getBaseContext(), " ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oldLoc != null){
            //if(oldLoc.getAccuracy()<location.getAccuracy() && oldLoc.getUpdatedAt()>Calendar.getInstance().getTime()){  //if the old location is more accurate, back out
            if (1==0){  //always false for now
                return;
            }
        }


        //Display the Lat and Long in the txt boxes
        TeamLocation newLoc = new TeamLocation();
        newLoc.setLongitude(location.getLongitude());
        newLoc.setLatitude(location.getLatitude());
        newLoc.setAltitude(location.getAltitude());
        newLoc.setAccuracy(location.getAccuracy());
        newLoc.setId(TeamIndex+1);


        //TextView TxtJudge = (TextView) findViewById(R.id.JudgeName);
        //TextView TxtTeamName = (TextView) findViewById(R.id.TeamName);

        Integer TeamIndexWeb=0;

        //if (mMap != null) {
        //    drawMarker(location);
        //}


        //newLoc.setTeamId(TeamIndex + 1);
        newLoc.setUpdatedAt(Calendar.getInstance().getTime().toString());

        TeamIndexWeb=TeamIndex+1;
        /*if (TeamIndex <=3)
        {
            TeamIndexWeb=TeamIndex+2;

        } else {
            TeamIndexWeb=TeamIndex+4;
        }*/

        //send data to server
        Call<TeamLocation> call = apiService.updateTeamLocation(TeamIndexWeb, newLoc);

        String Phone_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Float batteryPct = batteryInfo();

        try {
            //TextView TxtStatus = (TextView) findViewById(R.id.Status);
            String status = "N/A";
            String judge="Unknown";
            String team="Unknown";
            //if (TxtStatus.getText().toString() != null){status=TxtStatus.getText().toString();}
            //if (TxtJudge.getText().toString() != null){judge=TxtJudge.getText().toString();}
            //if (TxtTeamName.getText().toString() != null){team=TxtTeamName.getText().toString();}


            createFile(Phone_ID + ", " + newLoc.getUpdatedAt() + ", " + newLoc.getLatitude().toString() + ", " + newLoc.getLongitude().toString() +", " +batteryPct.toString() + ", " + newLoc.getAltitude()+ ", " + status+ ", " + judge+ ", " + team+", "+ location.getAccuracy()+", "+location.getSpeed() +", "+location.getBearing() ); //CreateFile -writes important data to a csv file
        } catch (Exception e) {
            createFile(Phone_ID + ", " + newLoc.getUpdatedAt() + ", " + newLoc.getLatitude().toString() + ", " + newLoc.getLongitude().toString() +", "+ batteryPct.toString()+ ", 0, N/A, N/A, N/A, 0, 0");
        }

        // Send data to Shane's server
        call.enqueue(new Callback<TeamLocation>() {
            @Override  /*If you get a response, do this*/
            public void onResponse(Response<TeamLocation> response, Retrofit retrofit) {
                int statusCode = response.code();
                TeamLocation receivedLoc = response.body();

                //Toast.makeText(getBaseContext(), receivedLoc.getUpdatedAt(), Toast.LENGTH_SHORT).show();

                //TextView TxtLastUpdated = (TextView) findViewById(R.id.LastUpdated);
                //TxtLastUpdated.setText("Last Updated: ".concat(Calendar.getInstance().getTime().toString())); //Display the Updated Date
            }

            @Override /*if you get an error, do this*/
            public void onFailure(Throwable t) {
                // Log error here since request failed
            }

        });
        oldLoc=newLoc;

    }

    public float batteryInfo (){

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float)scale;

        return batteryPct;
    }

    public void createFile(String string) {

        String Phone_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Calendar c = Calendar.getInstance();
        String today_string =  String.valueOf(c.get(Calendar.YEAR)).concat("-").concat(String.valueOf(c.get(Calendar.MONTH)+1)).concat("-").concat(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

        String filename= "SolarCarTracker ".concat(Phone_ID).concat(" ").concat(today_string) ;


        //String filename = "SolarCarTracker";
        FileOutputStream outputStream;
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        final File myDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);


        try {
            File myFile = new File(path+"/"+filename+".txt");

            if(!myDir.exists()){
                myDir.mkdirs();//make the folders where we write folders
            }

            if ( !myFile.exists()) {
                myFile.createNewFile();  //make the file where we write information

                FileOutputStream fOut = new FileOutputStream(myFile, true); //true means we append
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append("Phone ID, Time, Latitude, Longitude, , Battery Pct, Altitude, Status, Judge, Team, Accuracy, Speed, Bearing");
                myOutWriter.append("\n");
                myOutWriter.close();
                fOut.close();


            }


            FileOutputStream fOut = new FileOutputStream(myFile, true); //true means we append
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(string);
            myOutWriter.append("\n");
            myOutWriter.close();
            fOut.close();

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    @Override
    public void onProviderEnabled(String locationManager) {
        //Toast.makeText(getBaseContext(),"enabled", Toast.LENGTH_SHORT).show();
        initLocationSettings();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                //Toast.makeText(this, "Status Changed: Out of Service", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                //Toast.makeText(this, "Status Changed: Temporarily Unavailable", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.AVAILABLE:
                //Toast.makeText(this, "Status Changed: Available", Toast.LENGTH_SHORT).show();
                break;
        }
        //initLocationSettings();

    }
    @Override
    public void onProviderDisabled(String locationManager) {
        OpenLocationSettings();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // success!
                Location myLocation =
                        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }

}
