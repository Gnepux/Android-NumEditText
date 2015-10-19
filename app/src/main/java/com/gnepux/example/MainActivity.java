package com.gnepux.example;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gnepux.numedittext.NumEditText;


public class MainActivity extends ActionBarActivity implements NumEditText.NumChangedListener {

    private NumEditText net;

    private TextView numTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        net = (NumEditText) findViewById(R.id.net);
        numTextView = (TextView) findViewById(R.id.tv_num);
        net.setOnNumChangedListener(this);
    }

    @Override
    public void onAddClick() {
        numTextView.setText(net.getNumString());
    }

    @Override
    public void onMinusClick() {
        numTextView.setText(net.getNumString());
    }

    @Override
    public void onEditChange() {
        numTextView.setText(net.getNumString());
    }
}
