package com.synergye.beacon;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;


class Enviarsocket implements Runnable {

    String mensagensto;

    Enviarsocket(String m) {
        mensagensto = m;
    }

    public void run() {

        Socket socket;
        PrintWriter output;
        BufferedReader input;
        String SERVER_IP = "gtwdev.synergye.com.br";
        int SERVER_PORT = 8002;

        try {

            socket = new Socket(SERVER_IP, SERVER_PORT);
            output = new PrintWriter(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message = "";

            if(mensagensto != null){

                message = mensagensto;

            }


            output.write(message);
            output.flush();

            char[] buffer = new char[200];
            int anzahlZeichen = input.read(buffer, 0, 200);
            System.out.println("RETORNO SERVIDOR PHBEACON :" + new String(buffer, 0, anzahlZeichen));

            //OU

            //System.out.println("DISPLAY MSG:" + input.readLine());

            sleep(2000);



            output.close();
            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}