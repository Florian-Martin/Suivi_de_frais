package fr.cned.emdsgil.suividevosfrais.vue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker.OnDateChangedListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Locale;

import fr.cned.emdsgil.suividevosfrais.controleur.Controleur;
import fr.cned.emdsgil.suividevosfrais.modele.FraisMois;
import fr.cned.emdsgil.suividevosfrais.R;
import fr.cned.emdsgil.suividevosfrais.outils.Global;
import fr.cned.emdsgil.suividevosfrais.outils.Outils;
import fr.cned.emdsgil.suividevosfrais.outils.Serializer;

public class KmActivity extends AppCompatActivity {

    // informations affichées dans l'activité
    private Integer qte;
    private Controleur controleur;
    private DatePicker datePicker;
    private String typeFrais = "km";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km);
        setTitle("GSB : Frais kilométriques");

        // modification de l'affichage du DatePicker
        Outils.changeAfficheDate((DatePicker) findViewById(R.id.datKm), false);

        controleur = Controleur.getControleur();
        datePicker = ((DatePicker) findViewById(R.id.datKm));

        // valorisation des propriétés
        controleur.valoriseProprietes(datePicker, typeFrais);
        ((EditText) findViewById(R.id.txtKm)).setText(String.format(Locale.FRANCE, "%d",
                controleur.getQte()));

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
     * Sur le clic du bouton valider : sérialisation
     */
    private void cmdValider_clic() {
        findViewById(R.id.cmdKmValider).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Serializer.serialize(Global.listFraisMois, KmActivity.this);
                retourActivityPrincipale();
            }
        });
    }

    /**
     * Sur le clic du bouton plus : ajout de 10 dans la quantité
     */
    private void cmdPlus_clic() {
        findViewById(R.id.cmdKmPlus).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                controleur.majQte("plus");
                enregNewQte();
            }
        });
    }

    /**
     * Sur le clic du bouton moins : soustrait 10 à la quantité si elle est >= 10
     */
    private void cmdMoins_clic() {
        findViewById(R.id.cmdKmMoins).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                controleur.majQte("moins");
                enregNewQte();
            }
        });
    }


    /**
     * Sur le changement de date : mise à jour de l'affichage de la qte
     */
    private void dat_clic() {
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                new OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        controleur.valoriseProprietes(datePicker, typeFrais);
                        ((EditText) findViewById(R.id.txtKm)).setText(String.format(Locale.FRANCE,
                                "%d", controleur.getQte()));
                    }
                });
    }


    /**
     * Enregistrement dans la zone de texte et dans la liste de la nouvelle qte, à la date choisie
     */
    private void enregNewQte() {
        // enregistrement dans la zone de texte
        ((EditText) findViewById(R.id.txtKm)).setText(String.format(Locale.FRANCE, "%d", controleur.getQte()));
        // enregistrement dans la liste
        Integer key = (controleur.getAnnee() * 100) + controleur.getMois();
        if (!Global.listFraisMois.containsKey(key)) {
            // creation du mois et de l'annee s'ils n'existent pas déjà
            Global.listFraisMois.put(key, new FraisMois(controleur.getAnnee(), controleur.getMois()));
        }
        Global.listFraisMois.get(key).setKm(controleur.getQte());
    }

    /**
     * Sur la selection de l'image : retour au menu principal
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgKmReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale();
            }
        });
    }

    /**
     * Retour à l'activité principale (le menu)
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(KmActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
