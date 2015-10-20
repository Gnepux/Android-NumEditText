package com.gnepux.example;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gnepux.numedittext.NumEditText;


public class MainActivity extends ActionBarActivity implements NumEditText.OnNumChangedListener {

    private NumEditText net;

    private TextView numTextView;

    private EditText setNumEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        net = (NumEditText) findViewById(R.id.net);
        numTextView = (TextView) findViewById(R.id.tv_num);
        setNumEditText = (EditText) findViewById(R.id.et_set_num);

        numTextView.setText(getString(R.string.current_value, net.getNumString()));
        net.setOnNumChangedListener(this);
    }

    @Override
    public void onAddClick() {
        numTextView.setText(getString(R.string.current_value, net.getNumString()));
    }

    @Override
    public void onMinusClick() {
        numTextView.setText(getString(R.string.current_value, net.getNumString()));
    }

    @Override
    public void onEditChange() {
        numTextView.setText(getString(R.string.current_value, net.getNumString()));
    }

    public void goSetNum(View v) {
        if (net != null) {
            net.setNum(setNumEditText.getText().toString());
        }
    }

    public void goShake(View v) {
        if (net != null) {
            net.shake();
        }
    }

    public void goAdd(View v) {
        if (net != null) {
            net.add(1, true);
        }
    }

    public void goMinus(View v) {
        if (net != null) {
            net.minus(1, true);
        }
    }
}
