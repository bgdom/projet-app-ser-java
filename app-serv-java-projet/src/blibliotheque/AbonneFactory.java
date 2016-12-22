package blibliotheque;

import java.util.List;

public interface AbonneFactory {
	List<Abonne> getAbonneFromFile(String fileName);
}
