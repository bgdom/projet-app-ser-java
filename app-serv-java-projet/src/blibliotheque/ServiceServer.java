package blibliotheque;

import java.net.Socket;

public interface ServiceServer extends Runnable{
	boolean consume(Data data);
	void remove(Socket s);
}
