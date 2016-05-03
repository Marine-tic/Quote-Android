package com.g1.Appli2;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class ConfirmationMessage extends Activity {
    ListView message;
    Button envoi;
    Button modifier;
    //EditText editText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);

        //On associe les éléments au layout dans les listView
        message = (ListView) findViewById(R.id.message);


        /*******************On recupère les messages envoyés par l'activité 1***********************/

        //   final Intent intent = getIntent();
        final ArrayList<String> listeMessages =
                (ArrayList<String>) getIntent().getSerializableExtra("listeMessageEnvoyes");
        final ArrayList<ContactAndroid> listeContactSelectionnes =
                (ArrayList<ContactAndroid>) getIntent().getSerializableExtra("listeContactSelectionnes");

        /******************************On affiche les contacts(prenom)******************************/
        final ArrayAdapter<String> adapterMessage = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked,
                android.R.id.text1, listeMessages);
        message.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        message.setAdapter(adapterMessage);


        /******************************Bouton pour confirmer ou revenir en arriere*******************************/
        //mise en place des bouttons 
        envoi = (Button) findViewById(R.id.envoi);
        modifier = (Button) findViewById(R.id.modifier);

        //on crée un intent pour renvoyer a la premiere activité
        final Intent intent = new Intent(this, AfficherListeContacts.class);

        /*************boutton pour envoyer les sms******************/
        envoi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArrayList<String> listePhoneNumber = new ArrayList<>();
                for (ContactAndroid contact : listeContactSelectionnes) {
                    //le get 0 me sert à recupérer que le premier numéro, on considère que c'est celui par défaut
                    listePhoneNumber.add(contact.getListPhoneNumber().get(0));
                }
                // On considère que le tableau de message et de contact font la même taille et ordonné pareille donc il
                // suffit d'en parcourir qu'un seul et récupérer les deux élement avec qu'un seul indice.
                for (int i = 0; i < listePhoneNumber.size(); i++) {
                    sendSMS(listePhoneNumber.get(i), listeMessages.get(i));
                }
                Toast.makeText(getApplicationContext(), "Vos SMS sont envoyés", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });


        /*************boutton pour modifier le message (prenom) selectionné avant l'envoi******************/

        modifier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (message.getCheckedItemCount() > 0) {

                    /**********************Mise en place de la boite de dialogue pour modifier le prenom et de ces boutons******************************/
                    //le layout en tant que View
                    LayoutInflater inflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
                    final View formElementview = inflater.inflate(R.layout.alert_dialog, null, false);




                    //LayoutInflater layout = LayoutInflater.from(ConfirmationMessage.this);
                    final EditText editText = (EditText) formElementview.findViewById(R.id.modification);

                    new AlertDialog.Builder(ConfirmationMessage.this).setView(formElementview)
                            .setTitle("Modifier le prénom du sms : ")
                            .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    // Si on selectionne un message à modifier
                                    if(message.getCheckedItemCount() > 0){
                                        // On récupère le nom correcpondant au message selectionné dans la liste de noms selectionnés de base(activité2)
                                        String nomContactCourant  = listeContactSelectionnes.get(message.getCheckedItemPosition()).getName();
                                        // On permet de saisir un nouveau texte
                                        String newPrenom = editText.getText().toString();
                                        // On modifie le nom du contact selectionné directement dans la liste des contacts selectionné de base(activité2)
                                        // De cette manière on va pouvoir modifier autant de fois que souhaité le nom
                                        // Autrement la correspondance entre le tableau des messages et le tableau des noms ne correpondrait plus
                                        listeMessages.set(message.getCheckedItemPosition(),
                                                listeMessages.get(message.getCheckedItemPosition()).replace(
                                                        nomContactCourant,
                                                        newPrenom));
                                        ContactAndroid contactAndroid = listeContactSelectionnes.get(message.getCheckedItemPosition());
                                        contactAndroid.setName(newPrenom);
                                        listeContactSelectionnes.set(message.getCheckedItemPosition(),
                                                contactAndroid);

                                        // Pour raffraichir la liste et bien prendre en compte les modifications ( certaines versions le font automatiquement pas mais
                                        // si vieux modèle de téléphone)
                                        message.setAdapter(adapterMessage);
                                        // Fermeture du popup
                                        dialogInterface.cancel();
                                    } else {
                                        // On traite le cas si aucun contact n'est selectionner
                                        Toast.makeText(getApplicationContext(), "Aucun message n'a été sélectionné", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    dialogInterface.cancel();
                                }
                            }).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Aucun modèle de SMS n'a été sélectionné", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
}
