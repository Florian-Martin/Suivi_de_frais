package fr.cned.emdsgil.suividevosfrais.controleur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import fr.cned.emdsgil.suividevosfrais.R;
import fr.cned.emdsgil.suividevosfrais.controleur.Controleur;
import fr.cned.emdsgil.suividevosfrais.modele.FraisHf;

/**
 * Classe adapter pour les frais hors forfait
 * Cette classe permet de lier les propriétés d'un objet d'une liste
 * à une ligne représentant un objet d'une ListView
 *
 * <p>
 * Date : 2021
 *
 * @author emdsgil
 */
public class FraisHfAdapter extends BaseAdapter {

    // -------- VARIABLES --------
    private final ArrayList<FraisHf> lesFrais; // liste des frais du mois
    private final LayoutInflater inflater;
    private Context context;


    // -------- CONSTRUCTEUR --------

    /**
     * Constructeur de l'adapter pour valoriser les propriétés
     *
     * @param context  Accès au contexte de l'application
     * @param lesFrais Liste des frais hors forfait
     */
    public FraisHfAdapter(Context context, ArrayList<FraisHf> lesFrais) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lesFrais = lesFrais;
    }


    // -------- METHODES --------

    /**
     * retourne le nombre d'éléments de la listview
     */
    @Override
    public int getCount() {
        return lesFrais.size();
    }

    /**
     * retourne l'item de la listview à un index précis
     */
    @Override
    public Object getItem(int index) {
        return lesFrais.get(index);
    }

    /**
     * retourne l'index de l'élément actuel
     */
    @Override
    public long getItemId(int index) {
        return index;
    }


    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param index    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_liste, parent, false);
            holder.txtListJour = convertView.findViewById(R.id.txtListJour);
            holder.txtListMontant = convertView.findViewById(R.id.txtListMontant);
            holder.txtListMotif = convertView.findViewById(R.id.txtListMotif);
            holder.btnSuppr = convertView.findViewById(R.id.cmdSuppHf);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtListJour.setText(String.format(Locale.FRANCE, "%d",
                lesFrais.get(index).getJour()));
        holder.txtListMontant.setText(String.format(Locale.FRANCE, "%.2f",
                lesFrais.get(index).getMontant()));
        holder.txtListMotif.setText(lesFrais.get(index).getMotif());
        holder.btnSuppr.setTag(index);

        // Evènement au clic sur le bouton supprimer
        holder.btnSuppr.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controleur controleur = Controleur.getControleur();
                lesFrais.remove(index);
                controleur.suppFraisHf(index, context);
                notifyDataSetChanged(); // Rafraîchit la liste visuellement dès la suppression
            }
        });
        return convertView;
    }


    /**
     * Structure contenant les éléments d'une ligne
     */
    private class ViewHolder {
        TextView txtListJour;
        TextView txtListMontant;
        TextView txtListMotif;
        ImageView btnSuppr;
    }

}
