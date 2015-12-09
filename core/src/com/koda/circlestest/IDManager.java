package com.koda.circlestest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sean on 12/6/2015.
 */
public class IDManager {

    private static class ID {
        public int id;
        public boolean available;

        public ID(int id, boolean available) {
            this.id = id;
            this.available = available;
        }
    }

    private static HashMap<Integer, Integer> internedIDs = new HashMap<Integer, Integer>();
    private static ID[] ids = new ID[1000];
    private static int idsInUse = 0;

    static {
        for (int i = 0; i < ids.length; i++) {
            ids[i] = new ID(i, true);
        }
    }

    public static int requestID() {
        for (int i = 0; i < ids.length; i++) {
            ID v = ids[i];
            if (v.available) {
                v.available = false;
                idsInUse++;
                return v.id;
            }
        }
        return -1;
    }

    public static boolean releaseID(int id) {
        for (int i = 0; i < ids.length; i++) {
            ID v = ids[i];
            if (v.id == id) {
                v.available = true;
                idsInUse--;
                return true;
            }
        }
        return false;
    }

    public static boolean hasIDsRemaining() {
        return idsInUse < ids.length;
    }

    public static void internID(int connectionID, int id) {
        internedIDs.put(connectionID, id);
    }

    public static int retrieveInternedID(int connectionID) {
        if (internedIDs.containsKey(connectionID)) {
            return internedIDs.get(connectionID);
        }
        return -1;
    }
}
