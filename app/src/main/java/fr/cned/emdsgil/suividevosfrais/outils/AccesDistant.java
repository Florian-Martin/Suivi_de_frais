package fr.cned.emdsgil.suividevosfrais.outils;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.cned.emdsgil.suividevosfrais.controleur.Controleur;

/**
 * Classe outil permettant le traitement du retour du serveur
 *
 * @author fmart
 * @author emds
 */
public class AccesDistant implements AsyncResponse {

    // -------- CONSTANTES --------
//  private static final String SERVERADDRESS = "http://martinflorian.fr/Projets/GSB_Android/fonctions.php";
    private static final String SERVERADDRESS = "http://192.168.1.12/GSB_Android/fonctions.php";

    // -------- CONSTRUCTEUR --------

    /**
     * Constructeur
     */
    public AccesDistant() {
        super();
    }


    // -------- METHODES --------

    /**
     * Méthode appelée dans le thread principal après retour du serveur
     *
     * @param output Les données retournées par le serveur
     */
    @Override
    public void processFinish(String output) {
        // Affichage du retour du serveur dans la console
        Log.d("Retour serveur: ********** ", output);

        // Découpage du message reçu:
        // message[0] reçoit l'opération demandée ou "Erreur" si la requête n'a pas été exécutée
        // message[1] reçoit le JSON renvoyé par le serveur
        String[] message = output.split("%");

        if (message.length > 1) {
            switch (message[0]) {
                case "Connexion":
                    Log.d("Connexion: ******* ", message[1]);
                    try {
                        JSONObject jsonObject = new JSONObject(message[1]);
                        Boolean resultatLogin = Boolean.parseBoolean(jsonObject.getString("login"));
                        Controleur.setStatutLogin(resultatLogin);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case "Transfert":
                    Log.d("Transfert: ******* ", message[1]);
                    break;

                case "Erreur":
                    Log.d("Erreur: ******* ", message[1]);
                    break;

                default:
            }
        }
    }

    /**
     * @param operation
     * @param data
     */
    public void requeteHttp(String operation, JSONArray lesDonnees, String data) {
        AccesHttp accesHttp = new AccesHttp();
        accesHttp.delegate = this;

        // Ajout des paramètres à l'enveloppe HTTP
        accesHttp.addParams("operation", operation);
        accesHttp.addParams("lesDonnees", lesDonnees.toString());
        accesHttp.addParams("data", data);

        // Appel de execute() de la classe mère AsyncTask de AccesHttp, qui va appeler
        // la méthode doInBackground()
        accesHttp.execute(SERVERADDRESS);
    }
}
