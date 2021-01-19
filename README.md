# Saisie de frais de visiteurs médicaux - Contexte GSB - Android

### Objectifs:

* Cette application Android a pour vocation la consultation et la saisie de frais forfaitisés et hors forfait par les visiteurs médicaux du groupe GSB.

### Technologie utilisées:

| ![Logo Android studio](https://upload.wikimedia.org/wikipedia/commons/thumb/3/34/Android_Studio_icon.svg/64px-Android_Studio_icon.svg.png) | ![Logo PHP](https://upload.wikimedia.org/wikipedia/commons/thumb/2/27/PHP-logo.svg/64px-PHP-logo.svg.png) | [![Git-logo](https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Git-logo.svg/128px-Git-logo.svg.png)](https://commons.wikimedia.org/wiki/File:Git-logo.svg "Jason Long [CC BY 3.0 (https://creativecommons.org/licenses/by/3.0)], via Wikimedia Commons") | [![Octicons-mark-github](https://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Octicons-mark-github.svg/64px-Octicons-mark-github.svg.png)](https://commons.wikimedia.org/wiki/File:Octicons-mark-github.svg "GitHub [MIT (http://opensource.org/licenses/mit-license.php)], via Wikimedia Commons") | ![Logo MySQL](https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Cib-mysql_%28CoreUI_Icons_v1.0.0%29.svg/64px-Cib-mysql_%28CoreUI_Icons_v1.0.0%29.svg.png)
| ----- | ----- | ----- | ----- | ----- |

  - **Android studio** :  Environnement de développement intégré (IDE)
  - **Java** : Langage de programmation objet - Logo non présent car licence requise - 
  - **PHP** : Langage de programmation s'exécutant côté serveur pour permettre la communication entre l'application et la base de données
  - **Git** : Logiciel de gestion de version
  - **GitHub** : Forge logicielle en ligne utilisant Git
  - **MySQL** : Système de Gestion de Base de Données Relationnelles exploitant le langage SQL pour effectuer des requêtes afin d'insérer, extraire, modifier ou supprimer des données provenant d'une base de données.
  
### Structure:
 
 * L'application fonctionne selon le design pattern MVC (Modèle-Vue-Contrôleur). Les vues, après interaction des utilisateurs, demandent au contrôleur la mise à jour  des éléments graphiques après avoir préalablement demandé la mise à jour des objets métier.
 
### Fonctionnement:
 
* Au démarrage de l'application, l'utilisateur (via un menu composé de plusieurs boutons avec icône représentant le type de frais) a la possibilité de saisir des frais forfaitisés ou hors forfait, ainsi que de consulter les frais qu'il a déjà préalablement saisis pour un mois et une année (forfaitisés) et pour un jour précis du mois d'une année pour les frais hors forfait.
 
* La saisie, et l'affichage des frais préalablement saisis, se font sur la même activity pour les frais forfaitisés, et sur 2 "activities"
distinctes pour les frais hors forfait.
Pour les frais hors forfait l'affichage des données saisies se fait via une ListView déroulante avec pour information la date, le montant
et le motif. Il est aussi possible depuis cet affichage de supprimer un frais via un bouton relié à chaque ligne de la liste.

* Le choix de la date pour se fait via un objet graphique de type DatePicker (link image si possible) peu importe le type de frais.

### Persistance des données:

* La persistance des données est assurée via sérialisation, il est aussi possible de transférer les données sur une base distante via un simple clic sur un bouton de l'activity principale.




 
