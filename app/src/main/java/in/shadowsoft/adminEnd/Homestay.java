package in.shadowsoft.adminEnd;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Homestay extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText hsName,hsDes,ownName,ownPhone,hsAdd;
    CheckBox shareContact;
    String name,phone;
    SQLiteDatabase db;
    MediaPlayer mp=new MediaPlayer();
    int state=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homestay);
        hsName=findViewById(R.id.hsName);
        hsDes=findViewById(R.id.hsDes);
        ownName=findViewById(R.id.Owner_name);
        ownPhone=findViewById(R.id.owner_phone);
        hsAdd=findViewById(R.id.hsAdd);
        shareContact=findViewById(R.id.share_contact);
        db=openOrCreateDatabase("ciphers",MODE_PRIVATE,null);
        Cursor get=db.rawQuery("Select full_name,phone from tribal_user where status='1'",null);
        get.moveToFirst();
        db.execSQL("CREATE TABLE IF NOT EXISTS va_state(state integer);");
        name=get.getString(0);
        phone=get.getString(1);
        ownPhone.setText(""+phone);
        ownName.setText(""+name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_homestay);
        Cursor va_state = db.rawQuery("Select state from va_state;",null);
        try{
            va_state.moveToFirst();
            state=va_state.getInt(0);
        }
        catch (Exception e)
        {
            db.execSQL("Insert into va_state values(0);");
        }
        if(state==0)
        {

        }
        else if(state==1)
        {
            mp.release();
        }
        hsName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {

                    if (state==0){
                        mp.release();
                        mp=MediaPlayer.create(Homestay.this, R.raw.homestay_name_voice);
                        mp.start();
                    }
                }
            }
        });
        ownName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    if (state==0){
                        mp.release();
                        mp=MediaPlayer.create(Homestay.this, R.raw.name_voice);
                        mp.start();
                    }
                }
            }
        });
        ownPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    if (state==0){
                        mp.release();
                        mp=MediaPlayer.create(Homestay.this, R.raw.mobile_no);
                        mp.start();
                    }
                }
            }
        });
        hsAdd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    if (state==0){
                        mp.release();
                        mp=MediaPlayer.create(Homestay.this, R.raw.homestay_address_voice);
                        mp.start();
                    }
                }
            }
        });
        hsDes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    if (state==0){
                        mp.release();
                        mp=MediaPlayer.create(Homestay.this, R.raw.homestay_description_voice);
                        mp.start();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        mp.release();
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
        getMenuInflater().inflate(R.menu.hs, menu);
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
                    hsName.setText(result.get(0));
                }
                break;
            }
            case 2: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    hsDes.setText(result.get(0));
                }
                break;
            }
            case 3: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    ownName.setText(result.get(0));
                }
                break;
            }
            case 4: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    hsAdd.setText(result.get(0));
                }
                break;
            }

        }
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
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

    public void speak_add(View view) {
        mp.release();
        promptSpeechInput(4);
    }

    public void speak_ownName(View view) {
        mp.release();
        promptSpeechInput(3);
    }

    public void speak_des(View view) {

        mp.release();
        promptSpeechInput(2);
    }

    public void speak_name(View view) {
        mp.release();
        promptSpeechInput(1);
    }

    public void launch_map(MenuItem item) {
        mp.release();
        String Homestay_name=hsName.getText().toString();
        String Homestay_des=hsDes.getText().toString();
        String owner_name=ownName.getText().toString();
        String owner_phone=ownPhone.getText().toString();
        String Homestay_address=hsAdd.getText().toString();
        if (Homestay_name.equals("")||Homestay_des.equals("")||owner_phone.equals("")||owner_name.equals("")||Homestay_address.equals(""))
        {
            Toast.makeText(this,"Please fill all the fields", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent next=new Intent(Homestay.this, Homestay_location.class);
            next.putExtra("name",Homestay_name);
            next.putExtra("des",Homestay_des);
            next.putExtra("ownerName", owner_name);
            next.putExtra("ownerPhone", owner_phone);
            next.putExtra("address",Homestay_address);
            startActivity(next);
        }
    }

    public void copy_details(View view) {
        if(shareContact.isChecked())
        {
            ownName.setText(""+name);
            ownPhone.setText(""+phone);
        }
        else
        {
            ownName.setText("");
            ownPhone.setText("");
        }
    }
}
