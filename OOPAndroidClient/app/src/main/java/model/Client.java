package model;

import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import common.Account;

public class Client {
    public static Account friendAccount;
    public static String chatwith;
    public static String username;
    public static String password;
    private static Socket socket;
    public static ObjectInputStream inputStream;
    public static ObjectOutputStream outputStream;
    public static ArrayList<Account> friends;
    public static void login(String username, String password) throws Exception {
        Client.username = username;
        Client.password = password;
        /* Connect */
        Account account = new Account(username, password);
        socket = new Socket(Properties.getHost(), Properties.getPort());
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        /* Login */
        outputStream.writeObject(account);
        Object friendList = inputStream.readObject();
        if (!(friendList instanceof ArrayList))
            throw new Exception("Login error");
        friends = (ArrayList<Account>) friendList;
    }
}
