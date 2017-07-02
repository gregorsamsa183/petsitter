package com.batech.app.petsitter.helper;

import java.net.InetAddress;

/**
 * Created by TR21718 on 12.06.2017.
 */

public class Helper {

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


}
