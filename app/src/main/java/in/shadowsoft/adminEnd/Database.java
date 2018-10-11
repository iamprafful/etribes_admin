package in.shadowsoft.adminEnd;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Database extends AppCompatActivity {
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        db = openOrCreateDatabase("ciphers",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS shadowsoft(Username VARCHAR,Password VARCHAR);");
        db.execSQL("INSERT INTO shadowsoft VALUES('admin','admin');");
        Cursor resultSet = db.rawQuery("Select * from shadowsoft",null);
        resultSet.moveToFirst();
        String username = resultSet.getString(0);
        String password = resultSet.getString(1);
        Toast.makeText(this,username+" "+password,Toast.LENGTH_LONG).show();
    }

}
