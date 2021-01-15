package fr.cned.emdsgil.suividevosfrais.vue;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Locale;

import fr.cned.emdsgil.suividevosfrais.R;
import fr.cned.emdsgil.suividevosfrais.outils.Global;
import fr.cned.emdsgil.suividevosfrais.outils.Outils;

public class RepasActivity extends AppCompatActivity {

    // -------- VARIABLES --------
    private Integer annee ;
    private Integer mois ;
    private Integer qte ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repas);
        setTitle("GSB : Frais de repas");
        // modification de l'affichage du DatePicker
        Outils.changeAfficheDate((DatePicker) findViewById(R.id.datRepas), false) ;
        // valorisation des propriétés
        valoriseProprietes() ;
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
            retourActivityPrincipale() ;
        }
        return super.onOptionsItemSelected(item);
    }


    // -------- METHODES --------
    /**
     * Chargement des méthodes évènementielles appelées
     * lors d'interactions avec les composants de la vue.
     */
    public void onCreateListenersLoading(){
        imgReturn_clic();
    }

    /**
     * Retour à l'activité principale (le menu)
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(RepasActivity.this, MainActivity.class) ;
        startActivity(intent) ;
    }

    /**
     * Valorisation des propriétés avec les informations affichées
     */
    private void valoriseProprietes() {
        annee = ((DatePicker)findViewById(R.id.datRepas)).getYear() ;
        mois = ((DatePicker)findViewById(R.id.datRepas)).getMonth() + 1 ;
        // récupération de la qte correspondant au mois actuel
        qte = 0 ;
        Integer key = annee*100+mois ;
        if (Global.listFraisMois.containsKey(key)) {
            qte = Global.listFraisMois.get(key).getRepas() ;
        }
        ((EditText)findViewById(R.id.txtRepas)).setText(String.format(Locale.FRANCE, "%d", qte)) ;
    }


    // -------- EVENEMENTS --------
    /**
     * Sur la selection de l'image : retour au menu principal
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgRepasReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale() ;
            }
        }) ;
    }
}
