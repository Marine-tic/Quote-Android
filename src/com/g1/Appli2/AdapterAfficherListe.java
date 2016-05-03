package com.g1.Appli2;

import android.app.Activity;
import android.content.Context;

import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

//On se sert de la classe BaseAdapter pour la personnalisation, et de l'interface existante pour faire l'action au click
public class AdapterAfficherListe extends BaseAdapter implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    //Désigne la vue de chaque contact
    ArrayList<ContactAndroid> listeCompleteContacts;
    ArrayList<ContactAndroid> listeContactSelectionne;

    //Désigne tout l'environnement, donc le cadre d'execution de l'appli (difficile d'expliquer...)
    Context context;

    public AdapterAfficherListe(Context context, ArrayList<ContactAndroid> listeContacts) {
        this.listeCompleteContacts = listeContacts;
        this.listeContactSelectionne = new ArrayList<>();
        this.context = context;
    }

    public ArrayList<ContactAndroid> getListeContactSelectionne(){
        return this.listeContactSelectionne;
    }

    //Pour chaque objet présent dans la liste
    @Override
    public int getCount() {

        return listeCompleteContacts.size();
    }

    //Je positionne chaque élément de la liste avec un entier
    @Override
    public ContactAndroid getItem(int position) {

        return listeCompleteContacts.get(position);
    }

    //Pour récupérer l'id de l'élément (ou ligne) en fonction de sa position
    @Override
    public long getItemId(int position) {

        return listeCompleteContacts.indexOf(getItem(position));
    }

    //Pour la vue d'un élément
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VueElement vueElement;
        //Au premier appel on initialise le layout
        if (convertView == null) {
            //On instancie le layout xml dans sa correspondance en objet View
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.liste_noms, parent, false);

            //Je fais correspondre là chaque vue du layout avec les éléments d'un objet
            vueElement = new VueElement();
            vueElement.textViewId = (TextView) convertView.findViewById(R.id.textViewId);
            vueElement.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            vueElement.textViewEmail = (TextView) convertView.findViewById(R.id.textViewEmail);
            vueElement.textViewPhoneNumber = (TextView) convertView.findViewById(R.id.textViewPhoneNumber);
            vueElement.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            vueElement.cocheView = (ImageView) convertView.findViewById(R.id.cocheView);

            // Le setTag permet de faire qu'une fois l'association au lieux de le faire correspondre à chaque fois
            //là en gros, ça met une étiquette pour chaque vue je crois
            // Le setTag permet de stocker un objet dans une vue
            convertView.setTag(vueElement);
        } else {
            // Du coup ici, la vueElement existe déjà donc on à juste à faire une getTag pour reprendre tout
            vueElement = (VueElement) convertView.getTag();
        }

        // Je récupère le contact sélectionné
        ContactAndroid contactSelected = getItem(position);
        String allNumbers = "";

        // je parcours ma liste de numero
        for (String phoneNumber: contactSelected.getListPhoneNumber()){
            allNumbers  += phoneNumber + "\n";
        }

        String allEmail = "";
        // Je parcours ma liste d'Email
        for (String email: contactSelected.getListEmail()){
            allEmail +=  email + "\n";
        }
        vueElement.textViewId.setText("");
        vueElement.textViewName.setText(contactSelected.getName());
        vueElement.textViewPhoneNumber.setText(allNumbers);
        vueElement.textViewEmail.setText(allEmail);

        // Créer une Uri à partir d'une String
        Uri uri = Uri.parse(contactSelected.getIdImage());
        vueElement.imageView.setImageURI(uri);

        vueElement.cocheView.setImageResource(contactSelected.getIdCoche());

        convertView.setBackgroundColor(contactSelected.getIdColor());
        notifyDataSetChanged();
        return convertView;
    }


    // Lors d'un long clic je sélectionne l'élement
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // Je crée une instance de ContactAndroid avec l'élement sélectionné dans la liste de tous les contacts
        ContactAndroid contactSelected = this.listeCompleteContacts.get(position);
        if (contactSelected.getIdColor() == Color.TRANSPARENT) {

           VueElement vueElement = new VueElement();
            vueElement.cocheView = (ImageView) view.findViewById(R.id.cocheView);

            // J'affiche un toast qui m'indique que le contact est sélectionné suivi des caractéristique du contact
            Toast toast = Toast.makeText(context, "Contact sélectionné : " + contactSelected.getName(), Toast.LENGTH_SHORT);
            toast.show();


            // Je rajoute le contact sélectionné à la liste des contacts sélectionnés
            listeContactSelectionne.add(contactSelected);
            // Je m'assure de la mise à jour des données de la liste
            notifyDataSetChanged();
            //Je change l'image du coche en selectionné
            contactSelected.setIdCoche(R.drawable.oui);
            // Je change le background color pour indiquer la sélection de l'élément
            contactSelected.setIdColor(Color.rgb(95, 98, 98));
            // Demander à la vue la prise en compte des modifications
            view.setBackgroundColor(contactSelected.getIdColor());

            vueElement.cocheView.setImageResource(contactSelected.getIdCoche());

            return true;
        } else {
            return false;
        }
    }

    // Lors d'un clic rapide, si l'élement est sélectionné je le désélectionne
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ContactAndroid contactSelected = this.listeCompleteContacts.get(position);

        VueElement vueElement = new VueElement();
        vueElement.cocheView = (ImageView) view.findViewById(R.id.cocheView);

        // Si le background color de l'item correspond à la couleur de sélection
        // la coche et le background color de l'élément sont modifiés au même moment du coup
        // Une seule vérification suffit pour savoir si l'élément est coché ou pas
        if (contactSelected.getIdColor() == Color.rgb(95, 98, 98)){
            // J'affiche un toast qui m'indique que le contact est déselectionné suivi des caractéristiques du contact
            Toast toast = Toast.makeText(context, "Contact désélectionné : " + contactSelected.getName(), Toast.LENGTH_SHORT);
            toast.show();
            // Je supprime le contact désélectionné à la liste des contacts sélectionnés
            listeContactSelectionne.remove(contactSelected);
            // Je m'assure de la mise à jour des données de la liste
            notifyDataSetChanged();
            //Je change l'image du coche en déselectionné
            contactSelected.setIdCoche(R.drawable.non);
            // je remet le background color de l'item à transparent pour indiquer la déselection de l'item
            contactSelected.setIdColor(Color.TRANSPARENT);
            // Demander à la vue la prise en compte des modifications
            vueElement.cocheView.setImageResource(contactSelected.getIdCoche());
            view.setBackgroundColor(contactSelected.getIdColor());
        }
    }


    private class VueElement {
        TextView textViewId, textViewName, textViewPhoneNumber, textViewEmail;
        ImageView imageView, cocheView;
    }

}
