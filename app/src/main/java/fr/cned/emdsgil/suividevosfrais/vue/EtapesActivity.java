package fr.cned.emdsgil.suividevosfrais.vue;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fr.cned.emdsgil.suividevosfrais.R;
import fr.cned.emdsgil.suividevosfrais.controleur.Controleur;
import fr.cned.emdsgil.suividevosfrais.outils.Outils;

/**
 * Classe de présentation de la saisie de frais forfaitisés d'étapes
 * <p>
 * Date : 2021
 *
 * @author emdsgil
 * @author Florian MARTIN
 */
public class EtapesActivity extends AppCompatActivity {

    // -------- VARIABLES --------
    private Controleur controleur;
    private DatePicker datePicker;
    private final static String TYPE_FRAIS = "etapes";
    private Calendar calendar;


    // -------- CYCLE DE VIE --------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etapes);

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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
        setTitle("GSB : Frais d'étapes");
        datePicker = findViewById(R.id.datEtapes);

        // modification de l'affichage du DatePicker
        Outils.changeAfficheDate(datePicker, true);

        // valorisation de la quantité de frais saisis pour le mois sélectionné
        controleur.valoriseProprietes(datePicker, TYPE_FRAIS);
        ((EditText) findViewById(R.id.txtEtapes)).setText(String.format(Locale.FRANCE, "%d",
                controleur.getQte()));
    }

    /**
     * Chargement des méthodes évènementielles appelées
     * lors d'interactions avec les composants de la vue.
     */
    public void onCreateListenersLoading() {
        imgReturn_clic();
        cmdValider_clic();
        cmdPlus_clic();
        cmdMoins_clic();
        dat_clic();
    }

    /**
     * Retour à l'activité principale (le menu)
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(EtapesActivity.this, AccueilActivity.class);
        startActivity(intent);
    }


    // -------- EVENEMENTS --------

    /**
     * Sur le clic du bouton plus : ajout de 1 dans la quantité
     */
    private void cmdPlus_clic() {
        findViewById(R.id.cmdEtapePlus).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                controleur.majQte("plus");
                // enregistrement dans la zone de texte
                ((EditText) findViewById(R.id.txtEtapes)).setText(String.format(Locale.FRANCE, "%d",
                        controleur.getQte()));
            }
        });
    }

    /**
     * Sur le clic du bouton moins : soustrait 1 à la quantité si elle est >= 1
     */
    private void cmdMoins_clic() {
        findViewById(R.id.cmdEtapeMoins).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                controleur.majQte("moins");
                // enregistrement dans la zone de texte
                ((EditText) findViewById(R.id.txtEtapes)).setText(String.format(Locale.FRANCE, "%d",
                        controleur.getQte()));
            }
        });
    }

    /**
     * Sur le changement de date : mise à jour de l'affichage de la qte
     */
    private void dat_clic() {
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        controleur.valoriseProprietes(datePicker, TYPE_FRAIS);
                        ((EditText) findViewById(R.id.txtEtapes)).setText(String.format(Locale.FRANCE,
                                "%d", controleur.getQte()));
                    }
                });
    }

    /**
     * Sur la selection de l'image : retour au menu principal
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgEtapesReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale();
            }
        });
    }

    /**
     * Sur le clic du bouton valider : sérialisation
     */
    private void cmdValider_clic() {
        findViewById(R.id.cmdEtapesValider).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                controleur.serialize(EtapesActivity.this);
                retourActivityPrincipale();
            }
        });
    }
}
