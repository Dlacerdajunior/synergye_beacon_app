package com.synergye.beacon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;


class Enviarsocketphtag implements Runnable {

    String mensagensto;

    Enviarsocketphtag(String m) {
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

            //String message = "*PHBEACON,942cb302a84a,20190606120716,0.1.1,1.0.0,1;0;0;1;100,A;10;S21.74813;W48. 17971;0;54;672;0;0,724;10;045C;36A8;-85;724;10;045C;36A7;-99;724;10;045C;1F08;-103,B99F1E52#";

            String message = "";

            if(mensagensto != null){

                message = mensagensto;

            }


            output.write(message);
            output.flush();

            char[] buffer = new char[200];
            int anzahlZeichen = input.read(buffer, 0, 200);
            System.out.println("RETORNO SERVIDOR PHTAG:" + new String(buffer, 0, anzahlZeichen));

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