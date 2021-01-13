package fr.cned.emdsgil.suividevosfrais;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Locale;

public class EtapesActivity extends Activity {

    // informations affichées dans l'activité
    private Integer annee ;
    private Integer mois ;
    private Integer qte ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etapes);
        setTitle("GSB : Frais des nuitées");
        // modification de l'affichage du DatePicker
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datEtapes), false) ;
        // valorisation des propriétés
        valoriseProprietes() ;
    }

    /**
     * Valorisation des propriétés avec les informations affichées
     */
    private void valoriseProprietes() {
        annee = ((DatePicker)findViewById(R.id.datEtapes)).getYear() ;
        mois = ((DatePicker)findViewById(R.id.datEtapes)).getMonth() + 1 ;
        // récupération de la qte correspondant au mois actuel
        qte = 0 ;
        Integer key = annee*100+mois ;
        if (Global.listFraisMois.containsKey(key)) {
            qte = Global.listFraisMois.get(key).getEtape() ;
        }
        ((EditText)findViewById(R.id.txtEtapes)).setText(String.format(Locale.FRANCE, "%d", qte)) ;
    }
}
