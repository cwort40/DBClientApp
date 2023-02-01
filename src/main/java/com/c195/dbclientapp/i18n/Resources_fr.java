package com.c195.dbclientapp.i18n;

import java.util.ListResourceBundle;

/**
 * A class representing a resource bundle for French language.
 * This class extends the {@link ListResourceBundle} abstract class and
 * provides an array of key-value pairs that maps keys to their translations in the French language.
 */
public class Resources_fr extends ListResourceBundle {
    /**
     * Returns an array of key-value pairs that maps keys to their translations.
     * @return an array of key-value pairs that maps keys to their translations
     */
    protected Object[][] getContents() {
        return new Object[][]{
                {"Login", "Connexion"},
                {"Username", "Nom d'utilisateur"},
                {"Password", "Mot de passe"},
                {"Location", "Emplacement"},
                {"Submit", "Connexion terminée"},
                {"Exit", "Sortir"},
                {"Error", "Erreur"},
                {"Invalid username or password", "Nom d'utilisateur ou mot de passe invalide"},
                {"An error occurred while trying to validate the user",
                        "Une erreur s'est produite lors de la tentative de validation de l'utilisateur"},
                {"No Upcoming Appointments", "Aucun rendez-vous à venir"},
                {"Upcoming Appointment", "Rendez-vous à venir"}
        };
    }
}
