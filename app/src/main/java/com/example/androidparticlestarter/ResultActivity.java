package com.example.androidparticlestarter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.ParticleEvent;
import io.particle.android.sdk.cloud.ParticleEventHandler;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;


public class ResultActivity extends AppCompatActivity {


    String score;
    String score2;

    String name;
    String name2;

    String particleId;
    String text;



    TextView t;
    TextView p1Score;
    TextView p2Score;

    private final String TAG="ALAY";

    // MARK: Particle Account Info
    private final String PARTICLE_USERNAME = "alaydesai094@gmail.com";
    private final String PARTICLE_PASSWORD = "Alaydesai009";

    // MARK: Particle device-specific info
    private final String DEVICE_ID = "2a002e001447363333343437";
    private final String DEVICE_ID2 = "310045001047363333343437";

    // MARK: Particle Publish / Subscribe variables
    private long subscriptionId;

    // MARK: Particle device
    private ParticleDevice mDevice;
    private ParticleDevice mDevice2;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // create the outlet for the label
        t = (TextView) findViewById(R.id.txt4);
        p1Score = (TextView) findViewById(R.id.player1show);
        p2Score = (TextView) findViewById(R.id.player2show);

        SharedPreferences prefs = getSharedPreferences("userName", MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        String restoredText2 = prefs.getString("name2", null);
        if (restoredText != null) {
            name = prefs.getString("name", "No name defined");

        }

        if (restoredText2 != null) {
            name2 = prefs.getString("name2", "No name defined");

        }

        t.setText(name +" VS "+ name2);



        // 1. Initialize your connection to the Particle API
        ParticleCloudSDK.init(this.getApplicationContext());

        // 2. Setup your device variable
        getDeviceFromCloud();

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


    public void checkButtonPressed(View view)
    {
        Log.d("ALAY", "Button pressed result;");

        // check if device is null
        // if null, then show error
        if (mDevice == null) {
            Log.d(TAG, "Cannot find device");
            return;
        }

        String lvlhard = "show";

        String commandToSend = lvlhard ;
        Log.d(TAG, "Command to send to particle: " + commandToSend);


        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {

                // 2. build a list and put the r,g,b into the list
                List<String> functionParameters = new ArrayList<String>();
                functionParameters.add(commandToSend);

                // 3. send the command to the particle
                try {
                    mDevice.callFunction("showInput", functionParameters);
                    mDevice2.callFunction("showInput", functionParameters);
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


        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {

            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {
                try {
                    subscriptionId = ParticleCloudSDK.getCloud().subscribeToAllEvents("score", new ParticleEventHandler() {

                        @Override
                        public void onEventError(Exception e) {
                            Log.d("exp", e.toString());
                        }

                        @Override
                        public void onEvent(String eventName, ParticleEvent particleEvent) {
                            text = particleEvent.dataPayload;
                            particleId = particleEvent.deviceId;
                            Log.d("p id", particleId);

                            if (particleId.equals("2a002e001447363333343437")) {
                                score = particleEvent.dataPayload;
                                Log.d("p id", particleId);
                                Log.d(TAG, "Player 1 : " + score);

                                p1Score.setText(score);


                            }

                            if (particleId.equals("310045001047363333343437")) {
                                score2 = particleEvent.dataPayload;
                                Log.d("p id", particleId);
                                Log.d(TAG, "Player 2 : " + score2);

                                p2Score.setText(score2);

                            }

                            if (score2 != null && score != null ) {
                                //gameStart = false;
                                Log.d(TAG, "Found null");

                            }


                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


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



    String result;
    public void resultButtonPressed(View view)
    {

        int player1 = Integer.parseInt(score);
        int player2 = Integer.parseInt(score2);


        Log.d(TAG,  "Score 1 : " + score + "Score 2 : " + score2);

        if (player1 > player2) {

            t.setText("Winner : " + name);

        }

        if (player2 > player1) {


            t.setText("Winner : " + name2);

        }


        if (player1 == player2) {

            result = "WIN";

            t.setText("Its a Draw");
        }




        Log.d("ALAY", "Button pressed result;");

        // check if device is null
        // if null, then show error
        if (mDevice == null) {
            Log.d(TAG, "Cannot find device");
            return;
        }


        result = "WIN";
        String commandToSend = result ;
        Log.d(TAG, "Command to send to particle: " + commandToSend);


        Async.executeAsync(ParticleCloudSDK.getCloud(), new Async.ApiWork<ParticleCloud, Object>() {
            @Override
            public Object callApi(@NonNull ParticleCloud particleCloud) throws ParticleCloudException, IOException {

                // 2. build a list and put the r,g,b into the list
                List<String> functionParameters = new ArrayList<String>();
                functionParameters.add(commandToSend);

                // 3. send the command to the particle
                try {
                    if (player1 > player2) {



                        mDevice.callFunction("result", functionParameters);
                    }


                    if (player2 > player1) {



                        mDevice2.callFunction("result", functionParameters);

                    }

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


    public void backButtonPressed(View view)
    {
        // creating a segue in Android
        Intent i = new Intent(this, NextActivity.class);
        startActivity(i);

    }






}

