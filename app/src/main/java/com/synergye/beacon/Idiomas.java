package com.synergye.beacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Idiomas extends Activity {

    String FILENAME = "IDIOMA_APP";



    public static final String PREFS_NAME = "IDIOMA_APP";

    public String setIdioma(String idioma) {


        return "";
    }

    public String getIdioma() {



        String localidioma = "en";

        return localidioma;
    }



}