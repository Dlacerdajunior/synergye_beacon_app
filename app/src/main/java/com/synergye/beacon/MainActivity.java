package com.synergye.beacon;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;


public class MainActivity extends Activity {

    PendingIntent pendingIntent;

    MainActivity activity;

    public TextView info, infoip, msg,textViewtitulo,nomeequipamentotexto;
    String tagstringretorno = "",tagstring = "",tagmodelo = "",tagid = "",tagmac = "",tagstatus = "",tagrssi = "",tagstatuscinta = "",tagstatusbateria = "",macaddreswlan0 = "";
    int tagrssiint;
    TextView tvMessages;

    int COUNT_CONECTION = 0;
    String BATTERY_LEVEL = "1";
    String BATTERY_CHARGING = "0";
    int SOCKET_PORTNUNBER = 9700;
    public static final String MY_PREFS_NAME = "MyPrefsFile";


    DateFormat dateFormat;
    Date date,daterelogio;

    /*TELAS*/
    RelativeLayout telarelogio;
    RelativeLayout telalog;
    RelativeLayout relativelayout_for_fragment;
    RelativeLayout iconetexto;

    TextView nomeequipamento;
    TextView textmenu1;
    TextView textmenu2;
    TextView textmenu3;
    TextView textmenu4;
    /*TELAS*/

    /*RELOGIO*/
    TextView datahora,topdiatexto,topmestexto;
    TextView datahora2,topdiatexto2,topmestexto2;
    String mesnome = "",diasemananome,horaminutonome,dianome;
    /*RELOGIO*/


    Thread Enviarsocket = null;
    Thread Verificarsocketserver = null;
    ServerSocket serverSocket;


    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            BATTERY_LEVEL = String.valueOf(level);

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);

            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
                    ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            // If battery is in charging states, then get the charging method
            if(isCharging){
                // Display the battery charging states
                BATTERY_CHARGING = "1";

            }else {
                // Display the battery charging state
                BATTERY_CHARGING = "0";
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*GET NIVEL BATERIA*/
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        /*GET NIVEL BATERIA*/

        /*IDIOMA*/
        String localidioam = "";

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("IDIOMA", null);
        if (restoredText != null) {
            localidioam = restoredText;
        }

        if(localidioam.equals("en")){
            setLocale("en");
        }else if(localidioam.equals("es")){
            setLocale("es");
        }else {
            setLocale("pt");
        }
        /*IDIOMA*/


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_mainphone);

        Utils.darkenStatusBar(this, R.color.topapp);


        /*MANTER TELA LIGADA*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /*MANTER TELA LIGADA*/


        /*GET THE DEVICE IP*/
        String ipdevice = Utils.getIPAddress(true);
        macaddreswlan0 = Utils.getMACAddress("wlan0");
        String macaddreseth0 = Utils.getMACAddress("eth0");
        /*GET THE DEVICE IP*/

        /*IDIOMA*/
        ImageView btbrasil = (ImageView) findViewById(R.id.btbrasil);
        btbrasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLocale("pt");
                nomeequipamento.setText(R.string.EQUIPAMENTO);
                textmenu1.setText(R.string.LIGAR_PARA_CENTRAL);
                textmenu2.setText(R.string.AGENDA);
                textmenu3.setText(R.string.RELOGIO);
                textmenu4.setText(R.string.ALERTAS);

            }
        });

        ImageView bt_america = (ImageView) findViewById(R.id.bt_america);
        bt_america.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setLocale("en");
                nomeequipamento.setText(R.string.EQUIPAMENTO);
                textmenu1.setText(R.string.LIGAR_PARA_CENTRAL);
                textmenu2.setText(R.string.AGENDA);
                textmenu3.setText(R.string.RELOGIO);
                textmenu4.setText(R.string.ALERTAS);

            }
        });

        ImageView bt_espanha = (ImageView) findViewById(R.id.bt_espanha);
        bt_espanha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setLocale("es");
                nomeequipamento.setText(R.string.EQUIPAMENTO);
                textmenu1.setText(R.string.LIGAR_PARA_CENTRAL);
                textmenu2.setText(R.string.AGENDA);
                textmenu3.setText(R.string.RELOGIO);
                textmenu4.setText(R.string.ALERTAS);

            }
        });
        /*IDIOMA*/

        textViewtitulo = (TextView) findViewById(R.id.textViewtitulo);
        info = (TextView) findViewById(R.id.info);
        infoip = (TextView) findViewById(R.id.infoip);
        msg = (TextView) findViewById(R.id.msg);
        tvMessages = (TextView) findViewById(R.id.tvMessages);
        iconetexto = (RelativeLayout) findViewById(R.id.iconetexto);


        nomeequipamento = (TextView) findViewById(R.id.nomeequipamento);
        textmenu1 = (TextView) findViewById(R.id.textmenu1);
        textmenu2 = (TextView) findViewById(R.id.textmenu2);
        textmenu3 = (TextView) findViewById(R.id.textmenu3);
        textmenu4 = (TextView) findViewById(R.id.textmenu4);


        /*DESCONECTADO*/
        iconetexto.setBackgroundResource(R.drawable.roundcornerdesconec);
        info.setText(R.string.STATUS_DESCONECTADO);
        info.setTextColor(Color.RED);
        /*DESCONECTADO*/

        infoip.setText("IP" + ipdevice);

        nomeequipamentotexto = (TextView) findViewById(R.id.nomeequipamentotexto);
        nomeequipamentotexto.setText(macaddreswlan0.replace(":",""));


        /*TELAS*/
        telarelogio = (RelativeLayout) findViewById(R.id.telarelogio);
        telalog = (RelativeLayout) findViewById(R.id.telalog);
        relativelayout_for_fragment = (RelativeLayout) findViewById(R.id.relativelayout_for_fragment);


        /*TELAS*/

        /*LINK MENU*/

        telarelogio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativelayout_for_fragment.setVisibility(View.VISIBLE);
                telalog.setVisibility(View.GONE);
                telarelogio.setVisibility(View.GONE);
            }
        });

        ImageView imagemenu1 = (ImageView) findViewById(R.id.imagemenu1);
        imagemenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //947411041
                    Utils.call(getBaseContext(),"947411041");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        ImageView imagemenu3 = (ImageView) findViewById(R.id.imagemenu3);
        imagemenu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relativelayout_for_fragment.setVisibility(View.GONE);
                telalog.setVisibility(View.GONE);
                telarelogio.setVisibility(View.VISIBLE);

            }
        });
        /*LINK MENU*/

        /*RELOGIO ELEMENTOS*/
        datahora = (TextView) findViewById(R.id.datahora);
        topdiatexto = (TextView) findViewById(R.id.topdiatexto);
        topmestexto = (TextView) findViewById(R.id.topmestexto);
        datahora2 = (TextView) findViewById(R.id.datahora2);
        topdiatexto2 = (TextView) findViewById(R.id.topdiatexto2);
        topmestexto2 = (TextView) findViewById(R.id.topmestexto2);
        /*RELOGIO ELEMENTOS*/

        /*INICIO SOCKET SERVER*/
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();


        Thread Verificarsocketserver = new Thread(new Verificarsocketserver());
        Verificarsocketserver.start();
        /*INICIO SOCKET SERVER*/

    }


    class SocketServerThread implements Runnable {

        //class SocketServerThread extends Thread {
        //forma que estava antes

        String  envioPHTAGServer,envioPHBeaconServer;
        @Override
        public void run() {
            try {


                String str;

                ServerSocket servSocket = new ServerSocket(SOCKET_PORTNUNBER);

                while (true) {


                    // Socket object
                    Socket fromClientSocket = servSocket.accept();
                    PrintWriter pw = new PrintWriter(fromClientSocket.getOutputStream(), true);

                    BufferedReader br =
                            new BufferedReader(
                                    new InputStreamReader(
                                            fromClientSocket.getInputStream()));

                    char[] buffer = new char[200];
                    int anzahlZeichen = br.read(buffer, 0, 200);
                    tagstringretorno = new String(buffer, 0, anzahlZeichen);

                    //ex retorno tag:*PHTAG,1234568,,942cb302a84a,1;0,-57,#

                    //VERICA SE A STRING DE RETORNO DA TAG
                    if(tagstringretorno != null){


                        tagstring = tagstringretorno.replace("*","");
                        tagstring = tagstring.replace("#","");

                        /*Separa a string por , para pegar os parametros*/

                        String tagstringsplit = tagstring;
                        String[] partstag = tagstringsplit.split(",");



                        tagmodelo = partstag[0];
                        tagid = partstag[1];
                        tagmac = partstag[3];
                        tagstatus = partstag[4];
                        tagrssi = partstag[5];



                        tagrssiint = Integer.parseInt(tagrssi);

                        if(tagrssiint <=1){
                            tagrssiint = tagrssiint * -1;
                        }


                        /*PEGAR INFO DE STATUS DA TAG tagstatuscinta 0 cinta Rompida 1 cinta ok*/
                        if(tagstatus != null){
                            String[] partstagstatus = tagstatus.split(";");
                                tagstatuscinta = partstagstatus[0];
                                tagstatusbateria = partstagstatus[1];
                            }

                        /*Gerar data atual para eviar para sevidor*/
                        dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        date = new Date();


                        String envioPHTAGStatus   =  ",1" // Número de Tags
                                +";"+tagstatuscinta //Dispositivo com Case Violado , 0 para Case íntegro,1 par
                                +";0" // Dispositivo em Movimento, 0 para dispositivo em repouso, 1 para
                                +";"+BATTERY_CHARGING //Dispositivo conectado à Rede Elétrica
                                +";"+BATTERY_LEVEL; //Nível de carga da bateria

                        envioPHTAGServer   =  "*PHBEACON" //MODELO
                                +","+macaddreswlan0.replace(":","") //MAC do beacon
                                +","+dateFormat.format(date).toString() //TIMESTAMP
                                +","+Build.VERSION.RELEASE //VERSAO_FIRMWARE​
                                +","+Build.HARDWARE //VERSAO_HARDWARE​
                                +""+envioPHTAGStatus //STATUS
                                +",A;10;S21.74813;W48.17971;0;54;672;0;0" //GPS
                                +",724;10;045C;36A8;-85;724;10;045C;36A7;-99;724;10;045C;1F08;-103" //GSM
                                +","; //CHECKSUM


                        //Adler32
                        Checksums checksum = new Checksums();
                        String checksumstring = checksum.chkSumAdler32(envioPHTAGServer.getBytes(), envioPHTAGServer.length(), 8);
                        String envioPHTAGServer2   =  ""+envioPHTAGServer+""+checksumstring+"#";
                        //Adler32


                        //Log.d("EVIAR SERVER PHBEACON ",""+ envioPHTAGServer);

                        Thread Enviarsocket = new Thread(new Enviarsocket(envioPHTAGServer2));
                        Enviarsocket.start();


                        envioPHBeaconServer   =  "*PHTAG" //MODELO
                                +","+tagid //ID
                                +"," //NULL
                                +","+tagmac //MAC
                                +","+tagstatuscinta+";"+tagstatusbateria //STATUS
                                +",-"+tagrssiint //RSSI
                                +",#"; //

                        Log.d("EVIAR SERVER PHTAG",""+ envioPHBeaconServer);

                        Thread Enviarsocketphtag = new Thread(new Enviarsocketphtag(envioPHBeaconServer));
                        Enviarsocketphtag.start();


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                textmenu4.setText(R.string.ALERTAS);
                                iconetexto.setBackgroundResource(R.drawable.roundcornerconectado);

                                info.setText(R.string.STATUS_CONECTADO);
                                info.setTextColor(Color.rgb(0,100,0));
                                //ZERAR COUNT DE CHECAR A TAG, 0 = TAG OK , 10=TAG NAO ENCONTRADA
                                COUNT_CONECTION = 0;

                                //Log.d("RETORNO SERVER ",""+ tagstringretorno);

                                DateFormat dateFormat2 = new SimpleDateFormat("dd/MM dd:HH:mm-ss");
                                Date date2 = new Date();
                                tvMessages.setText(tvMessages.getText()+"\n\n D:" + dateFormat2.format(date2).toString()+"\n"+envioPHTAGServer);

                                msg.setText("TAG:" +tagmodelo+ "\n"+"MAC:" +tagmac+ "\n"+"s:" +tagstatus+ "\n"+"RSSI:" +tagrssi);

                                /* NOTICICACAO ALERT SONG*/
                                if(tagrssiint >=70){

                                    Utils utilnotificationtag = new Utils(getBaseContext());
                                    utilnotificationtag.createNotification(getString(R.string.NOTIFICACOES),getString(R.string.TAG_LONGE),2);
                                    textmenu4.setText(R.string.TAG_LONGE);
                                }
                                /* NOTICICACAO ALERT SONG*/
                            }
                        });




                    }

                    //Thread.sleep(1500);
                    Thread.sleep(8000);

                    pw.close();
                    br.close();

                    fromClientSocket.close();


                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    class Verificarsocketserver implements Runnable {

        public void run() {

            while (true) {

                try {

                    /*RELOGIO*/
                    daterelogio = new Date();
                    DateFormat mesemnumero = new SimpleDateFormat("MM");
                    mesnome = mesemnumero.format(daterelogio);
                    if(mesnome.equals("01")){ mesnome = getString(R.string.Janeiro); }
                    if(mesnome.equals("02")){ mesnome = getString(R.string.Fevereiro); }
                    if(mesnome.equals("03")){ mesnome = getString(R.string.Marco); }
                    if(mesnome.equals("04")){ mesnome = getString(R.string.Abril); }
                    if(mesnome.equals("05")){ mesnome = getString(R.string.Maio); }
                    if(mesnome.equals("06")){ mesnome = getString(R.string.Junho); }
                    if(mesnome.equals("07")){ mesnome = getString(R.string.Julho); }
                    if(mesnome.equals("08")){ mesnome = getString(R.string.Agosto); }
                    if(mesnome.equals("09")){ mesnome = getString(R.string.Setembro); }
                    if(mesnome.equals("10")){ mesnome = getString(R.string.Outubro); }
                    if(mesnome.equals("11")){ mesnome = getString(R.string.Novembro); }
                    if(mesnome.equals("12")){ mesnome = getString(R.string.Dezembro); }

                    DateFormat diadasemnumero = new SimpleDateFormat("u");
                    diasemananome = diadasemnumero.format(daterelogio);
                    if(diasemananome.equals("1")){ diasemananome = getString(R.string.segunda); }
                    if(diasemananome.equals("2")){ diasemananome = getString(R.string.terca); }
                    if(diasemananome.equals("3")){ diasemananome = getString(R.string.quarta); }
                    if(diasemananome.equals("4")){ diasemananome = getString(R.string.quinta); }
                    if(diasemananome.equals("5")){ diasemananome = getString(R.string.sexta); }
                    if(diasemananome.equals("6")){ diasemananome = getString(R.string.sabado); }
                    if(diasemananome.equals("7")){ diasemananome = getString(R.string.domingo); }

                    DateFormat horaminuto = new SimpleDateFormat("HH:mm");
                    horaminutonome = horaminuto.format(daterelogio);

                    DateFormat diahoje = new SimpleDateFormat("dd");
                    dianome = diahoje.format(daterelogio);
/*
                    Log.d("mes numero ",""+mesnome);
                    Log.d("dia da semana",""+ diasemananome);
                    Log.d("horaminutonome",""+ horaminutonome);
                    Log.d("dianome",""+ dianome);
*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            datahora.setText(horaminutonome+" |");
                            topdiatexto.setText(dianome);
                            topmestexto.setText(mesnome.toUpperCase() + " - " +diasemananome.toUpperCase());
                            datahora2.setText(horaminutonome+"");
                            topdiatexto2.setText(dianome);
                            topmestexto2.setText(mesnome.toUpperCase() + " - " +diasemananome.toUpperCase());
                        }
                    });
                    /*RELOGIO*/

                    Thread.sleep(10000);

                    COUNT_CONECTION = COUNT_CONECTION + 1;


                    if(COUNT_CONECTION >= 12){

                        Log.d("ZERAR COUNT ","0");

                        COUNT_CONECTION = 0;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                info.setText(R.string.STATUS_DESCONECTADO);
                                info.setTextColor(Color.RED);


                                iconetexto.setBackgroundResource(R.drawable.roundcornerdesconec);
                                textmenu4.setText(R.string.TAG_DESCONECTADA);

                                /* NOTICICACAO ALERT SONG*/
                                Utils utilnotification = new Utils(getBaseContext());
                                utilnotification.createNotification(getString(R.string.NOTIFICACOES),getString(R.string.TAG_DESCONECTADA),1);
                                /* NOTICICACAO ALERT SONG*/
                            }
                        });
                    }

                    Log.d("COUNT_CONECTION ","" + COUNT_CONECTION);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }


    public void setLocale(String lang) {

        String idiomasabe = "pt";
        if(lang != null){
            idiomasabe = lang;
        }

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("IDIOMA", idiomasabe);
        editor.apply();

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

    }
}