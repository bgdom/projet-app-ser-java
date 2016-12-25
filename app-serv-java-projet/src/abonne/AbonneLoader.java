package abonne;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import blibliotheque.AbonneFactory;
import blibliotheque.Client;

/**
 * 
 * @author guydo
 * represent a class to load different type of Users
 */
public class AbonneLoader implements AbonneFactory{

	@Override
	public List<Client> getAbonneFromFile(String fileName) {
		ArrayList<Client> liste = new ArrayList<Client>();
		try {
			BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			String line;
			while((line = buff.readLine()) != null){ // read an entry
				String tab[] = line.split(";");
				if(tab.length == 3)
					liste.add(new Abonne(tab[0], tab[1],Integer.valueOf(tab[2]))); // convert it into a user and add it
			}
			buff.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return liste;
		
	}
	
}
