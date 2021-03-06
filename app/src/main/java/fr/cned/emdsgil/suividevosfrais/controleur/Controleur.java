package fr.cned.emdsgil.suividevosfrais.controleur;

import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import fr.cned.emdsgil.suividevosfrais.modele.FraisHf;
import fr.cned.emdsgil.suividevosfrais.modele.FraisMois;
import fr.cned.emdsgil.suividevosfrais.outils.AccesDistant;
import fr.cned.emdsgil.suividevosfrais.outils.Serializer;
import fr.cned.emdsgil.suividevosfrais.vue.LoginActivity;

/**
 * Classe gérant la partie contrôle du modèle MVC
 * <p>
 * Cette classe permet de gérer les modifications et la persistance des données
 * suites aux interactions des utilisateurs sur les vues
 * <p>
 * Date : 2021
 *
 * @author Florian MARTIN
 * @author emdsgil
 */
public final class Controleur {

    // -------- VARIABLES --------
    private static Controleur controleur = null;
    private Context context;
    private int qte, annee, mois, jour, key;
    private String typeFrais, motif, idVisiteur;
    private Float montant;
    private FraisMois fraisMois;
    private ArrayList<FraisHf> lesFraisHf;
    private static AccesDistant accesDistant;

    // tableau d'informations mémorisées
    private static Hashtable<Integer, FraisMois> listFraisMois = new Hashtable<>();
    /* Retrait du type de l'Hashtable (Optimisation Android Studio)
     * Original : Typage explicite =
     * public static Hashtable<Integer, FraisMois> listFraisMois = new Hashtable<Integer, FraisMois>();
     */


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
     * Récupération de l'unique instance de la classe
     * <p>
     * Retourne, ou créée si elle n'existe pas encore, l'unique instance de la classe
     *
     * @return Controleur.controleur L'unique instance possible de la classe
     */
    public static Controleur getControleur() {
        if (Controleur.controleur == null) {
            Controleur.controleur = new Controleur();
            accesDistant = new AccesDistant();
        }
        return Controleur.controleur;
    }


    /**
     * Valorisation des propriétés avec les informations affichées sur les différentes views
     */
    public void valoriseProprietes(DatePicker datePicker, String typeFrais) {
        this.annee = datePicker.getYear();
        this.mois = datePicker.getMonth() + 1;
        this.typeFrais = typeFrais;
        this.qte = 0;
        this.key = (this.annee * 100) + this.mois;

        // récupération de la quantité correspondant au mois sélectionné (actuel par défaut)
        if (listFraisMois.containsKey(key)) {
            this.fraisMois = listFraisMois.get(key);

            switch (this.typeFrais) {
                case "km":
                    this.qte = fraisMois.getKm();
                    break;

                case "nuitees":
                    this.qte = fraisMois.getNuitee();
                    break;

                case "etapes":
                    this.qte = fraisMois.getEtape();
                    break;

                case "repas":
                    this.qte = fraisMois.getRepas();
                    break;

                case "recupFraisHf":
                    this.lesFraisHf = fraisMois.getLesFraisHf();

                default:
            }
        } else if (typeFrais.equals("recupFraisHf")) {
            lesFraisHf = new ArrayList<>();
        }
    }

    /**
     * Mise à jour de la quantité du frais saisi
     * <p>
     * Mise à jour de l'editText en fonction du bouton cliqué ("plus" ou "moins")
     *
     * @param plusMoins Sa valeur dépend du bouton cliqué ("plus" ou "moins")
     */
    public void majQte(String plusMoins) {
        if (this.typeFrais != null) {
            if (this.typeFrais.equals("etapes") || this.typeFrais.equals("nuitees")
                    || this.typeFrais.equals("repas")) {
                if (plusMoins.equals("plus")) {
                    this.qte += 1;
                } else if (plusMoins.equals("moins")) {
                    this.qte = Math.max(0, this.qte - 1); // soustraction de 1 si qte >= 1
                }
            } else {
                if (plusMoins.equals("plus")) {
                    this.qte += 10;
                } else if (plusMoins.equals("moins")) {
                    this.qte = Math.max(0, this.qte - 10); // soustraction de 10 si qte >= 10
                }
            }
        }
        enregNewQte();
    }

    /**
     * Enregistrement dans la zone de texte et dans la liste de la nouvelle quantité, à la date choisie
     */
    public void enregNewQte() {

        // création du frais s'il n'existe pas encore pour ce mois
        if (!listFraisMois.containsKey(key)) {
            listFraisMois.put(key, new FraisMois(this.annee, this.mois));
            this.fraisMois = listFraisMois.get(this.key);
        }

        switch (this.typeFrais) {
            case "km":
                fraisMois.setKm(this.qte);
                break;

            case "nuitees":
                fraisMois.setNuitee(this.qte);
                break;

            case "etapes":
                fraisMois.setEtape(this.qte);
                break;

            case "repas":
                fraisMois.setRepas(this.qte);
                break;

            case "hf":
                fraisMois.addFraisHf(montant, motif, jour);

            default:
                Log.d("ERREUR: ", "Type de frais inconnu");
        }
    }

    /**
     * Supprime un frais de la liste de frais hors forfait
     *
     * @param index   L'index du frais hors forfait à supprimer dans la liste
     * @param context Le contexte de l'activity sur laquelle on demande la suppression
     */
    public void suppFraisHf(int index, Context context) {
        fraisMois.supprFraisHf(index);
        serialize(context);
    }

    /**
     * Envoi d'une requête HTTP afin de tester la validité des informations de login saisies
     *
     * @param infosLogin La concaténation de l'identifiant et du password
     */
    public void logIn(String infosLogin) {
        accesDistant.requeteHttp("", "connexion", new JSONArray(), infosLogin);
    }

    /**
     * Récupération du résultat du test de validité de l'authentification
     *
     * @param statutLogin True si le test effectué côté serveur est validé
     */
    public void isLoginValid(Boolean statutLogin) {
        ((LoginActivity) context).isLoginValid(statutLogin);
    }

    /**
     * Transfert de la liste de frais mensuels vers la base distante
     *
     * @throws JSONException
     */
    public void transfertFraisDb() throws JSONException {
        Set keys = listFraisMois.keySet();
        Iterator itr = keys.iterator();
        String key;
        JSONArray jsonArray = new JSONArray();

        while (itr.hasNext()) {
            key = itr.next().toString();

            FraisMois fraisMois = listFraisMois.get(Integer.valueOf(key));
            try {
                jsonArray.put(FraisMois.toJSON(fraisMois));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("***********", jsonArray.toString());
        accesDistant.requeteHttp(this.idVisiteur, "transfert", jsonArray, "");
    }

    /**
     * Mise à jour de la liste de frais du visiteur
     * <p>
     * Si lors du transfert vers la base distante tout s'est bien déroulé la méthode reçoit
     * un JSONArray vide et la liste de frais du visiteur est vidée.
     * <p>
     * S'il y a eu un problème lors du transfert, le paramètre reçu contient un JSONArray
     * comportant les clés des frais qui ont pu être transférés avant que le problème survienne.
     *
     * @param jsonArray un JSONArray composé des clés des frais à supprimer ou vide
     * @throws JSONException
     */
    public void majListeFrais(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null) {
            listFraisMois.clear();
            Toast.makeText(controleur.getContext(), "Transfert réussi !", Toast.LENGTH_LONG).show();
        } else {
            for (int i = 0; i < jsonArray.length(); i++) {
                listFraisMois.remove(Integer.parseInt(jsonArray.get(i).toString()));
                Log.d("SUPPRLISTE mois = ", jsonArray.get(i).toString());
            }
            Toast.makeText(controleur.getContext(), "Transfert partiellement réussi", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Récupère la sérialisation si elle existe
     *
     * @param context : le contexte actuel de l'application
     */
    public void recupSerialize(Context context) {
        /* Pour éviter le warning "Unchecked cast from Object to Hash" produit par un casting direct :
         * Global.listFraisMois = (Hashtable<Integer, FraisMois>) Serializer.deSerialize(Global.filename, MainActivity.this);
         * On créé un Hashtable générique <?,?> dans lequel on récupère l'Object retourné par la méthode deSerialize, puis
         * on cast chaque valeur dans le type attendu.
         * Seulement ensuite on affecte cet Hastable à Global.listFraisMois.
         */
        Hashtable<?, ?> monHash = (Hashtable<?, ?>) Serializer.deSerialize(context);

        if (monHash != null) {
            Hashtable<Integer, FraisMois> monHashCast = new Hashtable<>();

            for (Hashtable.Entry<?, ?> entry : monHash.entrySet()) {
                monHashCast.put((Integer) entry.getKey(), (FraisMois) entry.getValue());
            }
            listFraisMois = monHashCast;
        }

        // si rien n'a été récupéré, il faut créer la liste
        if (listFraisMois == null) {
            listFraisMois = new Hashtable<>();
            /* Retrait du type de l'HashTable (Optimisation Android Studio)
             * Original : Typage explicite : listFraisMois = new Hashtable<Integer, FraisMois>();
             */
        }
    }

    /**
     * Sérialisation des données saisies
     *
     * @param context : le contexte actuel de l'application (ici une Activity de saisie
     *                de frais forfaitisé)
     */
    public void serialize(Context context) {
        Serializer.serialize(listFraisMois, context);
    }


    // -------- GETTERS & SETTERS --------

    public int getQte() {
        return qte;
    }

    public void setJour(int jour) {
        this.jour = jour;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public ArrayList<FraisHf> getLesFraisHf() {
        return lesFraisHf;
    }

    public void setContext(Context contexte) {
        this.context = contexte;
    }

    public Context getContext() {
        return this.context;
    }

    public void setIdVisiteur(String idVisiteur) {
        this.idVisiteur = idVisiteur;
    }
}
