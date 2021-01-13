package fr.cned.emdsgil.suividevosfrais;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Locale;

public class RepasActivity extends Activity {

    // informations affichées dans l'activité
    private Integer annee ;
    private Integer mois ;
    private Integer qte ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repas);
        setTitle("GSB : Frais de repas");
        // modification de l'affichage du DatePicker
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datRepas), false) ;
        // valorisation des propriétés
        valoriseProprietes() ;
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
}
