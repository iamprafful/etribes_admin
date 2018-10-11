package in.shadowsoft.adminEnd;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView username,userphone;
    String id;
    SQLiteDatabase db;
    MediaPlayer mp=new MediaPlayer();
    int state=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        username=findViewById(R.id.user_name);
        userphone=findViewById(R.id.user_phone);
        db=openOrCreateDatabase("ciphers",MODE_PRIVATE,null);
        Cursor resultSet= db.rawQuery("select full_name,phone from tribal_user where status='1'",null);
        db.execSQL("CREATE TABLE IF NOT EXISTS va_state(state integer);");
        resultSet.moveToFirst();
        username.setText(resultSet.getString(0));
        userphone.setText(resultSet.getString(1));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Listening", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                promptSpeechInput(1);
            }

        });
        Cursor va_state = db.rawQuery("Select state from va_state;",null);
        try{
            va_state.moveToFirst();
            state=va_state.getInt(0);
        }
        catch (Exception e)
        {
            db.execSQL("Insert into va_state values(0);");
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }


    private void promptSpeechInput(int reqCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, reqCode);
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
                    //txtSpeechInput.setText("क्या आपका मतलब है-"+result.get(0)+"\n"+"हाॅं/नही");
                    String op=result.get(0);
                    CharSequence homestay_string_hindi="गृह निवास";
                    CharSequence homestay_string_hindi21="वस्तु";
                    CharSequence homestay_string_hindi22="सामान";
                    CharSequence homestay_string_hindi23="हैंडीक्राफ्ट";
                    CharSequence homestay_string_hindi31="गाइड";
                    CharSequence homestay_string_hindi32="टूरिस्ट";
                    CharSequence homestay_string_hindi33="पर्यटक";
                    CharSequence homestay_string_hindi34="मार्गदर्शक";
                    CharSequence homestay_string_hindi41="स्थान";
                    CharSequence homestay_string_hindi42="जगह";
                    CharSequence homestay_string_hindi51="उत्सव";
                    CharSequence homestay_string_hindi52="कार्यक्रम";
                    CharSequence homestay_string_english11="home";
                    CharSequence homestay_string_english12="Homestay";
                    CharSequence homestay_string_english13="Homestays";
                    CharSequence homestay_string_english2="sell";
                    CharSequence homestay_string_english3="guide";
                    CharSequence homestay_string_english4="destination";
                    CharSequence homestay_string_english5="event";
                    CharSequence homestay_string_english6="events";
                    boolean exists1=op.contains(homestay_string_english11);
                    boolean exists12=op.contains(homestay_string_english12);
                    boolean exists13=op.contains(homestay_string_english13);
                    boolean exists=op.contains(homestay_string_hindi);
                    boolean exists21=op.contains(homestay_string_hindi21);
                    boolean exists22=op.contains(homestay_string_hindi22);
                    boolean exists23=op.contains(homestay_string_hindi23);
                    boolean exists24=op.contains(homestay_string_english2);
                    boolean exists3=op.contains(homestay_string_english3);
                    boolean exists31=op.contains(homestay_string_hindi31);
                    boolean exists32=op.contains(homestay_string_hindi32);
                    boolean exists33=op.contains(homestay_string_hindi33);
                    boolean exists34=op.contains(homestay_string_hindi34);
                    boolean exists4=op.contains(homestay_string_english4);
                    boolean exists41=op.contains(homestay_string_hindi41);
                    boolean exists42=op.contains(homestay_string_hindi42);
                    boolean exists5=op.contains(homestay_string_english5);
                    boolean exists6=op.contains(homestay_string_english6);
                    boolean exists51=op.contains(homestay_string_hindi51);
                    boolean exists52=op.contains(homestay_string_hindi52);
                    if(exists||exists1||exists13||exists12)
                    {
                        Intent intent=new Intent(this,Homestay.class);
                        this.startActivity(intent);
                        this.overridePendingTransition(0,0);
                    }
                    else if(exists21||exists22||exists23||exists24)
                    {
                        Intent intent=new Intent(this,Sell.class);
                        this.startActivity(intent);
                        this.overridePendingTransition(0,0);
                    }
                    else if(exists3||exists31||exists32||exists33||exists34)
                    {
                        Intent intent=new Intent(this,guide.class);
                        this.startActivity(intent);
                        this.overridePendingTransition(0,0);
                    }
                    else if(exists4||exists41||exists42)
                    {
                        Intent intent=new Intent(this,destinations.class);
                        this.startActivity(intent);
                        this.overridePendingTransition(0,0);
                    }
                    else if(exists6||exists5||exists51||exists52)
                    {
                        Intent intent=new Intent(this,event.class);
                        this.startActivity(intent);
                        this.overridePendingTransition(0,0);
                    }
                    else {
                        Toast.makeText(this,"Not recognised", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            }

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder exit=new AlertDialog.Builder(this);
            exit.setTitle("\n" + "Quitting?");
            exit.setMessage("Are you sure you want to exit?");
            exit.setIcon(R.mipmap.tribal);
            exit.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    finishAffinity();
                }
            });
            exit.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            exit.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (state == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.main_vs_off, menu);
        }

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
        if (id==R.id.voice_assistance_icon)
        {
            if(state==0)
            {
                state=1;
                db.execSQL("update va_state set state=1;");
                item.setIcon(R.drawable.ic_record_voice_over_white_48dp);
            }
            else
            {
                state=0;
                db.execSQL("update va_state set state=0;");
                item.setIcon(R.drawable.ic_record_voice_over_cross_white_48dp);
            }
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }else if (id == R.id.nav_outlet) {
            Intent intent=new Intent(this,food_outlet.class);
            this.startActivity(intent);
            this.overridePendingTransition(0,0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launch_homestay(View view) {
        Intent intent=new Intent(this,Homestay.class);
        this.startActivity(intent);
        this.overridePendingTransition(0,0);

    }

    public void launch_sell(View view) {
        Intent intent=new Intent(this,Sell.class);
        this.startActivity(intent);
        this.overridePendingTransition(0,0);
    }


    public void destination(View view) {
        Intent intent=new Intent(this,destinations.class);
        this.startActivity(intent);
        this.overridePendingTransition(0,0);
    }

    public void launch_event(View view) {
        Intent intent=new Intent(this,event.class);
        this.startActivity(intent);
        this.overridePendingTransition(0,0);
    }

    public void logout(View view) {
        db = openOrCreateDatabase("ciphers",MODE_PRIVATE,null);
        db.execSQL("Update tribal_user set status='0' where id='"+getIntent().getStringExtra("id")+"'");
        Intent logout=new Intent(this,Login.class);
        startActivity(logout);
    }

    public void launch_guide(View view) {
        Intent intent=new Intent(this,guide.class);
        this.startActivity(intent);
        this.overridePendingTransition(0,0);
    }

    public void launch_food_outlet(View view) {
        Intent intent=new Intent(this,food_outlet.class);
        this.startActivity(intent);
        this.overridePendingTransition(0,0);
    }

//    public void launch_va(View view) {
//        promptSpeechInput(1);
//    }
}
