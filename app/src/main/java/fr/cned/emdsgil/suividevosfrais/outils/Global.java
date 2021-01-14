package fr.cned.emdsgil.suividevosfrais.outils;


import java.util.Hashtable;

import fr.cned.emdsgil.suividevosfrais.modele.FraisMois;

public abstract class Global {

    // tableau d'informations mémorisées
    public static Hashtable<Integer, FraisMois> listFraisMois = new Hashtable<>();
    /* Retrait du type de l'Hashtable (Optimisation Android Studio)
     * Original : Typage explicit =
	 * public static Hashtable<Integer, FraisMois> listFraisMois = new Hashtable<Integer, FraisMois>();
	*/

    // fichier contenant les informations sérialisées
    public static final String FILENAME = "save.fic";

}
