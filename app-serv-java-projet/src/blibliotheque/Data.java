package blibliotheque;

import java.net.Socket;

public class Data {
	private Socket s;
	private String msg;
	private ServiceServer c;
	
	public Data(Socket s, String msg, ServiceServer c){
		this.c = c;
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
