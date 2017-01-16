package bibliotheque;

/**
 * represent an exception which is thrown when an identification failed
 * @author guydo
 *
 */
public class NonInscritException extends Exception {

	public NonInscritException() {
		super("Nous n'avons pas reconnu cet identifiant, veuillez ressaisir vos informations.");
	}
}
