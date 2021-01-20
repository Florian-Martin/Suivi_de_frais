package fr.cned.emdsgil.suividevosfrais.vue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import fr.cned.emdsgil.suividevosfrais.controleur.Controleur;
import fr.cned.emdsgil.suividevosfrais.R;


/**
 * Classe de présentation de l'activity de démarrage de l'application
 * <p>
 * Date : 2021
 *
 * @author emdsgil
 * @author fmart
 */
public class AccueilActivity extends AppCompatActivity {

    // -------- VARIABLES --------
    private Controleur controleur;
    private ImageButton cmdKm, cmdHf, cmdHfRecap, cmdNuitee, cmdRepas, cmdEtape;


    // -------- CYCLE DE VIE --------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("GSB : Suivi des frais");
        cmdKm = findViewById(R.id.cmdKm);
        cmdHf = findViewById(R.id.cmdHf);
        cmdEtape = findViewById(R.id.cmdEtape);
        cmdRepas = findViewById(R.id.cmdRepas);
        cmdNuitee = findViewById(R.id.cmdNuitee);
        cmdHfRecap = findViewById(R.id.cmdHfRecap);

        // Récupération des informations sérialisées
        controleur = Controleur.getControleur();
        controleur.recupSerialize(AccueilActivity.this);

        // Chargement des méthodes événementielles
        onCreateListenersLoading();

        // Transfert des données enregistrées localement vers la base distante
        cmdTransfert_clic();
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
    }


    // -------- EVENEMENTS --------

    /**
     * Sur la sélection d'un bouton dans l'activité principale ouverture de l'activité correspondante
     */
    private void cmdMenu_clic(ImageButton button, final Class classe) {
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // ouvre l'activité
                Intent intent = new Intent(AccueilActivity.this, classe);
                startActivity(intent);
            }
        });
    }

    /**
     * Cas particulier du bouton pour le transfert d'informations vers le serveur
     */
    private void cmdTransfert_clic() {
        findViewById(R.id.cmdTransfert).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // envoi les informations sérialisées vers le serveur
                // en construction
            }
        });
    }
}
