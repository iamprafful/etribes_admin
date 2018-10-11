package in.shadowsoft.adminEnd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Tribal_login extends AppCompatActivity {
    ImageView pass1,pass2,pass3,pass4,pass5,pass6,pass7,pass8,pass9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tribal_login);
        pass1=findViewById(R.id.imgPass1);
        pass2=findViewById(R.id.imgPass2);
        pass3=findViewById(R.id.imgPass3);
        pass4=findViewById(R.id.imgPass4);
        pass5=findViewById(R.id.imgPass5);
        pass6=findViewById(R.id.imgPass6);
        pass7=findViewById(R.id.imgPass7);
        pass8=findViewById(R.id.imgPass8);
        pass9=findViewById(R.id.imgPass9);
    }
    public void overlay1(View view) {
        pass1.setImageResource(R.drawable.fpass1);
    }public void overlay2(View view) {
        pass2.setImageResource(R.drawable.fpass2);
    }public void overlay3(View view) {
        pass3.setImageResource(R.drawable.fpass3);
    }public void overlay4(View view) {
        pass4.setImageResource(R.drawable.fpass4);
    }public void overlay5(View view) {
        pass5.setImageResource(R.drawable.fpass5);
    }public void overlay6(View view) {
        pass6.setImageResource(R.drawable.fpass6);
    }public void overlay7(View view) {
        pass7.setImageResource(R.drawable.fpass7);
    }public void overlay8(View view) {
        pass8.setImageResource(R.drawable.fpass8);
    }public void overlay9(View view) {
        pass9.setImageResource(R.drawable.fpass9);
    }




}
