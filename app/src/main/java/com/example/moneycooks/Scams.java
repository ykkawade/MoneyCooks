package com.example.moneycooks;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.RawRes;
//import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Scams extends AppCompatActivity {

    TextView txtOutput; // Section introduction body
    TextView vishing, vishing_body; // Heading body for vishing section
    TextView smishing, smishing_body; // heading and body for smishing section


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scams);
        txtOutput = (TextView) findViewById(R.id.scam_intro);
        vishing = (TextView) findViewById(R.id.vishing);
        vishing_body = (TextView) findViewById(R.id.vishing_body);
        smishing = (TextView) findViewById(R.id.smishing);
        smishing_body = (TextView) findViewById(R.id.smishing_body);

        readFile(txtOutput, R.raw.scams);
        readFile(vishing_body, R.raw.vishing);
        readFile(smishing_body, R.raw.smishing);

    }
    public void readFile(TextView input, @RawRes int file) {
        int id = file;
        InputStream inputStream = this.getResources().openRawResource(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer stringBuffer = new StringBuffer();

        String strData = "";

        if (inputStream != null) {
            try {
                while ((strData = bufferedReader.readLine()) != null) {
                    stringBuffer.append(strData + "\n");
                }
                input.setText(stringBuffer);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


}
