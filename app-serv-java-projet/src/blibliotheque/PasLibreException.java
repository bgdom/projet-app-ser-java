package blibliotheque;

/**
 * represent an exception thrown when a suscriber try to borrow or reserve a book borrowed
 * @author guydo
 *
 */
public class PasLibreException extends Exception {
	public PasLibreException() {
		super("Ce livre n'est pas disponible");
	}
}
