# SafetyNetAlerts

SafetyNet Alerts est une application back-end destinée à fournir des informations essentielles aux services d'urgence. Elle permet de gérer et d'accéder facilement aux informations sur les personnes, les casernes de pompiers et les dossiers médicaux.

## Caractéristiques

- Gestion des informations sur les résidents, casernes de pompiers et dossiers médicaux en utilisant un fichier JSON comme source de données.
- Recherche et filtrage des données selon divers critères.
- API REST pour une intégration facile avec d'autres systèmes ou applications front-end.
- Manipulation des données de personnes, de casernes de pompiers et de dossiers médicaux directement depuis le fichier JSON.

## Technologies Utilisées

- Spring Boot
- Maven pour la gestion des dépendances et la construction du projet

## Configuration et Installation

Configuration du Fichier JSON
Le fichier JSON utilisé pour la persistance des données doit être placé dans le répertoire src/main/resources. 
Le chemin d'accès au fichier peut être configuré dans le fichier application.properties ou application.yml de votre projet.

### Prérequis

- Java 17
- Spring-boot 3.1.3
- Maven
- Hibernate-validator 8.0.1.Final
- Maven-surefire-report-plugin 3.2.2
- Jacoco-maven-plugin 0.8.7
- Junit 4.13.2

### Instructions d'Installation

1. Cloner le dépôt Git :  git clone (https://github.com/Boubou84/SafetyNetAlerts.git)
   
2. Naviguer dans le dossier du projet via le terminal : cd safetynetalerts

3. Construire le projet avec Maven : mvn clean install

4. Exécuter l'application : mvn spring-boot:run

#### Utilisation

L'application expose une API REST que vous pouvez interroger à l'aide de requêtes HTTP aux différents endpoints.

Exemples d'Endpoints :

GET /personInfo : retourne les informations d'une personne avec le prénom {} et le nom de famille {}.
POST /person : ajoute une nouvelle personne.
PUT person/{oldFirstName}/{oldLastName} : met à jour les informations d'une personne.
DELETE /person/{firstName}/{lastName} : supprime une personne.

Contribution

Les contributions à ce projet sont les bienvenues. Si vous souhaitez contribuer, veuillez suivre les étapes suivantes :

1. **Forker le dépôt** : Créez une copie de ce dépôt dans votre espace GitHub.
2. **Créer une nouvelle branche** : Utilisez la commande `git checkout -b nomDeLaBranche` pour créer une nouvelle branche pour vos modifications.
3. **Commiter vos changements** : Après avoir apporté vos modifications, enregistrez-les avec `git commit -m 'message'`.
4. **Pusher dans la branche** : Envoyez vos modifications vers GitHub avec `git push origin nomDeLaBranche`.
5. **Ouvrir une Pull Request** : Depuis votre fork sur GitHub, ouvrez une Pull Request pour proposer vos changements.

Merci pour votre contribution à l'amélioration de ce projet !
