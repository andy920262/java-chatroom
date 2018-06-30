package com.example.jerry.oopandroidclient;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import model.Client;
import model.MessageListAdapter;
import model.UserMessage;

public class RoomActivity extends AppCompatActivity {
    public static final int MESSAGE_SENT = 1;
    public static final int MESSAGE_RECEIVED = 2;
    public static final int MESSAGE_SYSTEM = 3;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private ArrayList<UserMessage> messageList = new ArrayList<UserMessage>();
    private EditText mNewMessage;
    private Thread received_thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        setTitle("Room: " + Client.chatwith);

        Log.d("Room", "Entered room");
        mMessageAdapter = new MessageListAdapter(messageList);
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(layoutManager);
        mMessageRecycler.setAdapter(mMessageAdapter);

        mNewMessage =  (EditText) findViewById(R.id.edittext_chatbox);
        // Enter
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("Room", "Logging in");
                    Client.login(Client.username, Client.password);
                    Log.d("Room", "Connecting friend");
                    Client.outputStream.writeObject(Client.friendAccount);
                    Log.d("Room", "Done");
                    received_thread.start();
                } catch (Exception e) {
                    Log.e("Room-Friend", "cannot enter room!");
                    e.printStackTrace();
                }
            }
        }).start();

        // Receive messages
        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String message = (String) msg.obj;
                Log.d("[Handler]", message);
                switch (msg.what) {
                    case MESSAGE_SENT:
                        messageList.add(new UserMessage(Client.username, message));
                        break;
                    case MESSAGE_RECEIVED:
                        messageList.add(new UserMessage(Client.chatwith, message));
                        break;
                    case MESSAGE_SYSTEM:
                        messageList.add(new UserMessage("System", message));
                        break;
                }
                mMessageRecycler.setAdapter(mMessageAdapter);
            }
        };

        received_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Object packet = Client.inputStream.readObject();
                        Log.d("[Received]", "obj");
                        int type = 0;
                        String message = "";
                        if (packet instanceof common.Message) {
                            common.Message msg = (common.Message) packet;
                            if (msg.getAccount().equals(Client.username)) {
                                type = MESSAGE_SENT;
                                message = msg.getMsg();
                            } else {
                                Client.chatwith = msg.getName();
                                type = MESSAGE_RECEIVED;
                                message = msg.getMsg();
                            }
                        } else if (packet instanceof String) {
                            type = MESSAGE_SYSTEM;
                            message = (String) packet;
                        }
                        handler.sendMessage(handler.obtainMessage(type, message));
                    } catch (Exception e) {
                        if (e.getMessage().equals("Socket closed"))
                            break;
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("ROOM", "Exited");
        received_thread.interrupt();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Client.inputStream.close();
                    Client.outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessage(View button) {
        final Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mNewMessage.setText("");
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = mNewMessage.getText().toString();
                    Client.outputStream.writeObject(msg);
                    handler.sendMessage(handler.obtainMessage(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void addFriend(View button) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Client.outputStream.writeObject("@ADDFRIEND");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
