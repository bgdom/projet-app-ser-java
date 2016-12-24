package blibliotheque;

import java.net.Socket;

/**
 * 
 * @author guydo
 * represent a data that can be received and emit
 */
public class Data {
	private Socket s; // the socket to receive and emit data 
	private String msg; // the message receive and emit
	private ServiceServer c; // the service which manage this socket
	
	/**
	 * constructor
	 * @param s
	 * @param msg
	 * @param c
	 */
	public Data(Socket s, String msg, ServiceServer c){
		this.s = s;
		this.msg = msg;
		this.c = c;
	}
	
	
	public Socket getS() {
		return s;
	}
	
	public void setS(Socket s) {
		this.s = s;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public ServiceServer getC() {
		return c;
	}
	
	public void setC(ServiceServer c) {
		this.c = c;
	}
}
