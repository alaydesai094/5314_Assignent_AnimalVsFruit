package com.example.androidparticlestarter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;

public class LastActivity extends AppCompatActivity {


    private final String TAG="ALAY";

    String[] words = {"Chudap","Chodu","Vagri", "Gelfaido","Chamchi","limbu", "lasan"};
    String[] goodwords = {"Limbu","Lasan","Chamchi"};

    TextView t;
    String answer;

    String device1data;
    String device2data;

    String particleId;
    String text;



    // MARK: Particle Account Info
    private final String PARTICLE_USERNAME = "alaydesai094@gmail.com";
    private final String PARTICLE_PASSWORD = "Alaydesai009";

    // MARK: Particle device-specific info
    private final String DEVICE_ID = "2a002e001447363333343437";
    private final String DEVICE_ID2 = "310045001047363333343437";

    // MARK: Particle Publish / Subscribe variables
    private long subscriptionId;
    private long subscriptionId2;

    // MARK: Particle device
    private ParticleDevice mDevice;
    private ParticleDevice mDevice2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);

       t = (TextView) findViewById(R.id.txt2);

      // setQuestion();

        // 1. Initialize your connection to the Particle API
        ParticleCloudSDK.init(this.getApplicationContext());

        // 2. Setup your device variable
        getDeviceFromCloud();


//        SharedPreferences prefs = getSharedPreferences("userName", MODE_PRIVATE);
//        String restoredText = prefs.getString("name", null);
//        if (restoredText != null) {
//            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
//            t.setText("Hello " + name + "!!");
//        }


    }


    /**
     * Custom function to connect to the Particle Cloud and get the device
     */
    public void getDeviceFromCloud() {
        // This function runs in the background
        // It tries to connect to the Particle Cloud and get your device
        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                particleCloud.logIn(PARTICLE_USERNAME, PARTICLE_PASSWORD);
                mDevice = particleCloud.getDevice(DEVICE_ID);
                mDevice2 = particleCloud.getDevice(DEVICE_ID2);
                return -1;
            }

            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "Successfully got device from Cloud");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });
    }


    public void setQuestion() {

        Random r = new Random();
        int n = r.nextInt(7);
        t.setText(words[n]);


    }


    public void easyButtonPressed(View view)
    {

        setQuestion();

    }


    public void checkButtonPressed(View view) {


        if(Arrays.asList(goodwords).contains(t.getText())){

            answer = "NO";

        }
        else {
            answer = "YES";
        }

        String ToSend = answer ;
        Log.d(TAG, "Command to send to particle: " + ToSend);


        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {

                // 2. build a list and put the r,g,b into the list
                List<String> functionParameters = new ArrayList<String>();
                functionParameters.add(ToSend);

                // 3. send the command to the particle
                try {
                    mDevice.callFunction("answer", functionParameters);
                    mDevice2.callFunction("answer", functionParameters);
                } catch (ParticleDevice.FunctionDoesNotExistException e) {
                    e.printStackTrace();
                }

                return -1;
            }


            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "Sent command to device.");
            }

            @Override
            public void onFailure(ParticleCloudException exception) {
                Log.d(TAG, exception.getBestMessage());
            }
        });


    }


    public void hardButtonPressed(View view)
    {


        // creating a segue in Android
        Intent i = new Intent(this, ResultActivity.class);
        startActivity(i);

    }



}
