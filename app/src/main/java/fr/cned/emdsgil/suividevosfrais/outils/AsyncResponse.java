package fr.cned.emdsgil.suividevosfrais.outils;

/**
 * Interface utilisée pour redéfinir la méthode processFinish() au retour du serveur
 *
 * @author emds
 */
public interface AsyncResponse {

    void processFinish(String output);
}
