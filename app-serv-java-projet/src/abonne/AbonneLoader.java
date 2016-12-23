package abonne;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import blibliotheque.AbonneFactory;
import blibliotheque.Document;
import blibliotheque.DocumentFactory;

public class AbonneLoader implements AbonneFactory{

	@Override
	public List<Abonne> getAbonneFromFile(String fileName) {
		ArrayList<Abonne> liste = new ArrayList<Abonne>();
		try {
			BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			String line;
			while((line = buff.readLine()) != null){
				String tab[] = line.split(";");
				if(tab.length == 3)
					liste.add(new Abonne(tab[0], tab[1],Integer.valueOf(tab[2])));
			}
			buff.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*for(Abonne a : liste)	{
		System.out.println(	a.getNom() + " "+ a.getId()); 
		
		}*/
		return liste;
		
	}
	
}
