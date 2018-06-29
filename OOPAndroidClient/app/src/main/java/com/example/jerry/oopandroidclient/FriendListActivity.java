package com.example.jerry.oopandroidclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import common.Account;
import model.Client;

public class FriendListActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        setTitle("Welcome " + Client.username + " !");

        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<Account> listAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_list_item_1) ;
        listAdapter.add(new Account(null, null, "[Random Chat]"));
        listAdapter.addAll(Client.friends);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                ListView listView = (ListView) adapter;
                Account friend = (Account) listView.getItemAtPosition(position);
                Client.friendAccount = friend;
                String nickname = friend.getName();
                MyDialogFragment dialog = new MyDialogFragment();
                dialog.setUsername(nickname);
                dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }
        });
    }

    public void startChat() {
        Intent room = new Intent(FriendListActivity.this, RoomActivity.class);
        startActivity(room);
    }

    public static class MyDialogFragment extends DialogFragment {
        private String nickname = null;

        public void setUsername(String username) {
            this.nickname = username;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Confirm");
            builder.setMessage("Chat with " + nickname);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Client.chatwith = nickname;
                    ((FriendListActivity)getActivity()).startChat();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // You don't have to do anything here if you just
                    // want it dismissed when clicked
                }
            });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
