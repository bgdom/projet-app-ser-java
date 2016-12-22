package blibliotheque;

public class NonInscritException extends Exception {

	public NonInscritException() {
		super("Nous n'avons pas reconnu votre identifiant, veuillez ressaisir vos informations.");
	}
}
