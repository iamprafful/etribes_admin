package in.shadowsoft.adminEnd;

import android.*;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class food_outlet extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SQLiteDatabase db;
    CheckBox share_user_contact;
    String oName, oPhone, mName, tagLine, oAdd;
    EditText et_oName, et_oPhone, et_mName, et_tagLine, et_oAdd;
    Double currentLat=0.0, currentlng=0.0;
    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_outlet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        share_user_contact=findViewById(R.id.share_user_contact);
        et_oName=findViewById(R.id.et_oName);
        et_oPhone=findViewById(R.id.et_oPhone);
        et_mName=findViewById(R.id.et_mName);
        et_tagLine=findViewById(R.id.et_tagLine);
        et_oAdd=findViewById(R.id.et_oAdd);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        db=openOrCreateDatabase("ciphers",MODE_PRIVATE,null);
        Cursor get=db.rawQuery("Select full_name,phone from tribal_user where status='1'",null);
        get.moveToFirst();
        mName=get.getString(0);
        oPhone=get.getString(1);
        et_mName.setText(""+mName);
        et_oPhone.setText(""+oPhone);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLat=location.getLatitude();
                currentlng=location.getLongitude();
                //Toast.makeText(food_outlet.this, currentLat.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                AlertDialog.Builder builder = new AlertDialog.Builder(food_outlet.this);
                builder.setTitle("Need your Location");
                builder.setMessage("Turn on GPS")
                        .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                AlertDialog dailog=builder.create();
                dailog.show();
                Button negative = dailog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setTextColor(Color.BLACK);
                Button positive = dailog.getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setTextColor(Color.BLACK);

            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.food_outlet, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_destination) {
            Intent intent=new Intent(this,destinations.class);
            this.startActivity(intent);
            this.overridePendingTransition(0,0);
            // Handle the camera action
        } else if (id == R.id.nav_event) {
            Intent intent=new Intent(this,event.class);
            this.startActivity(intent);
            this.overridePendingTransition(0,0);

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_guide) {
            Intent intent=new Intent(this,guide.class);
            this.startActivity(intent);
            this.overridePendingTransition(0,0);

        } else if (id == R.id.nav_homestay) {
            Intent intent=new Intent(this,Homestay.class);
            this.startActivity(intent);
            this.overridePendingTransition(0,0);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_log_out) {
            db = openOrCreateDatabase("ciphers",MODE_PRIVATE,null);
            db.execSQL("Update tribal_user set status='0' where id='"+getIntent().getStringExtra("id")+"'");
            Intent logout=new Intent(this,Login.class);
            startActivity(logout);
        } else if (id == R.id.nav_sell) {
            Intent intent=new Intent(this,Sell.class);
            this.startActivity(intent);
            this.overridePendingTransition(0,0);
        }else if (id == R.id.nav_home) {
            Intent intent=new Intent(this,MainActivity.class);
            this.startActivity(intent);
            this.overridePendingTransition(0,0);
        }else if (id == R.id.nav_outlet) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launch_map_food(MenuItem item) {
        oName=et_oName.getText().toString();
        oPhone=et_oPhone.getText().toString();
        mName=et_mName.getText().toString();
        tagLine=et_tagLine.getText().toString();
        oAdd=et_oAdd.getText().toString();

        if (oName.isEmpty()||oPhone.isEmpty()||mName.isEmpty()||tagLine.isEmpty()||oAdd.isEmpty())
        {
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_LONG).show();
        }
        else{
            Intent up=new Intent(this,food_outlet_location.class);
            up.putExtra("oName",oName);
            up.putExtra("oPhone",oPhone);
            up.putExtra("mName",mName);
            up.putExtra("tagLine",tagLine);
            up.putExtra("oAdd",oAdd);
            up.putExtra("currentLat",currentLat.toString());
            up.putExtra("currentLng",currentlng.toString());
            this.startActivity(up);
            this.overridePendingTransition(0,0);
        }
    }

    public void copy_user_details(View view) {
        if(share_user_contact.isChecked())
        {
            et_mName.setText(mName);
            et_oPhone.setText(oPhone);
        }
        else
        {
            et_mName.setText("");
            et_oPhone.setText("");
        }
    }
    private void promptSpeechInput(int rqCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, rqCode);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et_oName.setText(result.get(0));
                }
                break;
            }
            case 2: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et_tagLine.setText(result.get(0));
                }
                break;
            }
            case 3: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et_mName.setText(result.get(0));
                }
                break;
            }
            case 4: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et_oAdd.setText(result.get(0));
                }
                break;
            }

        }
    }

    public void speak_oAdd(View view) {
        promptSpeechInput(4);
    }

    public void speak_mName(View view) {
        promptSpeechInput(3);
    }

    public void speak_oName(View view) {
        promptSpeechInput(1);
    }

    public void speak_tagLine(View view) {
        promptSpeechInput(2);
    }
}
