package service;

import java.util.ArrayList;
import java.util.List;

import bibliotheque.Bibliotheque;
import bibliotheque.Service;
import bibliotheque.ServiceLoader;

public class ServiceManager implements ServiceLoader {

	@Override
	public List<Service> load(Bibliotheque b) {
		// TODO Auto-generated method stub
		List<Service> list = new ArrayList<Service>();
		
		Checker ck = new Checker(b);
		list.add(ck);
		
		OutputWorker output = new OutputWorker(); // to write data over sockets
		list.add(output);
		
		DataConsumer dataConsumer = new DataConsumer(output); // to consume data that socket inputed
		list.add(dataConsumer);
		
		
		InputWorker input = new InputWorker(dataConsumer); // to read data over sockets
		list.add(input);
		
		list.add(new ReservationServer(input, output, b, ck)); // Reservation service 
		list.add(new RetourServer(input, output, b, ck)); // Retour service 
		list.add(new EmpruntServer(input, output,b, ck)); // Emprunt Service
		
		return list;
	}
}
