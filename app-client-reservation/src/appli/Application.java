package appli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import client.Client;
/**
 * 
 * @author hassa
 *
 */
class Application {
		private final static int PORT = 2500;
		private final static String HOST = "localhost"; 
	
	public static void main(String[] args) throws UnknownHostException, IOException {
	
		Client c1 = new Client(HOST,PORT);
	
	}

	
}