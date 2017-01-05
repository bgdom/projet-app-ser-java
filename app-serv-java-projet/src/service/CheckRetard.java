package service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bibliotheque.Client;
import bibliotheque.Document;

public class CheckRetard{
	private List<Document> documents;
	private Timer ti = new Timer();
	
	ti.schedule(new TimerTask(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("bg");
		}
		
	},5000,1000*24);


		
}
