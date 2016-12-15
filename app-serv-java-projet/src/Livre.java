
public class Livre implements Document {
	private int numero;
	private String titre;
	private Abonne reserveur;
	private Abonne emprunteur;

	public Livre(int numero, String titre) {
		this.numero = numero;
		this.titre = titre;
		emprunteur = null;
		reserveur = null;
	}

	@Override
	public int numero() {
		return numero;
	}

	@Override
	public void reserver(Abonne ab) throws PasLibreException {
		if (reserveur != null)
			throw new PasLibreException();

		this.reserveur = ab;

	}

	@Override
	public void emprunter(Abonne ab) throws PasLibreException {
		if (reserveur != null) {
			if (reserveur.equals(ab)) {
				emprunteur = ab;
			} else {
				throw new PasLibreException();
			}

		}

	}

	@Override
	public void retour() {
		if (reserveur !=null && emprunteur !=null){
			reserveur = null;
			emprunteur = null;
		}
		}

}
