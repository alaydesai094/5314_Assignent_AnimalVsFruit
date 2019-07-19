package com.example.androidparticlestarter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.particle.android.sdk.cloud.ParticleCloud;
import io.particle.android.sdk.cloud.ParticleCloudSDK;
import io.particle.android.sdk.cloud.ParticleDevice;
import io.particle.android.sdk.cloud.exceptions.ParticleCloudException;
import io.particle.android.sdk.utils.Async;




import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // MARK: Debug info
    private final String TAG="ALAY";

    // Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();



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
        setContentView(R.layout.activity_main);

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


    public void saveButtonPressed(View view)
    {
        Log.d("ALAY", "Button pressed;");


        // 1. get the NAME value from the UI
        EditText name = (EditText) findViewById(R.id.name);
        EditText name2 = (EditText) findViewById(R.id.name2);

        int score = 0;

        // Convert these to strings
        String uname = name.getText().toString();
        String uname2 = name2.getText().toString();

        Log.d(TAG, "User name: " + uname);

        // storing the name into a shared pref
        SharedPreferences.Editor editor = getSharedPreferences("userName", MODE_PRIVATE).edit();
        editor.putString("name", uname);
        editor.putString("name2", uname2);
        editor.apply();


        //-----------------------
        //Firebase Stuff
        //------------------------

        // 4. create a dictionary to store your data
        // - We will be sending this dictionary to Firebase
        Map<String, Object> player = new HashMap<>();
        player.put("name", uname);
        player.put("score", score);


//        //5. connect to firebase
//        // Add a new document with a ID = gameID
//
//        final DocumentReference ref = db.collection("games").document();
//        ref.set(player)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "Added player choice to game = " + ref.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "Error adding document", e);
//                    }
//                });


        // creating a segue in Android
        Intent i = new Intent(this, NextActivity.class);
        startActivity(i);

    }

}
