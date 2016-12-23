package blibliotheque;

import java.util.List;

import abonne.Abonne;

public interface AbonneFactory {
	List<Abonne> getAbonneFromFile(String fileName);
}
