package fr.cned.emdsgil.suividevosfrais.outils;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.lang.reflect.Field;

/**
 * Classe permettant l'utilisation de fonctions utilitaires diverses
 *
 * @author Florian MARTIN
 * @author emdsgil
 */
public abstract class Outils {

    // -------- METHODES --------

    /**
     * Modification de l'affichage de la date (juste le mois et l'année, sans le jour)
     */
    public static void changeAfficheDate(DatePicker datePicker, boolean dateMin) {
        try {
            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id",
                    "android");
            datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                    null);
            if (daySpinnerId != 0) {
                View daySpinner = datePicker.findViewById(daySpinnerId);
                daySpinner.setVisibility(View.GONE);
            }
            if (dateMin) {
                datePicker.setMinDate(System.currentTimeMillis() - 1000);
                datePicker.setMaxDate(System.currentTimeMillis() - 1000);
            }
        } catch (SecurityException | IllegalArgumentException e) {
            Log.d("ERROR", e.getMessage());
        }
    }
}
