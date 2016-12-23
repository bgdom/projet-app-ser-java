package document;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import abonne.Abonne;
import blibliotheque.Document;
import blibliotheque.DocumentFactory;

public class DocumentLoader implements DocumentFactory{

	@Override
	public ArrayList<Document> getDocumentFromFile(String fileName) {
		ArrayList<Document> liste = new ArrayList<Document>();
		try {
			BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			String line;
			while((line = buff.readLine()) != null){
				String tab[] = line.split(";");
				if(tab.length == 2)
					liste.add(new Livre(Integer.valueOf(tab[0]), tab[1]));
			}
			buff.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*for(Document a : liste)	{
			System.out.println(	a.getTitre() + " "+ a.getNumero()); 
			
			}*/
		return liste;
	}
	
}
