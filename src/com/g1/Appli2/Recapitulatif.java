package com.g1.Appli2;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class Recapitulatif extends Activity {

    Button confirmation;
    Button annuler;
    ListView nom;
    ListView message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_contact_view);

        //On associe les éléments au layout dans les listView
        nom = (ListView) findViewById(R.id.nom);
        message = (ListView) findViewById(R.id.message);
        confirmation = (Button) findViewById(R.id.confirmation);
        annuler = (Button) findViewById(R.id.annuler);

        /**CONTACTS**/
        /*******************On recupère les contacts envoyés par l'activité 1***********************/

        //   final Intent intent = getIntent();
        final ArrayList<ContactAndroid> listContacts = (ArrayList<ContactAndroid>) getIntent().getSerializableExtra("listeContact");

        /******************************On affiche les contacts(prenom)******************************/
        // Je crée une liste de prénom des contacts sélectionnés
        final ArrayList<String> listePrenom = new ArrayList<>();
        // Je remplis la liste des prénoms avec les prénom des contacts sélectionnés
        for (ContactAndroid contact : listContacts) {
            listePrenom.add(contact.getName());
        }

        // On peut le faire avec un ArrayAdapter de String car c'est juste des String (au lieu de refaire un adapter personnalisé)
        ArrayAdapter<String> adapterContact = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listePrenom);

        /**********************Assignation du contact au bon textView********************************/
        // on assigne l'adapter à notre listView
        nom.setAdapter(adapterContact);

        /**SMS**/
        /******************************On créé un tableau pour les SMS types**********************/

        // Ajout de la liste des messages à la main
        final ArrayList<String> listeMessage = new ArrayList<>();
        // Remplissage du tableau par des messages
        listeMessage.add("Du coté obscure de la force tu iras <prenom>, si en java tu code .");
        listeMessage.add("I will be back <prenom>.");
        listeMessage.add("Vous voulez pas un wisky d'abord <prenom> ?");
        listeMessage.add("Prenez un chewing gum <prenom> !");
        listeMessage.add("Que la force soit avec toi, <prenom>.");
        listeMessage.add("You know nothing <prenom>.");
        listeMessage.add("I'm not in danger <prenom>, I am the danger.");
        listeMessage.add("Un petit baiser, <prenom> ? Et je te ferais voir ma grosse gondole ! Et ma belle tour de Pise ! Et je planterais ma grosse fourchette dans ton ravioli !");
        listeMessage.add("Non <prenom>. Je suis ton père.");
        listeMessage.add("Hasta la vista, <prenom>!");
        listeMessage.add("C'est pas faux <prenom>");
        listeMessage.add("Challenge Accepted <prenom>");
        /************************On met un place une selection unique pour le SMS***************/

        // Utilisation d'un adaptateur de base pour la liste des messages (String)
        // (Le layout "simple_list_item_checked" est présent de base dans android ainsi que son id text1 auquel on va
        // associer notre liste de messages)
        ArrayAdapter<String> adapterMessage = new ArrayAdapter<>(this,
                // On utilise pas de nouveau layout avec textView mais un layout prédéfini par android
                android.R.layout.simple_list_item_checked, android.R.id.text1, listeMessage);

        // Un seul choix est possible
        message.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        /**********************Assignation du message au bon textView********************************/
        // on assigne l'adapter à notre listView
        message.setAdapter(adapterMessage);

        /****************Bouton pour revenir en arriere***********************/

        //mise en place des bouttons pour revenir à l'etape 1
        confirmation = (Button) findViewById(R.id.confirmation);
        annuler = (Button) findViewById(R.id.annuler);

        //on crée un intent pour renvoyer a la premiere activité ou l'activé 3
        final Intent retourIntent = new Intent(this, AfficherListeContacts.class);
        final Intent myIntent = new Intent(this, ConfirmationMessage.class);

        //on dit au bouton d'envoyer un all_contact_view avec envoi avant de revenir à l'activité1

        final ArrayList<String> listeMessageEnvoyes = new ArrayList<>();
        confirmation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String messageSelected = "";
                if (message.getCheckedItemCount() > 0) {
                    // pareil pour le message
                    messageSelected = listeMessage.get(message.getCheckedItemPosition());
                    // si au moins un élément dans le textView nom est checked
                    for (String prenom : listePrenom) {
                        // je définis la string avec la valeur sélectionné
                        // Je récupère l'élement n°position sélectionnée dans la liste des prénoms
                        // Je remplace dans le message la chaine <prenom> par le prénom sélectionné
                        listeMessageEnvoyes.add(messageSelected.replace("<prenom>", prenom));
                    }
                    myIntent.putExtra("listeMessageEnvoyes", listeMessageEnvoyes);
                    myIntent.putExtra("listeContactSelectionnes", listContacts);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(Recapitulatif.this, "Choisissez un modèle de SMS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //on revoie directement à l'activité 1
        annuler.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(retourIntent);
            }
        });
    }
}
