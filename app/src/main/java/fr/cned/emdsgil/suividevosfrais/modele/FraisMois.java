package fr.cned.emdsgil.suividevosfrais.modele;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe métier contenant les informations des frais d'un mois
 */
public class FraisMois implements Serializable {

    // -------- VARIABLES --------
    private Integer mois; // mois concerné
    private Integer annee; // année concernée
    private Integer etape; // nombre d'étapes du mois
    private Integer km; // nombre de km du mois
    private Integer nuitee; // nombre de nuitées du mois
    private Integer repas; // nombre de repas du mois
    private final ArrayList<FraisHf> lesFraisHf; // liste des frais hors forfait du mois


    // -------- CONSTRUCTEUR --------
    public FraisMois(Integer annee, Integer mois) {
        this.annee = annee;
        this.mois = mois;
        this.etape = 0;
        this.km = 0;
        this.nuitee = 0;
        this.repas = 0;
        lesFraisHf = new ArrayList<>();
        /* Retrait du type de l'ArrayList (Optimisation Android Studio)
         * Original : Typage explicit =
         * lesFraisHf = new ArrayList<FraisHf>() ;
         */
    }


    // -------- METHODES --------

    /**
     * Ajout d'un frais hors forfait
     *
     * @param montant Montant en euros du frais hors forfait
     * @param motif   Justification du frais hors forfait
     */
    public void addFraisHf(Float montant, String motif, Integer jour) {
        lesFraisHf.add(new FraisHf(montant, motif, jour));
    }

    /**
     * Suppression d'un frais hors forfait
     *
     * @param index Indice du frais hors forfait à supprimer
     */
    public void supprFraisHf(Integer index) {
        lesFraisHf.remove(index);
    }

    /**
     * Conversion d'un objet FraisMois en JSONObject
     *
     * @param frais L'objet FraisMois à parser
     * @return Un JSONObject après avoir parsé l'objet FraisMois
     * @throws JSONException
     */
    public static JSONObject toJSON(Object frais) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        jsonObject.put("annee", ((FraisMois) frais).getAnnee());
        jsonObject.put("mois", ((FraisMois) frais).getMois());
        jsonObject.put("etape", ((FraisMois) frais).getEtape());
        jsonObject.put("km", ((FraisMois) frais).getKm());
        jsonObject.put("nuitee", ((FraisMois) frais).getNuitee());
        jsonObject.put("repas", ((FraisMois) frais).getRepas());

        for (int i = 0; i < ((FraisMois) frais).lesFraisHf.size(); i++) {
            JSONObject listeFraisHf = new JSONObject();
            listeFraisHf.put("motif", ((FraisMois) frais).lesFraisHf.get(i).getMotif());
            listeFraisHf.put("montant", ((FraisMois) frais).lesFraisHf.get(i).getMontant());
            listeFraisHf.put("jour", ((FraisMois) frais).lesFraisHf.get(i).getJour());
            jsonArray.put(listeFraisHf);
        }
        jsonObject.put("fraisHf", jsonArray);
        return jsonObject;
    }


    // -------- GETTERS & SETTERS --------
    public Integer getMois() {
        return mois;
    }

    public void setMois(Integer mois) {
        this.mois = mois;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Integer getEtape() {
        return etape;
    }

    public void setEtape(Integer etape) {
        this.etape = etape;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Integer getNuitee() {
        return nuitee;
    }

    public void setNuitee(Integer nuitee) {
        this.nuitee = nuitee;
    }

    public Integer getRepas() {
        return repas;
    }

    public void setRepas(Integer repas) {
        this.repas = repas;
    }

    public ArrayList<FraisHf> getLesFraisHf() {
        return lesFraisHf;
    }

}
