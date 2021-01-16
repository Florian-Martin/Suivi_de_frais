package fr.cned.emdsgil.suividevosfrais.modele;

import java.io.Serializable;

/**
 * Classe métier contenant la description d'un frais hors forfait
 *
 * @author emdsgil
 */
public class FraisHf  implements Serializable {

	// -------- VARIABLES --------
	private final Float montant ;
	private final String motif ;
	private final Integer jour ;


	// -------- CONSTRUCTEUR --------
	public FraisHf(Float montant, String motif, Integer jour) {
		this.montant = montant ;
		this.motif = motif ;
		this.jour = jour ;
	}


	// -------- GETTERS & SETTERS --------
	public Float getMontant() {
		return montant;
	}

	public String getMotif() {
		return motif;
	}

	public Integer getJour() {
		return jour;
	}

}
