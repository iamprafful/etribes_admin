package in.shadowsoft.adminEnd;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {
    EditText pwd;
    CheckBox show;
    Button login;
    EditText phone;
    String final_url;
    TelephonyManager tm;
    SQLiteDatabase db;
    MediaPlayer mp=new MediaPlayer();
    int state=0;
    ImageView im_va;
    TextView tv_va;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pwd = findViewById(R.id.pwd);
        show = findViewById(R.id.showCheck);
        im_va=findViewById(R.id.img_va);
        tv_va=findViewById(R.id.tv_va);
        login = findViewById(R.id.login);
        phone = findViewById(R.id.phone);
        try {
            db = openOrCreateDatabase("ciphers",MODE_PRIVATE,null);
            Cursor resultSet = db.rawQuery("Select id from tribal_user where status='1'",null);
            db.execSQL("CREATE TABLE IF NOT EXISTS va_state(state integer);");
            resultSet.moveToFirst();
            String id = resultSet.getString(0);
            Intent already_login = new Intent(this, MainActivity.class);
            already_login.putExtra("id", id);
            startActivity(already_login);
        }
        catch (Exception e)
        {
            //Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "IMEI No.: " + tm.getDeviceId(), Toast.LENGTH_LONG).show();
            //IMEI=tm.getDeviceId();
            phone.setText("" + tm.getLine1Number());
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 112);

        }
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
            im_va.setImageResource(R.drawable.ic_record_voice_over_cross_white_48dp);
            tv_va.setText("Tap to turn off Voice Assistance");
        }
        else if(state==1)
        {
            im_va.setImageResource(R.drawable.ic_record_voice_over_white_48dp);
            tv_va.setText("Tap to turn on Voice Assistance");
        }
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    if (state==0){
                        mp.release();
                        mp=MediaPlayer.create(Login.this, R.raw.mobile_number_voice);
                        mp.start();
                    }
                }
            }
        });
        pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    if (state==0){
                        mp.release();
                        mp=MediaPlayer.create(Login.this, R.raw.pin_voice);
                        mp.start();
                    }
                }
            }
        });
    }

    public void showPwd(View view) {
        pwd.setSelection(pwd.getText().length());

        if (show.isChecked()) {
            pwd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            Toast viible = Toast.makeText(this, "Password is Visible", Toast.LENGTH_LONG);
            viible.show();

        } else {
            pwd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            Toast hide = Toast.makeText(this, "Password is Hidden", Toast.LENGTH_LONG);
            hide.show();

        }
        pwd.setSelection(pwd.getText().length());
        pwd.requestFocus();
    }

    public void Validate(View view) {
        String password = pwd.getText().toString();
        String ph = phone.getText().toString();
        if (password.isEmpty() && ph.isEmpty()) {
            pwd.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 148, 148)));
            pwd.setHintTextColor(ColorStateList.valueOf(Color.rgb(255, 148, 148)));
            phone.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 148, 148)));
            phone.setHintTextColor(ColorStateList.valueOf(Color.rgb(255, 148, 148)));
            Toast t = Toast.makeText(this, "Please Enter Phone & PIN", Toast.LENGTH_SHORT);
            t.show();
            phone.requestFocus();
        } else if (ph.isEmpty()) {
            phone.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 148, 148)));
            phone.setHintTextColor(ColorStateList.valueOf(Color.rgb(255, 148, 148)));
            Toast t1 = Toast.makeText(this, "Please Enter Phone", Toast.LENGTH_SHORT);
            t1.show();
            phone.requestFocus();
        } else if (password.isEmpty()) {
            pwd.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(255, 148, 148)));
            pwd.setHintTextColor(ColorStateList.valueOf(Color.rgb(255, 148, 148)));
            Toast t = Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT);
            t.show();
            pwd.requestFocus();
        } else {
            final_url = "http://ciphers.shadowsoft.in/tribal/auth.php?phone=" + ph + "&password=" + password;
            try {
                Log.e("sql", final_url);
                getJSON(final_url);
            } catch (Exception ex) {
                Toast e = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG);
                e.show();
            }


        }

    }

    public void signup(View view) {
        mp.release();
        Intent intent = new Intent(this, Sign_up.class);
        startActivity(intent);
        this.overridePendingTransition(0, 0);
    }

    public void getServerMsg(String json) throws JSONException {
        if (json.equals("0"))
        {
            Toast.makeText(Login.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        else{

            //creating a json array from the json string
            JSONArray jsonArray = new JSONArray(json);

            //looping through all the elements in json array
            String[] id = new String[1];
            String[] name = new String[1];
            String[] phone = new String[1];
            String[] password = new String[1];

            for (int i = 0; i < 1; i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                id[i] = obj.getString("id");
                name[i] = obj.getString("name");
                phone[i] = obj.getString("phone");
                password[i] = obj.getString("password");

            }
            if (id[0].equals("404")) {
                Toast err = Toast.makeText(this, "Invalid Phone or password", Toast.LENGTH_LONG);
                err.show();
            } else {
                db = openOrCreateDatabase("ciphers",MODE_PRIVATE,null);
                db.execSQL("CREATE TABLE IF NOT EXISTS tribal_user(id VARCHAR,full_name VARCHAR, phone VARCHAR, password VARCHAR, status VARCHAR);");
                Cursor resultSet = db.rawQuery("Select count(*) from tribal_user where id='"+id[0]+"'",null);
                resultSet.moveToFirst();
                int count = resultSet.getInt(0);
                if (count==0)
                {
                    db.execSQL("INSERT INTO tribal_user VALUES('"+id[0]+"','"+name[0]+"','"+phone[0]+"','"+password[0]+"','1');");
                }
                else
                {
                    db.execSQL("Update tribal_user set status='1' where id='"+id[0]+"'");
                }
                Intent successful_login = new Intent(this, MainActivity.class);
                successful_login.putExtra("id", id[0]);
                this.startActivity(successful_login);
            }

        }


    }

    private void getJSON(final String urlWebService) {
        /*
        * As fetching the json string is a network operation
        * And we cannot perform a network operation in main thread
        * so we need an AsyncTask
        * The constrains defined here are
        * Void -> We are not passing anything
        * Void -> Nothing at progress update as well
        * String -> After completion it should return a string and it will be the json string
        * */
        class GetJSON extends AsyncTask<Void, Void, String> {
            final ProgressDialog progressDialog = new ProgressDialog(Login.this, R.style.MyAlertDialogStyle);

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                try {
                    getServerMsg(s);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {


                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return "0";
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == 112) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                }
                //Toast.makeText(this, "IMEI No.: " + tm.getDeviceId(), Toast.LENGTH_LONG).show();
                //IMEI=tm.getDeviceId();
                phone.setText("" + tm.getLine1Number());
                Toast.makeText(this, "Thank You for granting permissions", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    public void switch_voice_assistance(View view) {
        if (state==0)
        {
            mp.release();
            db.execSQL("update va_state set state=1;");
            state=1;
            im_va.setImageResource(R.drawable.ic_record_voice_over_white_48dp);
            tv_va.setText("Tap to turn on Voice Assistance");
        }
        else
        {
            phone.requestFocus();
            mp=MediaPlayer.create(Login.this, R.raw.name_voice);
            mp.start();
            state=0;
            db.execSQL("update va_state set state=0;");
            im_va.setImageResource(R.drawable.ic_record_voice_over_cross_white_48dp);
            tv_va.setText("Tap to turn off Voice Assistance");
        }
    }
}