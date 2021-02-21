package fr.cned.emdsgil.suividevosfrais.outils;


import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.cned.emdsgil.suividevosfrais.controleur.Controleur;

/**
 * Classe outil permettant le traitement du retour du serveur
 *
 * @author Florian MARTIN
 * @author emds
 */
public class AccesDistant implements AsyncResponse {

    // -------- CONSTANTES --------
    // Adresse pour utiliser la base distante
    // private static final String SERVERADDRESS = "http://martinflorian.fr/Projets/GSB_Android/fonctions.php";
    // Adresse pour le travail en local
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
        Controleur controleur = Controleur.getControleur();

        // Affichage du retour du serveur dans la console
        Log.d("Retour serveur ********** ", output);

        // Découpage du message reçu:
        // message[0] = l'opération demandée ou "Erreur" si problème lors de la création du PDO
        // message[1] = la liste des clés des frais transférés si tout s'est bien passé
        //              lors du transfert, sinon "Erreur" si un problème est survenu
        // message[2] = le message décrivant l'erreur survenue ou rien si tout s'est bien passé
        // message[3] = les clés des frais transférés avant que ne survienne l'erreur
        String[] message = output.split("%");

        if (message.length > 1) {
            switch (message[0]) {
                case "Connexion":
                    Log.d("Connexion ******* ", message[1]);
                    try {
                        JSONObject infosConnexion = new JSONObject(message[1]);
                        String isLoginValid = infosConnexion.getString("isLogInValid");
                        String idVisiteur = infosConnexion.getString("idVisiteur");

                        Boolean resultatLogin = Boolean.parseBoolean(isLoginValid);
                        controleur.isLoginValid(resultatLogin);
                        controleur.setIdVisiteur(idVisiteur);
                    } catch (JSONException e) {
                        controleur.isLoginValid(false);
                        Log.d("Erreur authentification:", "Conversion du JSON impossible " + e.getMessage());
                    }
                    break;

                case "Transfert":
                    Log.d("Transfert ******* ", message[1]);

                    if (!message[1].equals("Erreur")){
                        // Les frais ont été intégralement transférés vers la base distante
                        try{
                            controleur.majListeFrais(null);
                            controleur.serialize(controleur.getContext());
                        }
                        catch (JSONException e){
                            Log.d("Erreur transfert:", "Conversion du JSON impossible " + e.getMessage());
                        }
                    }
                    else{
                        // Une partie des frais a potentiellement pu être transférée
                        try{
                            controleur.majListeFrais(new JSONArray(message[3]));
                            controleur.serialize(controleur.getContext());
                        }
                        catch (JSONException e){
                            Log.d("Erreur transfert:", "Conversion du JSON impossible " + e.getMessage());
                        }
                    }
                    break;

                case "Erreur":
                    Log.d("Erreur PDO: ******* ", message[1]);
                    break;

                default:
            }
        }
    }

    /**
     * @param operation Le type d'opération à mener côté serveur :
     *                  test de validité du login ou transfert de données saisies vers la base distante
     * @param jsonArray La liste de frais de l'utilisateur formaté en JSON
     * @param data      Les informations de connexion utilisateur
     */
    public void requeteHttp(String id, String operation, JSONArray jsonArray, String data) {
        AccesHttp accesHttp = new AccesHttp();
        accesHttp.delegate = this;

        // Ajout des paramètres à l'enveloppe HTTP
        accesHttp.addParams("id", id);
        accesHttp.addParams("operation", operation);
        accesHttp.addParams("data", data);
        accesHttp.addParams("listeFrais", jsonArray.toString());

        // Appel de execute() de la classe mère AsyncTask de AccesHttp, qui va appeler
        // la méthode doInBackground()
        accesHttp.execute(SERVERADDRESS);
    }
}
