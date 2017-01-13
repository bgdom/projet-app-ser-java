package document;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import bibliotheque.Bibliotheque;
import bibliotheque.Document;
import bibliotheque.DocumentFactory;

/**
 * represent an implementation of the DocumentFactory interface
 * @author guydo
 *
 */
public class DocumentLoader implements DocumentFactory{
	
	private Bibliotheque bi;
	
	public DocumentLoader(Bibliotheque b){
		bi = b;
	}

	@Override
	public ArrayList<Document> getDocumentFromFile(String fileName) {
		ArrayList<Document> liste = new ArrayList<Document>();
		try {
			BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			String line;
			while((line = buff.readLine()) != null){
				String tab[] = line.split(";");
				if(tab.length == 2)
					liste.add(new Livre(Integer.valueOf(tab[0]), tab[1], bi));
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
