package fr.cned.emdsgil.suividevosfrais.vue;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import fr.cned.emdsgil.suividevosfrais.controleur.Controleur;
import fr.cned.emdsgil.suividevosfrais.R;
import fr.cned.emdsgil.suividevosfrais.outils.Outils;

/**
 * Classe de présentation de l'activity de saisie de frais hors forfait
 * <p>
 * Date : 2021
 *
 * @author emdsgil
 * @author Florian MARTIN
 */
public class HfActivity extends AppCompatActivity {

    // -------- VARIABLES --------
    private Controleur controleur;
    private DatePicker datePicker;
    private EditText etMontant, etMotif;
    private final static String TYPEFRAIS = "hf";


    // -------- CYCLE DE VIE --------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf);

        controleur = Controleur.getControleur();

        init();

        // chargement des méthodes évènementielles
        onCreateListenersLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.retour_accueil))) {
            retourActivityPrincipale();
        }
        return super.onOptionsItemSelected(item);
    }


    // -------- METHODES --------

    /**
     * Initialisation des objets graphiques et bind des views à des variables
     */
    private void init() {
        setTitle("GSB : Frais HF");
        datePicker = findViewById(R.id.datHf);
        etMontant = findViewById(R.id.txtHf);
        etMotif = findViewById(R.id.txtHfMotif);

        // mise à 0 du montant
        etMontant.setText("0");
    }

    /**
     * Chargement des méthodes évènementielles appelées
     * lors d'interactions avec les composants de la vue.
     */
    private void onCreateListenersLoading() {
        imgReturn_clic();
        cmdAjouter_clic();
    }

    /**
     * Enregistrement du nouveau frais hors forfait
     * <p>
     * Récupération des informations saisies puis enregistrement dans la liste de frais si les
     * champs ont été correctement remplis
     */
    private void enregListe() {
            controleur.setJour(datePicker.getDayOfMonth());
            controleur.valoriseProprietes(datePicker, TYPEFRAIS);
            controleur.setMotif(etMotif.getText().toString());
            controleur.setMontant(Float.valueOf(etMontant.getText().toString()));
            controleur.enregNewQte();
    }

    /**
     * Teste si les champs "motif" et "montant" sont correctement remplis
     *
     * @return true si c'est le cas
     */
    private boolean champsRemplis() {
        boolean champsRemplis = false;
        String etMontantToString = etMontant.getText().toString().trim();
        String etMotifToString = etMotif.getText().toString().trim();

        if (etMontantToString.isEmpty()) {
            etMontant.setError("Veuillez renseigner un montant");
        } else if (etMotifToString.isEmpty()) {
            etMotif.setError("Veuillez renseigner un motif");
        } else {
            etMontant.setError(null);
            etMotif.setError(null);
            champsRemplis = true;
        }
        return champsRemplis;
    }

    /**
     * Retour à l'activité principale (le menu)
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(HfActivity.this, AccueilActivity.class);
        startActivity(intent);
    }


    // -------- EVENEMENTS --------

    /**
     * Au clic sur le bouton "ajouter" :
     * enregistrement dans la liste
     * sérialisation
     * retour au menu principal
     */
    private void cmdAjouter_clic() {
        findViewById(R.id.cmdHfAjouter).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (champsRemplis()){
                    enregListe();
                    controleur.serialize(HfActivity.this);
                    retourActivityPrincipale();
                }
            }
        });
    }

    /**
     * Au clic sur l'image : retour au menu principal
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgHfReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale();
            }
        });
    }
}
