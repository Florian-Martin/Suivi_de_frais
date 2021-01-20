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
 * Classe de présentation de la saisie de frais hors forfait
 * <p>
 * Date : 2021
 *
 * @author emdsgil
 * @author fmart
 */
public class HfActivity extends AppCompatActivity {

    // -------- VARIABLES --------
    private Controleur controleur;
    private DatePicker datePicker;
    private EditText editText;
    private final String typeFrais = "hf";


    // -------- CYCLE DE VIE --------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf);
        setTitle("GSB : Frais HF");
        controleur = Controleur.getControleur();
        datePicker = findViewById(R.id.datHf);
        editText = findViewById(R.id.txtHf);

        // modification de l'affichage du DatePicker
        Outils.changeAfficheDate(datePicker, true);

        // mise à 0 du montant
        editText.setText("0");

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
     * Chargement des méthodes évènementielles appelées
     * lors d'interactions avec les composants de la vue.
     */
    private void onCreateListenersLoading() {
        imgReturn_clic();
        cmdAjouter_clic();
    }

    /**
     * Enregistrement du nouveau frais hors forfait
     * Récupération des informations saisies puis enregistrement dans la liste
     */
    private void enregListe() {
        controleur.setJour(datePicker.getDayOfMonth());
        controleur.valoriseProprietes(datePicker, typeFrais);
        controleur.setMotif(((EditText) findViewById(R.id.txtHfMotif)).getText().toString());
        controleur.setMontant(Float.valueOf(editText.getText().toString()));
        controleur.enregNewQte();
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
     * Sur le clic du bouton ajouter :
     * enregistrement dans la liste
     * sérialisation
     * retour au menu principal
     */
    private void cmdAjouter_clic() {
        findViewById(R.id.cmdHfAjouter).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                enregListe();
                controleur.serialize(HfActivity.this);
                retourActivityPrincipale();
            }
        });
    }

    /**
     * Sur la selection de l'image : retour au menu principal
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgHfReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale();
            }
        });
    }
}
