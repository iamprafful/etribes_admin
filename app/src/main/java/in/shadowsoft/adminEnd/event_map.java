package in.shadowsoft.adminEnd;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class event_map extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.event_map, menu);
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
            Intent intent=new Intent(this,Sell.class);
            this.startActivity(intent);
            this.overridePendingTransition(0,0);
            // Handle the camera action
        } else if (id == R.id.nav_event) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

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

        if (id == R.id.action_settings) {
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
