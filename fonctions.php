<?php

/**
 * Classe d'accès aux données.
 * 
 * PHP version 7.4.13

 * <p>Ce fichier est appelé par l'application Android permettant à un visiteur médical
 * de persister les données saisies sur sa STA nomade, dans la base de données
 * de l'entreprise GSB.</p>
 * 
 * <p>Afin d'exécuter des requêts SQL et de renvoyer les résultats vers l'application
 * Android un objet de type PDO est initialisé en début de fichier et sera utilisé
 * pour chaque requête.</p>
 * 
 * <p>Différents opérations sont exécutées ici :</p>
 * <ul>
 *  <li>Authentification de l'utilisateur</li>
 *  <li>Vérification de l'existance d'une fiche de frais pour un mois donné</li>
 *  <li>Création d'une fiche de frais</li>
 *  <li>Enregistrement d'un nouveau frais (forfaitisé et / ou hors forfait)</li>
 *  <li>Mise à jour de frais existants en y ajoutant les frais saisis sur mobile</li>
 * </ul>
 * 
 * @author  Florian MARTIN
 * @package GSB
 * @link    http://www.php.net/manual/fr/book.pdo.php PHP Data Objects sur php.net
 */


include "connexion_PDO.php";

// Contrôle de réception de paramètres
// $operation correspond au type d'opération demandé par l'application:
// soit "connexion" pour authentifier l'utilisateur, soit "transfert" pour 
// transférer les données saisies sur l'application mobile vers la base distante.
$operation = filter_input(INPUT_POST, "operation", FILTER_SANITIZE_STRING);
$mPDO = connexionPDO();

if ($operation) {
    // Authentification
    if ($operation == "connexion") {
        try {
            print "Connexion%";
            logIn($mPDO);
        } catch (PDOException $ex) {
            print "Erreur%" . $ex->getMessage();
        }
    }

    // Transfert des données enregistrées localement vers la base distante
    elseif ($operation == "transfert") {
        try {
            print "Transfert%";

            // Récupération et parsing du JSON envoyé en POST contenant la liste
            // des frais (forfaitisés et hors forfait) saisis par l'utilisateur 
            $lesDonnees = filter_input(INPUT_POST, "listeFrais");
            $idVisiteur = filter_input(INPUT_POST, "id", FILTER_SANITIZE_STRING);
            $lesFrais = json_decode($lesDonnees, true);
            $tabFraisTraites = [];
            
            foreach ($lesFrais as $frais) {
                
                $unFrais = [
                    "annee" => $frais['annee'],
                    "mois" => $frais['mois'],
                    "etape" => $frais['etape'],
                    "km" => $frais['km'],
                    "nuitee" => $frais['nuitee'],
                    "repas" => $frais['repas'],
                    "fraisHf" => $frais['fraisHf']
                ];

                // Ajout d'un "0" devant les mois inférieurs à 10
                // afin de garder le format MM et non M
                if ($unFrais['mois'] < 10) {
                    $unFrais['mois'] = "0" . $unFrais['mois'];
                }

                $dateFrais = $unFrais['annee'] . $unFrais['mois'];
                //print (" DateFrais = " . $dateFrais . " Différence en mois = " . diffDates($dateFrais) . " mois.");
                
                // On enregistre les frais hors forfait datant d'un an maximum.
                // Pour les frais forfaitisés seul le mois courant est permis
                if (diffDates($dateFrais) < 12) {
                  
                    // Mois sous la forme AAAAMM
                    $moisCourant = date('Ym');
                    
                    // S'il n'existe pas déjà une fiche de frais pour ce mois,
                    // on en créée une
                    // print(" Date = " . $dateFrais . " 1erFraisMois ? = " . estPremierFraisMois($mPDO, $idVisiteur, $dateFrais)); 
                    if (estPremierFraisMois($mPDO, $idVisiteur, $moisCourant)) {
                        // Création d'une fiche de frais pour le mois courant
                        creeFicheFrais($mPDO, $idVisiteur, $moisCourant);

                        //print (" idvisiteur = " . $idVisiteur . " dateFrais = " . $dateFrais);
                        // Récupération de chaque type de frais forfaitisé
                        $lesIdFrais = getIdFrais($mPDO);

                        // Insertion de la quantité de chaque type de frais forfaitisé
                        foreach ($lesIdFrais as $idFrais) {
                            $qteFrais;
                            switch ($idFrais['id']) {
                            case 'ETP':
                                $qteFrais = $unFrais['etape'];
                                break;
                            case 'KM':
                                $qteFrais = $unFrais['km'];
                                break;
                            case 'NUI':
                                $qteFrais = $unFrais['nuitee'];
                                break;
                            case 'REP':
                                $qteFrais = $unFrais['repas'];
                                break;
                            default:
                            }
                            nouvelleLigneFraisForfaitise($mPDO, $idVisiteur,
                                    $moisCourant, $idFrais, $qteFrais);
                        }
                    } else {
                        // Mise à jour des frais si la fiche n'a pas encore été clôturée
                        $ficheFrais = getFicheFrais($mPDO, $idVisiteur, $moisCourant);
                        
                        if ($ficheFrais['idetat'] == "CR") {
                            // Récupération des frais existants pour ce mois
                            $lesTypesDeFrais = getFraisMois($mPDO, $idVisiteur,
                                    $moisCourant);

                            // Mise à jour des frais existants
                            foreach ($lesTypesDeFrais as $unTypeDeFrais) {
                                switch ($unTypeDeFrais['idfraisforfait']) {
                                case 'ETP':
                                    $qteFrais = $unFrais['etape'] 
                                                + $unTypeDeFrais['quantite'];
                                    break;
                                case 'KM':
                                    $qteFrais = $unFrais['km'] 
                                                + $unTypeDeFrais['quantite'];
                                    break;
                                case 'NUI':
                                    $qteFrais = $unFrais['nuitee'] 
                                                + $unTypeDeFrais['quantite'];
                                    break;
                                case 'REP':
                                    $qteFrais = $unFrais['repas'] 
                                                + $unTypeDeFrais['quantite'];
                                    break;
                                default:
                                }
                                //print ("date = " . $dateFrais . " type = " . $unTypeDeFrais['idfraisforfait'] . "qte = " . $qteFrais);
                                updateLigneFraisForfaitise($mPDO, $idVisiteur,
                                                            $moisCourant, 
                                                            $unTypeDeFrais, 
                                                            $qteFrais);
                            }
                        }
                    }                  
                    // Insertion des frais hors forfait
                    foreach ($unFrais['fraisHf'] as $unFraisHf) {
                        $dateHf = modifDate($dateFrais, "hf", $unFraisHf['jour']);
                        //print ("Jour = " . $unFraisHf['jour'] . " / Motif = " . $unFraisHf['motif'] . " / Montant = " . $unFraisHf['montant']);
                        nouvelleLigneHf($mPDO, $idVisiteur, $moisCourant, $unFraisHf,
                                        $dateHf);
                    }
                }
                $tabFraisTraites[] = $dateFrais;
            }
            print json_encode($tabFraisTraites);
        } catch (PDOException $ex) {
            print "Erreur%" . $ex->getMessage() . "%" . json_encode($tabFraisTraites);
        }
    }
}

/**
 * Teste s'il existe déjà une fiche de frais pour ce mois
 * 
 * @param PDO    $mPDO          Objet permettant la connexion à une base de données
 * @param String $idVisiteur    ID du visiteur
 * @param String $dateFrais     Mois sous la forme aaaamm
 *    
 * @return boolean              Retourne vrai si c'est le premier frais du mois
 */
function estPremierFraisMois($mPDO, $idVisiteur, $dateFrais): bool 
{
    //print ("idvisi = " . $idVisiteur . " date = " . $dateFrais);
    
    $boolReturn = true;
    $ficheFrais = getFicheFrais($mPDO, $idVisiteur, $dateFrais);

    if ($ficheFrais) {
        $boolReturn = false;
    }
    
    return $boolReturn;
}

/**
 * Récupère une fiche de frais pour un mois donné
 * 
 * @param PDO    $mPDO          Objet permettant la connexion à une base de données
 * @param String $idVisiteur    ID du visiteur
 * @param String $dateFrais     Mois sous la forme aaaamm
 */
function getFicheFrais($mPDO, $idVisiteur, $dateFrais) {
    $requete = $mPDO->prepare(
            'SELECT * '
            . 'FROM fichefrais '
            . 'WHERE idvisiteur = :idVisiteur '
            . 'AND mois = :dateFrais'
    );
    $requete->bindParam(':idVisiteur', $idVisiteur, PDO::PARAM_STR);
    $requete->bindParam(':dateFrais', $dateFrais, PDO::PARAM_STR);
    $requete->execute();
    return $requete->fetch();
}


/**
 * Récupère les frais forfaitisés déjà enregistrés en base distante pour le mois donné
 * 
 * @param PDO    $mPDO          Objet permettant la connexion à une base de données
 * @param String $idVisiteur    ID du visiteur
 * @param String $dateFrais     Mois sous la forme aaaamm
 * 
 * @return Array                Retourne tous les frais forfaitisés du visiteur pour
 *                              un mois donné sous forme de tableau associatif 
 */
function getFraisMois($mPDO, $idVisiteur, $dateFrais): array 
{
    $requete = $mPDO->prepare(
            "SELECT * FROM lignefraisforfait "
            . "WHERE idvisiteur = :idVisiteur "
            . "AND mois = :dateFrais "
    );
    $requete->bindParam(':idVisiteur', $idVisiteur, PDO::PARAM_STR);
    $requete->bindParam(':dateFrais', $dateFrais, PDO::PARAM_STR);
    $requete->execute();
    return $requete->fetchAll();
}

/**
 * Met à jour les quantités de frais forfaitisés déjà saisis pour le mois
 * 
 * @param PDO       $mPDO               Objet permettant la connexion à une base de données
 * @param String    $idVisiteur         ID du visiteur
 * @param String    $dateFrais          Mois sous la forme aaaamm
 * @param Array     $unTypeDeFrais      Un tableau associatif représentant une ligne
 *                                      de frais forfaitisé 
 * @param Integer   $qteFrais           La quantité de frais mise à jour qui vient
 *                                      remplacer la quantité préalablement enregistrée
 */
function updateLigneFraisForfaitise($mPDO, $idVisiteur, $dateFrais, $unTypeDeFrais, 
                                    $qteFrais) 
{
    $requete = $mPDO->prepare(
            "UPDATE lignefraisforfait "
            . "SET quantite = :qteFrais "
            . "WHERE idvisiteur = :idVisiteur "
            . "AND mois = :dateFrais "
            . "AND idfraisforfait = :idFrais"
    );

    //print $idVisiteur . "-" . $dateFrais . "-" . $unTypeDeFrais['idfraisforfait'] . "-" . $qteFrais;

    $requete->bindParam(':qteFrais', $qteFrais, PDO::PARAM_INT);
    $requete->bindParam(':idVisiteur', $idVisiteur, PDO::PARAM_STR);
    $requete->bindParam(':dateFrais', $dateFrais, PDO::PARAM_STR);
    $requete->bindParam(':idFrais', $unTypeDeFrais['idfraisforfait'], PDO::PARAM_STR);
    $requete->execute();
}

/**
 * Création d'une nouvelle fiche de frais
 * 
 * @param PDO    $mPDO          Objet permettant la connexion à une base de données
 * @param String $idVisiteur    ID du visiteur
 * @param String $dateFrais     Mois sous la forme aaaamm
 */
function creeFicheFrais($mPDO, $idVisiteur, $dateFrais) 
{
    $requete = $mPDO->prepare(
            "INSERT into fichefrais "
            . "VALUES (:idVisiteur, :dateFrais, 0, 0, now(), 'CR')"
    );
    $requete->bindParam(':idVisiteur', $idVisiteur, PDO::PARAM_STR);
    $requete->bindParam(':dateFrais', $dateFrais, PDO::PARAM_STR);
    $requete->execute();
}

/**
 * Récupère tous les ID de frais forfaitisés depuis la table Fraisforfait
 * 
 * @param PDO $mPDO Objet permettant la connexion à une base de données
 * 
 * @return          Un tableau associatif contenant tous les ID de frais forfaitisés
 */
function getIdFrais($mPDO) 
{
    $requete = $mPDO->prepare(
            'SELECT id FROM fraisforfait'
    );
    $requete->execute();
    return $requete->fetchAll();
}

/**
 * Créée une nouvelle ligne de frais forfaitisé
 * 
 * @param PDO       $mPDO       Objet permettant la connexion à une base de données
 * @param String    $idVisiteur ID du visiteur
 * @param String    $dateFrais  Mois sous la forme aaaamm
 * @param Array     $idFrais    Un tableau associatif contenant les informations 
 *                              relatives à un type de frais   
 * @param Integer   $qteFrais   La quantité du frais en question
 */
function nouvelleLigneFraisForfaitise($mPDO, $idVisiteur, $dateFrais, $idFrais, $qteFrais) 
{
    $requete = $mPDO->prepare(
            "INSERT into lignefraisforfait(idvisiteur,mois,"
            . "idfraisforfait,quantite) "
            . "VALUES (:idVisiteur, :mois, :idFrais, :quantite)"
    );
    //print $idVisiteur . "-" . $dateFrais . "-" . $idFrais['id'] . "-" . $qteFrais;
    $requete->bindParam(':idVisiteur', $idVisiteur, PDO::PARAM_STR);
    $requete->bindParam(':mois', $dateFrais, PDO::PARAM_STR);
    $requete->bindParam(':idFrais', $idFrais['id'], PDO::PARAM_STR);
    $requete->bindParam(':quantite', $qteFrais, PDO::PARAM_INT);
    $requete->execute();
}

/**
 * Créée une nouvelle ligne de frais hors forfait
 * 
 * @param PDO       $mPDO       Objet permettant la connexion à une base de données
 * @param String    $idVisiteur ID du visiteur
 * @param String    $dateFrais  Mois sous la forme aaaamm
 * @param Array     $unFraisHf  Tableau associatif représentant un frais hors forfait
 * @param String    $dateHf     La date du frais mise au format AAAA-MM-JJ
 */
function nouvelleLigneHf($mPDO, $idVisiteur, $dateFrais, $unFraisHf, $dateHf) 
{
    $requete = $mPDO->prepare(
            "INSERT into lignefraishorsforfait "
            . "VALUES (null, :idVisiteur, :dateFrais, :motif, :dateEng, :montant)"
    );
    $requete->bindParam(':idVisiteur', $idVisiteur, PDO::PARAM_STR);
    $requete->bindParam(':dateFrais', $dateFrais, PDO::PARAM_STR);
    $requete->bindParam(':motif', $unFraisHf['motif'], PDO::PARAM_STR);
    $requete->bindParam(':dateEng', $dateHf, PDO::PARAM_STR);
    $requete->bindParam(':montant', $unFraisHf['montant'], PDO::PARAM_STR);
    $requete->execute();
}

/**
 * Calcule l'intervalle entre 2 dates.
 * 
 * Le calcul soustrait le mois suivant celui passé en paramètre à la date d'aujourd'hui
 * grâce à la fonction <code>diff()</code> de la classe DateTime
 * d'où le test "< 12" lors de l'appel de la fonction pour contrer le fait que le 
 * mois retranché est le suivant du mois passé en paramètre.
 * 
 * @param  String    $dateFrais  Mois sous la forme aaaamm
 * 
 * @return Integer              Le nombre de mois séparant les 2 dates
 */
function diffDates($dateFrais): int 
{
    $dateAuj = date('Ym');
    $date = modifDate($dateFrais, "", "");

    $d1 = new DateTime($dateAuj);
    $d2 = new DateTime($date);

    $intervalle = $d2->diff($d1);
    $diffMois = $intervalle->y * 12 + $intervalle->m;
    
    // La propriété "invert" d'un objet DateTime = 0 lorsque la date qui est soustraite
    // est inférieure à la date de référence, sinon = 1.
    if ($intervalle->invert == 0){
        return $diffMois;
    }
    else{
        return $diffMois * 100;
    }
}

/**
 * Modifie le format d'une date
 * 
 * Pour un frais forfaitisé la date sera formatée de la manière AAAA-MM
 * Pour un frais hors forfait le format de sortie sera AAAA-MM-JJ
 * 
 * @param String  $dateFrais    La date dont le format doit être modifié
 * @param String  $typeFrais    Vide pour un frais forfaitisé, "hf" pour un hors forfait
 * @param Integer $jour         Le jour du mois d'un frais hors forfait
 * 
 * @return string               la date dont le format a été modifié 
 */
function modifDate($dateFrais, $typeFrais, $jour): string 
{
    $date = substr_replace($dateFrais, "-", 4, 0);
    if ($typeFrais == "hf") {
        $date = substr_replace($date, "-" . $jour, 7, 0);
    }
    return $date;
}

/**
 * Authentification du visiteur médical
 * 
 * @param PDO $mPDO Objet permettant la connexion à une base de données
 */
function login($mPDO) 
{
    // Récupération de l'identifiant et mot de passe envoyés en POST
    $lesDonnees = filter_input(INPUT_POST, "data", FILTER_SANITIZE_STRING);

    // $infosLogin[0] contient le nom d'utilisateur
    // $infosLogin[1] contient le password
    $infosLogin = explode("%", $lesDonnees);

    // Récupération depuis la base distante de l'id et du password
    // correspondant à l'identifiant envoyé par l'application
    $requete = $mPDO->prepare(
            'SELECT id, mdp FROM visiteur'
            . ' WHERE login = :login'
    );
    $requete->bindParam(':login', $infosLogin[0], PDO::PARAM_STR);
    $requete->execute();
    $resultatReq = $requete->fetch();

    if ($infosLogin[1] == $resultatReq['mdp']) {
        $array = [
            'isLogInValid' => "true",
            'idVisiteur' => $resultatReq['id']
        ];
    } else {
        $array = ['isLogInValid' => "false"];
    }

    // Renvoi du résultat du login vers l'application Android
    // Un tableau associatif est utilisé pour renvoyer le résultat du login ainsi
    // que l'ID du visiteur afin de stocker cette information dans un souci de 
    // réutilisation pour le transfert des données
    print (json_encode($array));
}
?>