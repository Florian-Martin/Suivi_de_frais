package fr.cned.emdsgil.suividevosfrais.controleur;

import android.util.Log;
import android.widget.DatePicker;

import fr.cned.emdsgil.suividevosfrais.outils.Global;

/**
 * Classe gérant la partie contrôle du modèle MVC
 * Cette classe permet de gérer la modifications et la persistance des données
 * suites aux interactions des utilisateurs sur les vues
 *
 * @author fmart
 */
public final class Controleur {

    // -------- VARIABLES --------
    private static Controleur controleur = null;
    private int qte, annee, mois;


    // -------- CONSTRUCTEUR --------
    /**
     * Constructeur privé
     * Le but est d'interdire l'instanciation de la classe depuis l'extérieur.
     */
    private Controleur() {
        super();
    }


    // -------- METHODES --------
    /**
     * Fonction d'accès à l'unique instance de la classe
     *
     * @return Controleur.controleur
     * Retourne, ou créée si elle n'existe pas encore, l'unique instance de la classe
     */
    public final static Controleur getControleur() {
        if (Controleur.controleur == null) {
            Controleur.controleur = new Controleur();
        }
        return Controleur.controleur;
    }

    /**
     * Valorisation des propriétés avec les informations affichées
     */
    public void valoriseProprietes(DatePicker datePicker, String typeFrais) {
        annee = datePicker.getYear();
        mois = datePicker.getMonth() + 1;
        // récupération de la qte correspondant au mois actuel
        this.qte = 0;
        Integer key = (annee * 100) + mois;
        if (Global.listFraisMois.containsKey(key)) {
            switch (typeFrais) {
                case "km":
                    this.qte = Global.listFraisMois.get(key).getKm();
                    break;

                case "nuitees":
                    this.qte = Global.listFraisMois.get(key).getNuitee();
                    break;

                case "etapes":
                    this.qte = Global.listFraisMois.get(key).getEtape();
                    break;

                case "repas":
                    this.qte = Global.listFraisMois.get(key).getRepas();
                    break;

                default:
                    Log.d("Erreur: ", "Type de frais manquant");
            }
        }
    }

    /**
     * Mise à jour de la quantité du frais saisie
     * Mise à jour de l'editText en fonction du bouton cliaué ("plus" ou "moins")
     * @param plusMoins Sa valeur dépend du bouton cliqué ("plus" ou "moins")
     */
    public void majQte(String plusMoins){
        if (plusMoins == "plus"){
            this.qte += 10;
        }
        else{
            if (plusMoins == "moins"){
                this.qte = Math.max(0, this.qte - 10); // suppression de 10 si possible
            }
        }
    }


    // -------- GETTERS & SETTERS --------

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte += qte;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int getMois() {
        return mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }
}
