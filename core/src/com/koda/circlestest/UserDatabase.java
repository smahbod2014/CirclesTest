package com.koda.circlestest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;

import java.io.*;
import java.util.HashMap;

/**
 * Created by Sean on 12/9/2015.
 */
public class UserDatabase {
    private static HashMap<String, String> users = new HashMap<>();

    public static void addUser(String username, String password) {
        users.put(username, password);
    }

    public static boolean userExists(String username) {
        return users.containsKey(username);
    }

    public static int authenticate(String username, String password) {
        if (!userExists(username))
            return Messages.LoginResponse.RESULT_BAD_USERNAME;
        if (!users.get(username).equals(password))
            return Messages.LoginResponse.RESULT_BAD_PASSWORD;
        return Messages.LoginResponse.RESULT_ACCEPTED;
    }

    public static void writeToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("userdatabase.txt"));
            for (String username : users.keySet()) {
                bw.write(username);
                bw.newLine();
                bw.write(users.get(username));
                bw.newLine();
            }
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFromFile() {
        File file = new File("userdatabase.txt");
        if (!file.exists())
            return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                String username = line;
                String password = br.readLine();
                users.put(username, password);
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
