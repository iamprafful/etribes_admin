package in.shadowsoft.adminEnd;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteTransactionListener;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class food_outlet_location extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    SQLiteDatabase db;
    private GoogleMap mMap;
    int markerState = 0;
    Double lat=0.0,lng=0.0;
    Double currentLat=0.0,currentLng=0.0;
    String oName, oPhone, mName, tagLine, oAdd;
    TextView tv_name, tv_add, tv_phone;
    SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_outlet_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_name=findViewById(R.id.o_name);
        tv_add=findViewById(R.id.o_address);
        tv_phone=findViewById(R.id.o_phone);
        currentLat=Double.parseDouble(getIntent().getStringExtra("currentLat"));
        currentLng=Double.parseDouble(getIntent().getStringExtra("currentLng"));
        oName=getIntent().getStringExtra("oName");
        oPhone=getIntent().getStringExtra("oPhone");
        mName=getIntent().getStringExtra("mName");
        tagLine=getIntent().getStringExtra("tagLine");
        oAdd=getIntent().getStringExtra("oAdd");
        tv_name.setText(oName);
        tv_add.setText(oAdd);
        tv_phone.setText(mName+"\n+91 "+oPhone);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(food_outlet_location.this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            if(currentLat!=0.0&&currentLng!=0.0){
                LatLng currentLatLng = new LatLng(currentLat, currentLng);
                float zoomLevel = 10.0f;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoomLevel));
            }

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng position) {
                    if(markerState==0)
                    {
                        lat=position.latitude;
                        lng=position.longitude;
                        markerState=1;
                        //Toast.makeText(Homestay_location.this, position.latitude + " : " + position.longitude, Toast.LENGTH_SHORT).show();
                        LatLng sydney = new LatLng(position.latitude, position.longitude);
                        mMap.addMarker(new MarkerOptions().position(sydney).title(oName));
                        float zoomLevel = 17.0f;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
                    }
                    else {
                        markerState=1;
                        mMap.clear();
                        lat=position.latitude;
                        lng=position.longitude;
                        //Toast.makeText(Homestay_location.this, position.latitude + " : " + position.longitude, Toast.LENGTH_SHORT).show();
                        LatLng sydney = new LatLng(position.latitude, position.longitude);
                        mMap.addMarker(new MarkerOptions().position(sydney).title(oName));
                        float zoomLevel = 17.0f;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
                    }
                }
            });
            return;
        }
        else if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        else if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);;
        }
        else if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }

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
        getMenuInflater().inflate(R.menu.food_outlet_location, menu);
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

    public void launch_upload_food(MenuItem item) {
        if (markerState==0)
        {
            Toast.makeText(this,"Please select Outlet location", Toast.LENGTH_LONG).show();
        }
        else
        {
            try{
                Intent upload=new Intent(food_outlet_location.this, food_outlet_upload.class);
                upload.putExtra("foName",oName);
                upload.putExtra("ftagLine",tagLine);
                upload.putExtra("fmName",mName);
                upload.putExtra("foPhone",oPhone);
                upload.putExtra("foAdd",oAdd);
                upload.putExtra("lat",lat.toString());
                upload.putExtra("lng",lng.toString());
                startActivity(upload);
            }
            catch (Exception ex)
            {
                Toast.makeText(this,ex.getMessage(), Toast.LENGTH_LONG).show();
            }


        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == 1) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                //Toast.makeText(this, "IMEI No.: " + tm.getDeviceId(), Toast.LENGTH_LONG).show();
                //IMEI=tm.getDeviceId();
                //  Toast.makeText(this, "Thank You for granting permissions", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                //Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        if (requestCode == 2) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                //Toast.makeText(this, "IMEI No.: " + tm.getDeviceId(), Toast.LENGTH_LONG).show();
                //IMEI=tm.getDeviceId();
                // Toast.makeText(this, "Thank You for granting permissions", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                // Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
