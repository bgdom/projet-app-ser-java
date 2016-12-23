package service;

import java.net.Socket;

import blibliotheque.Data;
import blibliotheque.ServiceServer;

public class EmpruntServer implements ServiceServer{
	private static final int PORT = 2600;
	private static final String IP = "127.0.0.1";
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean consume(Data data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void remove(Socket s) {
		// TODO Auto-generated method stub
		
	}

	
}
