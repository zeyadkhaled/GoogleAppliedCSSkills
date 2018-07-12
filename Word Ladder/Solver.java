package com.google.engedu.wordladder;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.engedu.worldladder.R;
import java.util.ArrayList;

public class Solver extends AppCompatActivity {

    ArrayList<EditText> views;
    String[] words;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver);
        words = getIntent().getStringArrayExtra("words");
        bt = (Button) findViewById(R.id.solveButton);

        TextView startWordView = (TextView) findViewById(R.id.startTextView);
        startWordView.setText(words[0]);


        LinearLayout tvs = (LinearLayout) findViewById(R.id.textViews);
        views = new ArrayList<>();
        for ( int i = 0; i < words.length - 2; i++) {
           EditText txt = new EditText(this);
           views.add(txt);
           tvs.addView(txt);
        }


        TextView endWordView = (TextView) findViewById(R.id.endTextView);
        endWordView.setText(words[words.length - 1]);
    }

    public void solveAll(View view) {
        for ( int i = 1; i < words.length - 1; i++) {
            EditText txt = views.get(i - 1);
            txt.setBackgroundColor(Color.GRAY);
            txt.setText(words[i]);
        }
        bt.setEnabled(false);
    }

    public void checkSolved(View view) {
        for ( int i = 1; i < words.length - 1; i++) {
            EditText txt = views.get(i - 1);
            if ( txt.getText().length() > 0) {
                if ( txt.getText().toString().equals(words[i]))
                    txt.setBackgroundColor(Color.GREEN);
                else {
                    txt.setBackgroundColor(Color.RED);
                    txt.setText(words[i] + " - wrong");
                }
            }
        }
    }
}
