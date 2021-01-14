package fr.cned.emdsgil.suividevosfrais.controleur;

/**
 * Classe gérant la partie contrôle du modèle MVC
 * Cette classe permet de gérer la modifications et la persistance des données
 * suites aux interactions des utilisateurs sur les vues
 *
 * @author fmart
 *
 */
public class Controleur {

    private static Controleur controleur = null;

    /**
     * Constructeur privé
     * Le but est d'interdire l'instanciation de la classe depuis l'extérieur.
     */
    private Controleur(){
        super();
    }

    /**
     * Fonction d'accès à l'unique instance de la classe
     * @return Controleur.controleur
     * Retourne, ou créée si elle n'existe pas encore, l'unique instance de la classe
     */
    private static final Controleur getControleur(){
        if (Controleur.controleur == null){
            Controleur.controleur = new Controleur();
        }
        return Controleur.controleur;
    }
}
