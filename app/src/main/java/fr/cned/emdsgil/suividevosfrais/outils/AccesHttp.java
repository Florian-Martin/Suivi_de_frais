package fr.cned.emdsgil.suividevosfrais.outils;

import android.os.AsyncTask;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Classe outil permettant une connexion avec le protocole HTTP à un serveur distant
 *
 * <p>
 * Cette classe hérite de AsyncTask ce qui lui permet d'exécuter des requêtes asynchrones,
 * ce qui évite de bloquer l'application en attendant la réponse du serveur.
 *
 * <p>
 * Date : 2021
 *
 * @author Florian MARTIN
 * @author emds
 */
public class AccesHttp extends AsyncTask<String, Integer, Long> {

    // -------- VARIABLES --------
    public String retourServeur = "";
    private String parametres = "";
    public AsyncResponse delegate = null; // gestion du retour asynchrone


    // -------- CONSTRUCTEUR --------

    /**
     * Constructeur
     */
    public AccesHttp() {
        super();
    }


    // -------- METHODES --------

    /**
     * Construction de la chaîne de paramètres à envoyer en POST au serveur
     *
     * @param nom
     * @param valeur
     */
    public void addParams(String nom, String valeur) {
        try {
            if (parametres.equals("")) {
                // premier paramètre
                parametres = URLEncoder.encode(nom, "UTF-8") + "=" + URLEncoder.encode(valeur, "UTF-8");
            } else {
                // paramètres suivants (séparés par &)
                parametres += "&" + URLEncoder.encode(nom, "UTF-8") + "=" + URLEncoder.encode(valeur, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This will normally run on a background thread. But to better
     * support testing frameworks, it is recommended that this also tolerates
     * direct execution on the foreground thread, as part of the {@link #execute} call.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param urls The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Long doInBackground(String... urls) {
        // pour éliminer certaines erreurs
        System.setProperty("http.keepAlive", "false");
        // objets pour gérer la connexion, la lecture et l'écriture
        PrintWriter writer = null;
        BufferedReader reader = null;
        HttpURLConnection connexion = null;

        try {
            // création de l'url à partir de l'adresse reçu en paramètre, dans urls[0]
            URL url = new URL(urls[0]);

            // ouverture de la connexion
            connexion = (HttpURLConnection) url.openConnection();
            connexion.setDoOutput(true);

            // choix de la méthode POST pour l'envoi des paramètres
            connexion.setRequestMethod("POST");
            connexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connexion.setFixedLengthStreamingMode(parametres.getBytes().length);

            // création de la requete d'envoi sur la connexion, avec les paramètres
            writer = new PrintWriter(connexion.getOutputStream());
            writer.print(parametres);

            // Une fois l'envoi réalisé, vide le canal d'envoi
            writer.flush();

            // Récupération du retour du serveur
            reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            retourServeur = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // fermeture des canaux d'envoi et de réception
            try {
                writer.close();
            } catch (Exception e) {
            }
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
        return null;
    }


    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.
     * To better support testing frameworks, it is recommended that this be
     * written to tolerate direct execution as part of the execute() call.
     * The default version does nothing.</p>
     *
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param aLong The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     */
    @Override
    protected void onPostExecute(Long aLong) {
        delegate.processFinish(this.retourServeur);
    }
}
