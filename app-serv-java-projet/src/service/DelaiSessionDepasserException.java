package service;

public class DelaiSessionDepasserException extends RuntimeException {

	public DelaiSessionDepasserException(){
		super("Votre session a expirer car vous etes restez inactif pendant 3 minute");
	}

}
