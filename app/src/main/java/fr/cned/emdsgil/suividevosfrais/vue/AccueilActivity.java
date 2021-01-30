package fr.cned.emdsgil.suividevosfrais.vue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONException;

import fr.cned.emdsgil.suividevosfrais.R;
import fr.cned.emdsgil.suividevosfrais.controleur.Controleur;


/**
 * Classe de présentation de l'activity contenant le menu principal
 *
 * <p>
 * Présente le menu permettant à l'utilisateur de choisir le type de frais qu'il veut
 * saisir, ou d'envoyer sur la base de données distante les frais qu'il a saisi localement.
 *
 * <p>
 * Date : 2021
 *
 * @author emdsgil
 * @author fmart
 */
public class AccueilActivity extends AppCompatActivity {

    // -------- VARIABLES --------
    private Controleur controleur;
    private ImageButton cmdKm, cmdHf, cmdHfRecap, cmdNuitee, cmdRepas, cmdEtape, cmdTransfert;


    // -------- CYCLE DE VIE --------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controleur = Controleur.getControleur();

        init();

        // Chargement des méthodes événementielles
        onCreateListenersLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    // -------- METHODES --------

    /**
     * Initialisation des objets graphiques et bind des views à des variables
     */
    private void init() {
        setTitle("GSB : Suivi des frais");
        cmdKm = findViewById(R.id.cmdKm);
        cmdHf = findViewById(R.id.cmdHf);
        cmdEtape = findViewById(R.id.cmdEtape);
        cmdRepas = findViewById(R.id.cmdRepas);
        cmdNuitee = findViewById(R.id.cmdNuitee);
        cmdHfRecap = findViewById(R.id.cmdHfRecap);
        cmdTransfert = findViewById(R.id.cmdTransfert);

        // Récupération des informations sérialisées
        controleur.recupSerialize(AccueilActivity.this);
    }

    /**
     * Chargement des méthodes évènementielles appelées
     * lors d'interactions avec les composants de la vue.
     */
    private void onCreateListenersLoading() {
        cmdMenu_clic(cmdKm, KmActivity.class);
        cmdMenu_clic(cmdHf, HfActivity.class);
        cmdMenu_clic(cmdHfRecap, HfRecapActivity.class);
        cmdMenu_clic(cmdNuitee, NuiteesActivity.class);
        cmdMenu_clic(cmdRepas, RepasActivity.class);
        cmdMenu_clic(cmdEtape, EtapesActivity.class);
        cmdTransfert_clic();
    }


    // -------- EVENEMENTS --------

    /**
     * Sur la sélection d'un bouton dans l'activité principale ouverture de l'activité correspondante
     */
    private void cmdMenu_clic(ImageButton button, final Class classe) {
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AccueilActivity.this, classe);
                startActivity(intent);
            }
        });
    }

    /**
     * Transfert d'informations sérialisées vers le serveur
     */
    private void cmdTransfert_clic() {
        cmdTransfert.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // envoi des informations sérialisées vers le serveur
                try {
                    controleur.setContext(AccueilActivity.this);
                    controleur.transfertFraisDb();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
