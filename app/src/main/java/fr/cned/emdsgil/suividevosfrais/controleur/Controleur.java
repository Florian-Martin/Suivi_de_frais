package fr.cned.emdsgil.suividevosfrais.controleur;

import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;

import fr.cned.emdsgil.suividevosfrais.modele.FraisMois;
import fr.cned.emdsgil.suividevosfrais.outils.Global;
import fr.cned.emdsgil.suividevosfrais.outils.Serializer;

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
    private String typeFrais;


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
        this.annee = datePicker.getYear();
        this.mois = datePicker.getMonth() + 1;
        this.typeFrais = typeFrais;
        this.qte = 0;
        Integer key = (annee * 100) + mois;

        // récupération de la quantité correspondant au mois sélectionné (actuel par défaut)
        if (Global.listFraisMois.containsKey(key)) {
            switch (this.typeFrais) {
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
     *
     * @param plusMoins Sa valeur dépend du bouton cliqué ("plus" ou "moins")
     */
    public void majQte(String plusMoins) {
        if (this.typeFrais != "") {
            if (this.typeFrais == "etapes" || this.typeFrais == "nuitees" || this.typeFrais == "repas") {
                if (plusMoins == "plus") {
                    this.qte += 1;
                } else {
                    if (plusMoins == "moins") {
                        this.qte = Math.max(0, this.qte - 1); // soustraction de 1 si qte >= 1
                    }
                }
            } else {
                if (plusMoins == "plus") {
                    this.qte += 10;
                } else {
                    if (plusMoins == "moins") {
                        this.qte = Math.max(0, this.qte - 10); // soustraction de 10 si qte >= 1
                    }
                }
            }
        }
        enregNewQte();
    }

    /**
     * Enregistrement dans la zone de texte et dans la liste de la nouvelle quantité, à la date choisie
     */
    private void enregNewQte() {
        // enregistrement dans la liste
        Integer key = (this.annee * 100) + this.mois;
        if (!Global.listFraisMois.containsKey(key)) {
            // creation du mois et de l'annee s'ils n'existent pas déjà
            Global.listFraisMois.put(key, new FraisMois(this.annee, this.mois));
        }
        switch (this.typeFrais) {
            case "km":
                Global.listFraisMois.get(key).setKm(this.qte);
                break;

            case "nuitees":
                Global.listFraisMois.get(key).setNuitee(this.qte);
                break;

            case "etapes":
                Global.listFraisMois.get(key).setEtape(this.qte);
                break;

            case "repas":
                Global.listFraisMois.get(key).setRepas(this.qte);
                break;

            default:
                Log.d("Erreur: ", "Type de frais manquant");
        }
    }

    /**
     * Sérialisation des données saisies
     *
     * @param context : le contexte actuel de l'application (ici une Activity de saisie
     *                de frais forfaitisé)
     */
    public void serialize(Context context) {
        Serializer.serialize(Global.listFraisMois, context);
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
