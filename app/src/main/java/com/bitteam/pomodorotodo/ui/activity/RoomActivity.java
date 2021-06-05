package com.bitteam.pomodorotodo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bitteam.pomodorotodo.R;



public class RoomActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        initView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void initView() {
        Button createRoom = findViewById(R.id.create_room);
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent roomIntent = new Intent(RoomActivity.this, NewRoomActivity.class);
                startActivity(roomIntent);
            }
        });
        Button joinRoom = findViewById(R.id.join_room);
        joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent roomIntent = new Intent(RoomActivity.this, JoinRoomActivity.class);
                startActivity(roomIntent);
            }
        });
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

}