package service;

import bibliotheque.Service;

public abstract class AbstractService implements Service {
	protected Thread th;
	
	@Override
	public void lancer() {
		// TODO Auto-generated method stub
		th = new Thread(this);
		th.start();
	}

	@Override
	public void stropper() {
		// TODO Auto-generated method stub
		th.interrupt();
	}

}
