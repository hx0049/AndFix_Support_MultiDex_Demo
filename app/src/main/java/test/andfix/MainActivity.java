package test.andfix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //old apk
        Toast.makeText(this, "This is a bug", Toast.LENGTH_SHORT).show();
        //new apk
        //Toast.makeText(this, "This is a fixed bug", Toast.LENGTH_SHORT).show();
    }
}
