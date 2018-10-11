package in.shadowsoft.adminEnd;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class Sell extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CheckBox share_contact;
    String name,phone,pName,pdes,pCost,sAdd;
    EditText et_sName, et_sPhone, et_pName, et_pDes, et_pCost, et_sAdd;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        share_contact=findViewById(R.id.share_contact);
        et_sName=findViewById(R.id.et_sName);
        et_sPhone=findViewById(R.id.et_sPhone);
        et_pName=findViewById(R.id.et_pName);
        et_pDes=findViewById(R.id.et_pDes);
        et_pCost=findViewById(R.id.et_pCost);
        et_sAdd=findViewById(R.id.et_sAdd);
        setSupportActionBar(toolbar);
        db=openOrCreateDatabase("ciphers",MODE_PRIVATE,null);
        Cursor get=db.rawQuery("Select full_name,phone from tribal_user where status='1'",null);
        get.moveToFirst();
        name=get.getString(0);
        phone=get.getString(1);
        et_sName.setText(""+name);
        et_sPhone.setText(""+phone);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_sell);
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
        getMenuInflater().inflate(R.menu.sell, menu);
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
                    et_pName.setText(result.get(0));
                }
                break;
            }
            case 2: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et_pDes.setText(result.get(0));
                }
                break;
            }
            case 4: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et_sName.setText(result.get(0));
                }
                break;
            }
            case 5: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et_sAdd.setText(result.get(0));
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
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
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

    public void copy_details(View view) {
        if(share_contact.isChecked())
        {
            et_sName.setText(name);
            et_sPhone.setText(phone);
        }
        else
        {
            et_sName.setText("");
            et_sPhone.setText("");
        }

    }

    public void launch_image_upload(MenuItem item) {

        name=et_sName.getText().toString();
        phone=et_sPhone.getText().toString();
        pName=et_pName.getText().toString();
        pdes=et_pDes.getText().toString();
        pCost=et_pCost.getText().toString();
        sAdd=et_sAdd.getText().toString();

        if (name.isEmpty()||phone.isEmpty()||pName.isEmpty()||pdes.isEmpty()||pCost.isEmpty()||sAdd.isEmpty())
        {
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_LONG).show();
        }
        else{
            Intent up=new Intent(this,upload_product.class);
            up.putExtra("name",name);
            up.putExtra("phone",phone);
            up.putExtra("pName",pName);
            up.putExtra("pdes",pdes);
            up.putExtra("pCost",pCost);
            up.putExtra("sAdd",sAdd);
            this.startActivity(up);
            this.overridePendingTransition(0,0);
        }


    }

    public void speak_pName(View view) {
        promptSpeechInput(1);
    }

    public void speak_pDes(View view) {
        promptSpeechInput(2);
    }

    public void speak_sAdd(View view) {
        promptSpeechInput(5);
    }

    public void speak_sName(View view) {
        promptSpeechInput(4);
    }
}
