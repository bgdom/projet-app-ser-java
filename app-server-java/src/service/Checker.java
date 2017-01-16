package service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import bibliotheque.Bibliotheque;
import bibliotheque.Client;
import bibliotheque.Document;
import bibliotheque.Service;

/**
 * Class to schedule checking state of documents
 * @author guydo
 *
 */
public class Checker implements Service{
	private Timer ti = new Timer(true);
	private HashMap<Client,Long> mapClient = new HashMap<Client,Long>();
	private HashMap<Document,Long> mapDoc = new HashMap<Document,Long>();
	private static final int DAY_IN_MILLIS = 1000*60*60*24;
	private static final int NB_DAYS_MAX_EMPRUNT = 30;
	private static final int NB_DAYS_PB = 30;
	private Bibliotheque bi;
	
	public Checker(Bibliotheque bi){
		this.bi = bi;
	}
	
	@Override
	public void lancer() {
		System.err.println("CHECKER : Marche");
		run();
	}

	/**
	 * tell the checker to take care of this document and detect after if there is
	 * a problem with the document
	 * @param d the document
	 */
	public void takeCare(Document d){
		synchronized(mapDoc){
			mapDoc.put(d, System.currentTimeMillis());
		}
	}
	
	/**
	 * detect if there is a problem with the document
	 * @param d the document
	 * @return true if everything is OK, false otherwise
	 */
	public boolean manageTime(Document d){
		Long l = null;
		synchronized(mapDoc){ // get the time associated withe the doc
			l = mapDoc.get(d); 
		}
		if(l == null) // if the doc is not mapped, so it was not borrowed
			return true;
		Calendar c1 = Calendar.getInstance(), c2 = Calendar.getInstance();
		c2.setTimeInMillis(l+DAY_IN_MILLIS*NB_DAYS_MAX_EMPRUNT);
		if(c1.compareTo(c2) < 0){ // if the doc is returned after the NB_DAYS_MAX_EMPRUNT
			addClient(bi.getEmprunteur(d));
			synchronized(mapDoc){ // remove the doc to the care mapping
				mapDoc.remove(d);
			}
			return false;
		}
		synchronized(mapDoc){ // remove the doc to the care mapping
			mapDoc.remove(d);
		}
		return true;
	}
	
	/**
	 * add a client to a mapping list of problematic client
	 * @param c the problematic client
	 */
	public void addClient(Client c){
		if(c == null)
			return;
		synchronized(mapClient){
			if(mapClient.containsKey(c)) // if the client is already registered
				mapClient.put(c, mapClient.get(c)+ NB_DAYS_PB*DAY_IN_MILLIS); // add a time again
			else{ // else 
				mapClient.put(c, new Long(NB_DAYS_PB*DAY_IN_MILLIS));
			}
		}
	}
	
	@Override
	public void stropper() {
		// TODO Auto-generated method stub
		ti.cancel();
		synchronized(mapDoc){
			mapDoc.clear();
		}
		synchronized(mapClient){
			mapClient.clear();
		}
		System.err.println("CHECKER : Arrêt");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, 1);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0); // represent the day after at 00h00
		ti.scheduleAtFixedRate(new TimerTask(){ 
			@Override
			public void run() {
				//System.err.println("Checker : run()");
				Set<Client> set = null;
				synchronized(mapClient){
					set = mapClient.keySet();
				}
				if(set != null){
					for(Client c : set){ // manage each key
						synchronized(mapClient){
							Long l = mapClient.get(c);
							if(l != null ){
								l = l - DAY_IN_MILLIS; // cut one day
								if(l > 0)
									mapClient.put(c, l); // put the new time
								else{ 
									mapClient.remove(c); //remove and set clean
								}
							 }
						}
					}
				}
			}
			
		},c.getTime(), DAY_IN_MILLIS);
	}

	public boolean isProblem(Client c) {
		// TODO Auto-generated method stub
		synchronized(mapClient){
			return mapClient.containsKey(c);
		}
	}
}