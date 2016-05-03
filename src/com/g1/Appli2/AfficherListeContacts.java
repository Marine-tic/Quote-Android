package com.g1.Appli2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AfficherListeContacts extends Activity {

    Button valider1;
    ListView listView;
    Button selectAll;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (ListView) findViewById(R.id.contact);
        valider1 = (Button) findViewById(R.id.valider1);
        selectAll = (Button) findViewById(R.id.selectAll);

        /************************Ancien code avec données en dure***************************
         * (En supposant qu'il existe encore la class Contact)
         *
         String[] noms = new String[]{"Serge Karamazov", "Odile Deray", "Simon Jérémi", "Émile Gravier"};
         String[] numero = {"0625418532", "0420308545", "0625143289", "0652189865"};
         int[] image = {R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher};
         int[] coche = {R.drawable.non, R.drawable.non, R.drawable.non, R.drawable.non};

         ArrayList<Contact> listContact = new ArrayList<Contact>();


         for (int i = 0; i < noms.length; i++) {
         listContact.add(new Contact(noms[i], numero[i], image[i], coche[i]));
         }
         */

        ArrayList<ContactAndroid> listContacts = new ArrayList<>();
        listContacts = fetchContacts(listContacts);

        // Tri des contacts par ordre Alphabétique en fonction du nom
        Collections.sort(listContacts, new Comparator<ContactAndroid>() {
            @Override
            public int compare(ContactAndroid contactAndroid1, ContactAndroid contactAndroid2) {
                return contactAndroid1.getName().compareTo(contactAndroid2.getName());            }
        });

        // Pour afficher les contacts depuis l'adapterPerso
        final AdapterAfficherListe adapterAfficherListe = new AdapterAfficherListe(this, listContacts);

        listView.setAdapter(adapterAfficherListe);
        listView.setOnItemLongClickListener(adapterAfficherListe);
        listView.setOnItemClickListener(adapterAfficherListe);


        /****************Bouton Valider, qui verifie si oui ou non un contact est selectionner. Si oui il envoie les données***********************/
        final Intent myIntent = new Intent(this, Recapitulatif.class);
        final ArrayList<ContactAndroid> listeContactsSelectionnes = adapterAfficherListe.getListeContactSelectionne();
        valider1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (listeContactsSelectionnes.isEmpty()) {
                    Toast.makeText(AfficherListeContacts.this, R.string.noContactSelected, Toast.LENGTH_SHORT).show();
                } else {
                    myIntent.putExtra("listeContact", listeContactsSelectionnes);
                    startActivity(myIntent);
                }
            }
        });

        /****************Bouton pour tout selectioner TOUS les contacts et qui les envoie a l'activité 2***********************/
        final ArrayList<ContactAndroid> finalListContacts = listContacts;
        selectAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choix) {
                        switch (choix) {
                            case DialogInterface.BUTTON_POSITIVE:
                                for (ContactAndroid finalListContact : finalListContacts) {
                                    finalListContact.setIdCoche(R.drawable.oui);
                                    finalListContact.setIdColor(Color.rgb(95, 98, 98));
                                    listeContactsSelectionnes.add(finalListContact);
                                }
                                AdapterAfficherListe adapterAfficherListe = new AdapterAfficherListe(getApplicationContext(), listeContactsSelectionnes);
                                listView.setAdapter(adapterAfficherListe);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                // texte pour la boite de dialogue de envoi de selection à tout les contacts
                AlertDialog.Builder builder = new AlertDialog.Builder(AfficherListeContacts.this);
                builder.setMessage("Etes vous sûr de vouloir sélectionner tous les contacts ?").setPositiveButton("Oui", dialogClickListener).setNegativeButton("Non", dialogClickListener).show();
            }
        });
    }

    /***************** Récupération des contact du téléphone et envoi dans une liste de contact android***********************/
    // On déclare les variables et leurs types
    public ArrayList<ContactAndroid> fetchContacts(ArrayList<ContactAndroid> listeContacts) {
        String contact_id;
        String name;
        ArrayList<String> listPhoneNumber;
        String phoneNumber;
        ArrayList<String> listEmail;
        String email;
        String idImage = "";
        int idCoche = R.drawable.non;
        int idColor = Color.TRANSPARENT;


        // Permet d'intéragir avec le contenu du téléphone
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String idImageParDefaut = ContentResolver.SCHEME_ANDROID_RESOURCE +
        "://" + getResources().getResourcePackageName(R.drawable.default_profile)
        + '/' + getResources().getResourceTypeName(R.drawable.default_profile) + '/' +
                getResources().getResourceEntryName(R.drawable.default_profile);

        // On boucle sur chaque contact (tableau pour les cas ou il peu y avoir plusieurs numéros/...)
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                listPhoneNumber = new ArrayList<>();
                listEmail = new ArrayList<>();
                contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    // On demande et on boucle sur chaque numéro de téléphone du contact
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        listPhoneNumber.add(phoneNumber);
                    }

                    phoneCursor.close();

                    // On demande et on boucle sur chaque adresse email du contact
                    Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while (emailCursor.moveToNext()) {
                        email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        listEmail.add(email);
                    }
                    emailCursor.close();

                    // On demande s'il y a une image du contact à récupérer
                    if(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)) != null){
                        idImage  = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
                        // Si le lien de l'image est mort aucune image ne sera affichée
                    } else {
                        idImage = idImageParDefaut;
                    }
                }

                listeContacts.add(new ContactAndroid(contact_id, name, listPhoneNumber, listEmail, idImage, idCoche, idColor));
            }
        }
        cursor.close();
        //renvoi en liste de contact android
        return listeContacts;
    }
}
