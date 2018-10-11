package in.shadowsoft.adminEnd;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Destination_location extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private GoogleMap mMap;
    int markerState = 0;
    private LocationManager locationManager;
    private static final long MIN_TIME = 0;
    private static final float MIN_DISTANCE = 0;
    String dAdd, sDes, fDes, dName;
    TextView tv_dName, tv_dAddress, tv_sDes;
    Double lat=0.0,lng=0.0;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_dName=findViewById(R.id.tv_dName);
        tv_dAddress=findViewById(R.id.tv_dAddress);
        tv_sDes=findViewById(R.id.tv_sDes);
        dName=getIntent().getStringExtra("Destination_name");
        sDes=getIntent().getStringExtra("short_des");
        fDes=getIntent().getStringExtra("full_des");
        dAdd=getIntent().getStringExtra("destination_address");
        tv_dName.setText(dName);
        tv_dAddress.setText(dAdd);
        tv_sDes.setText(sDes);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_destination);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        getMenuInflater().inflate(R.menu.destination_location, menu);
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
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
            Intent intent=new Intent(this,food_outlet.class);
            this.startActivity(intent);
            this.overridePendingTransition(0,0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launch_destination_image(MenuItem item) {
        if (markerState==0)
        {
            Toast.makeText(this,"Please Locate Destination", Toast.LENGTH_LONG).show();
        }
        else
        {
            try{
                Intent upload=new Intent(Destination_location.this, destination_image.class);
                upload.putExtra("Destination_name",getIntent().getStringExtra("Destination_name"));
                upload.putExtra("short_des",sDes);
                upload.putExtra("full_des", fDes);
                upload.putExtra("destination_address",dAdd);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
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
                        mMap.addMarker(new MarkerOptions().position(sydney).title(dName));
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
                        mMap.addMarker(new MarkerOptions().position(sydney).title(dName));
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
