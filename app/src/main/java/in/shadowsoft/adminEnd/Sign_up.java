package in.shadowsoft.adminEnd;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class Sign_up extends AppCompatActivity {
    Button register;
    EditText Name,phone,pwd;
    String Name_text,phone_text,pwd_text,final_url,IMEI;
    String[] Success = new String[1];
    String[] Msg=new String[1];
    String[] id=new String[1];
    private final int REQ_CODE_SPEECH_INPUT = 100;
    TelephonyManager tm;
    SQLiteDatabase db;
    MediaPlayer mp=new MediaPlayer();
    int state=0;
    ImageView im_va;
    TextView tv_va;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
         Name=findViewById(R.id.Name);
         im_va=findViewById(R.id.img_va);
         tv_va=findViewById(R.id.tv_va);
        phone=findViewById(R.id.phone);
        pwd=findViewById(R.id.pwd);
        Name_text=Name.getText().toString();
        phone_text=phone.getText().toString();
        pwd_text=pwd.getText().toString();
        db = openOrCreateDatabase("ciphers",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tribal_user(id VARCHAR,full_name VARCHAR, phone VARCHAR, password VARCHAR, status VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS va_state(state integer);");
        Cursor resultSet = db.rawQuery("Select id from tribal_user where status='1'",null);
        try {
            resultSet.moveToFirst();
            String id = resultSet.getString(0);
            Intent already_login = new Intent(this, MainActivity.class);
            already_login.putExtra("id", id);
            startActivity(already_login);
            finish();
        }
        catch (Exception e)
        {
            //no result
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
        Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {

                    if (state==0){
                        mp.release();
                        mp=MediaPlayer.create(Sign_up.this, R.raw.contact_name_voice);
                        mp.start();
                    }
                }
            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    if (state==0){
                        mp.release();
                        mp=MediaPlayer.create(Sign_up.this, R.raw.mobile_number_voice);
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
                        mp=MediaPlayer.create(Sign_up.this, R.raw.pin_voice);
                        mp.start();
                    }
                }
            }
        });


        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this,"IMEI No.: "+tm.getDeviceId(),Toast.LENGTH_LONG).show();
            IMEI=tm.getDeviceId();
            phone.setText(""+tm.getLine1Number());
        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 112);
        }

    }

    public void register(View view) {
        Name_text=Name.getText().toString();
//        email_text=email.getText().toString();
        phone_text=phone.getText().toString();
        pwd_text=pwd.getText().toString();
//        cpwd_text=cpwd.getText().toString();


        if (Name_text.isEmpty()||phone_text.isEmpty()||pwd_text.isEmpty()){
            Toast validate_error=Toast.makeText(this,"Please fill all fields",Toast.LENGTH_LONG);
            validate_error.show();
            mp.release();
            mp=MediaPlayer.create(Sign_up.this, R.raw.fill_all_entries);
            mp.start();
        }
        else{
//            if (!cpwd_text.equals(pwd_text))
//            {
//                Toast password_error=Toast.makeText(this,"Password not matched",Toast.LENGTH_LONG);
//                password_error.show();
//            }
//            else {


                    final_url="http://ciphers.shadowsoft.in/tribal/register_tribal.php?name="+Name_text.replace(" ","%20")+"&IMEI="+IMEI+"&phone="+phone_text+"&pwd="+pwd_text;
                    try {
                        Log.e("sql",final_url);
                        getJSON(final_url);
                    }
                    catch (Exception ex)
                    {
                        Toast e=Toast.makeText(this,ex.getMessage(),Toast.LENGTH_LONG);
                        e.show();
                    }


//            }
        }
    }

    public void launch_login(View view) {
        mp.release();
        Intent launch_login=new Intent(this,Login.class);
        this.startActivity(launch_login);
        this.overridePendingTransition(0,0);
    }

    public void getServerMsg(String json) throws JSONException {
        if (json.equals("0"))
        {
            Toast.makeText(Sign_up.this,"No Internet Connection",Toast.LENGTH_LONG).show();
            if (state==0){
                mp.release();
                mp=MediaPlayer.create(Sign_up.this, R.raw.no_internet_voice);
                mp.start();
            }
        }
        else
        {
            Name_text=Name.getText().toString();
//        email_text=email.getText().toString();
            phone_text=phone.getText().toString();
            pwd_text=pwd.getText().toString();

            //creating a json array from the json string
            JSONArray jsonArray = new JSONArray(json);

            //looping through all the elements in json array
            for (int i = 0; i < 1; i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                id[i]=obj.getString("id");
                Success[i] = obj.getString("Success");
                Msg[i]=obj.getString("Comment");
                Log.e("serverMsg",Msg[i]);
            }
            if(Success[0].equals("1"))
            {
                db.execSQL("INSERT INTO tribal_user VALUES('"+id[0]+"','"+Name_text+"','"+phone_text+"','"+pwd_text+"','1');");
                Intent successful_login=new Intent(this,MainActivity.class);
                successful_login.putExtra("id",id[0]);
                this.startActivity(successful_login);
            }
            else {
                Toast err=Toast.makeText(this,Msg[0],Toast.LENGTH_LONG);
                err.show();
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
            final ProgressDialog progressDialog = new ProgressDialog(Sign_up.this, R.style.MyAlertDialogStyle);

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Creating your Account");
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
                    String error="0";
                    return error;
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Name.setText(result.get(0));
                }
                break;
            }

        }
    }

    public void speak_name(View view) {
        mp.release();
        promptSpeechInput();
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == 112) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                }
               // Toast.makeText(this,"IMEI No.: "+tm.getDeviceId(),Toast.LENGTH_LONG).show();
                IMEI=tm.getDeviceId();
                phone.setText(""+tm.getLine1Number());
                Toast.makeText(this, "Thank You for granting permissions", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void play_name_instructions(View view) throws IOException {
    }

    public void play_mobile_instruction(View view) {
    }

    public void play_pin_instruction(View view) {
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
            Name.requestFocus();
            mp=MediaPlayer.create(Sign_up.this, R.raw.name_voice);
            mp.start();
            state=0;
            db.execSQL("update va_state set state=0;");
            im_va.setImageResource(R.drawable.ic_record_voice_over_cross_white_48dp);
            tv_va.setText("Tap to turn off Voice Assistance");
        }
    }
}
