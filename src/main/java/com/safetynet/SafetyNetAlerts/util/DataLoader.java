package com.safetynet.SafetyNetAlerts.util;

import jakarta.annotation.PostConstruct;

public class DataLoader {
    @PostConstruct // Cette annotation assure que cette méthode est exécutée au démarrage de l'application
    public void loadData() {
        // Lire le fichier JSON depuis 'src/main/resources'
        // Peupler la base de données
    }
}
