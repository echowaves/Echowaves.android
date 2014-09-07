package com.echowaves.android;

import java.text.SimpleDateFormat;

/**
 * copyright echowaves
 * Created by dmitry
 * on 6/25/14.
 */

public interface EWConstants {
    public final static String FlurryKey = "V4V86GZNZGPXMHR9FM6Q";

    public static final String BASE_URL = "http://echowaves.com";
    public static final String EWAWSBucket = "http://images.echowaves.com";
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
    public static SimpleDateFormat naturalDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

//    public static final String BASE_URL = "http://192.168.1.103:3000";
//    public static final String BASE_URL = "http://10.0.20.72:3000";
//    public static final String EWAWSBucket = "http://imagesdev.echowaves.com";

}
