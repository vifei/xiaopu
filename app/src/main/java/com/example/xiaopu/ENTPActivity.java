package com.example.xiaopu;

import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.os.Bundle;

public class ENTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entp);
        TextView text = findViewById(R.id.textView_entp);
        text.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
//class MainActivity : AppCompatActivity() {
//        override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val text = findViewById<TextView>(R.id.txt_scroll)
//        text.movementMethod = ScrollingMovementMethod.getInstance()
//        }
//        }
