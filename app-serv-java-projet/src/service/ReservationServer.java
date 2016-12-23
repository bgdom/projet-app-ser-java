package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import blibliotheque.Data;
import blibliotheque.InputWorker;
import blibliotheque.ServiceServer;

public class ReservationServer implements ServiceServer {
	private static final int PORT = 2500;
	private static final String IP = "127.0.0.1";
	private ServerSocket server;
	private InputWorker input;
	private HashMap<Socket, Socket> map;
	
	public ReservationServer(InputWorker in){
		try {
			server = new ServerSocket(PORT);
			input = in;
			map = new HashMap<Socket,Socket>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		if(server != null){// if there was no IOEception, we can start to work
			try {
				System.out.println("Le server est en marche");
				do{
					Socket s = server.accept();
					input.add(new Data(s,null, this));
					synchronized(map){
						map.put(s, s);
						System.out.println("Un nouveau client est accepté, il y en a "+ map.size());
					}
				}while(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean consume(Data data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void remove(Socket s){
		// TODO Auto-generated method stub
		synchronized(map){
			if(map.get(s) != null){
				map.remove(s);
			}
		}
		try {
			s.close();
			System.out.println("Un nouveau client est déconnecté, il y en reste "+ map.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void finalize(){
		if(server != null){
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			synchronized(map){
				Iterator<Entry<Socket, Socket>> it = map.entrySet().iterator();
				while(it.hasNext()){
					try {
						it.next().getValue().close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					it.remove();
				}
			}
			System.out.println("Le server s'arrête...");
		}
	}
}
