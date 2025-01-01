Point d'exécution de l'application

   Pour démarrer l'application, veuillez exécuter le fichier App.java situé dans : com.Application de src.main.java.
   Structure du Projet et Processus d'Inscription : 

Partie de l'inscription :
   Pour s'inscrire à l'application, l'utilisateur doit fournir les informations nécessaires conformément à la norme ISO 20022 :
   
   Nom :
   La taille maximale est de 144 caractères.
   La taille minimale est de 2 caractères.
   
   IBAN :
   Fournir un IBAN valide.
   
   Email :
   Fournir un email valide.
   
   Mot de passe :
   Fournir un mot de passe ainsi qu'une confirmation du mot de passe.
   
   Banque :
   Sélectionner une banque et indiquer son BIC (les informations doivent être compatibles avec celles de la base de données).
   
   Pièce d'identité :
   Fournir une pièce d'identité unique, telle que :
   Le numéro de la carte nationale d'identité.
   Ou l'identifiant du passeport.
   
   Autres champs :
   Les autres champs sont facultatifs.
   
   Un identifiant unique sera automatiquement attribué à chaque utilisateur lors de son inscription.
   
   Finalement, le reçu d'inscription sera envoyé par email à l'utilisateur.

2ème partie : L'authentification

L'utilisateur pourra se connecter à l'application à l'aide de son identifiant unique, envoyé par email, et du mot de passe confirmé lors de l'inscription.

Interface d'accueil

L'interface d'accueil permet à l'utilisateur de consulter les 10 derniers virements effectués.  

Fonctionnalités principales :
   1. **Affichage des virements récents** :  
      - Les virements sont listés en ordre décroissant (du plus récent au plus ancien).  
      - Les détails affichés pour chaque virement incluent :  
        - **Date** de la transaction.  
        - **Bénéficiaire**.  
        - **Montant**.  
        - **Type de virement**.  
        - **Motif** de la transaction.  

   2. **Navigation vers l'historique** :  
      - L'utilisateur peut revenir à tout moment à cette interface en cliquant sur le bouton **Historique**.  

Cette interface offre une vue rapide et détaillée des transactions les plus récentes pour une gestion simplifiée et efficace.  


fonctionnalités :

Ajout de compte :

   Implémentation de la fonctionnalité permettant à un utilisateur d'ajouter un compte bancaire avec un IBAN valide, conforme à la norme ISO 20022.
   Cette amélioration permet désormais à l'utilisateur d'effectuer des transactions à partir du compte ajouté.
   Validation complète des IBANs pour garantir la conformité avec les règles du format international.
   Ajout de tests unitaires pour vérifier la validité des IBANs et le bon fonctionnement de la fonctionnalité.

Ajout de bénéficiaire : 

   Cette fonctionnalité permet d'enregistrer un bénéficiaire afin de pouvoir lui effectuer des transactions.
   Pour ajouter un bénéficiaire, l'utilisateur doit renseigner les informations suivantes :
      -Le nom complet du bénéficiaire.
      -Son IBAN.

Passation de virements simples

   Cette fonctionnalité permet à l'utilisateur d'effectuer un virement simple avec les options suivantes :  
   
   1. **Sélection de l'IBAN** :  
      - L'utilisateur peut spécifier son propre IBAN s'il en possède plusieurs.
   
   2. **Choix du bénéficiaire** :  
      - Recherche d'un bénéficiaire à l'aide de son IBAN.  
      - Sélection d'un bénéficiaire déjà ajouté dans la liste des bénéficiaires.
   
   3. **Spécification des détails de la transaction** :  
      - **Montant** : Doit être spécifié et est obligatoire.  
      - **Devise** : Doit être spécifiée pour la transaction.  
      - **Motif du virement** : Optionnel, mais peut être précisé.  
      - **Méthode de paiement** : Optionnelle, à choisir selon les préférences.  
   
   Cette fonctionnalité garantit que toutes les transactions respectent les exigences obligatoires (montant et devise) et offrent une flexibilité avec des options supplémentaires            (motif et méthode de paiement). 

   
Virement Multiple

   La fonctionnalité de **virement multiple** permet à l'utilisateur d'effectuer jusqu'à trois virements en une seule opération, tout en respectant la norme **ISO 20022**.  
   
   ### Étapes et fonctionnalités principales :
   1. **Sélection du compte de l'utilisateur** :  
      - Si l'utilisateur possède plusieurs comptes, il doit commencer par sélectionner le compte à utiliser pour les virements.
   
   2. **Spécification des détails du virement** :  
      Pour chaque virement, l'utilisateur doit obligatoirement fournir :  
      - **Bénéficiaire** :  
        - Sélection d'un bénéficiaire déjà enregistré.  
        - Ajout d'un nouveau bénéficiaire en recherchant par IBAN.  
      - **Montant** de la transaction.  
      - **Devise** utilisée.  
   
      **Options supplémentaires** :  
      - **Date d'exécution** du virement.  
      - **Motif** de la transaction.  
      - **Méthode de virement** (conformément à la spécification ISO 20022).  
   
   3. **Génération du fichier XML** :  
      - À la fin du processus, un fichier XML conforme à la norme **ISO 20022** est généré.  
      - Le fichier est automatiquement enregistré dans le dossier suivant :  
        ```
        Transaction_XML/VirementMultiple
        ```  
   
   Cette fonctionnalité offre une gestion efficace des virements multiples tout en assurant la compatibilité avec les exigences de la norme ISO 20022.  

Virement de Masse

   La fonctionnalité de virement de masse permet à l'utilisateur d'effectuer jusqu'à 5000 virements en une seule opération, tout en respectant la norme ISO 20022.
   
   Détails :
   
      Bénéficiaires :
      
      L'utilisateur peut sélectionner un bénéficiaire existant ou effectuer une recherche simple par IBAN pour en ajouter un nouveau.
      
      Informations Obligatoires :
      
      Bénéficiaire pour chaque virement.
      Montant de la transaction.
      Devise utilisée.
      
      Options Supplémentaires :
      
      Spécification d'une date exécutive pour chaque virement.
      Choix de la méthode de paiement adaptée.

   
   Cette fonctionnalité offre une grande flexibilité et optimise la gestion des transactions à grande échelle pour répondre aux besoins des utilisateurs professionnels et 
   particuliers.
   
   Fichiers générés :
   Chaque virement de masse génère automatiquement un fichier XML conforme à la norme ISO 20022, enregistré dans le répertoire suivant :
   Transaction_XML/VirementDeMasse
   
Test avec JUnit

   ### Description
   Achèvement de tous les tests liés à la base de données de l'application avec succès. Les tests incluent :
   
   - Ajout d'un utilisateur.
   - Ajout d'un compte.
   - Ajout d'un bénéficiaire.
   - Obtention de tous les bénéficiaires.
   - Obtention des virements récents.
   - Ajout de virements.
   - Vérification de la banque.
   
   Ces tests garantissent la fiabilité et la cohérence des fonctionnalités principales de l'application.

#### Premier Test avec SonarQube : Analyse de la Qualité et du Taux de Couverture de Code

      Suite au premier test de qualité avec SonarQube, voici une interprétation des résultats présentés dans l'image :
      
      ![FirstAnalysisSonarQube](https://github.com/user-attachments/assets/fcbde8c6-4f20-488e-9cbe-51ec09707b0e)

      Résultats Identifiés :
      
          Erreurs de Sécurité :

               ![Capture d’écran du 2024-12-29 10-23-43](https://github.com/user-attachments/assets/74fb0162-2f5f-4857-bf91-af48786c003d)
               ![Capture d’écran du 2024-12-29 10-24-04](https://github.com/user-attachments/assets/10ebc7a0-d7b5-4f70-84c4-db2e173d04d9)

          
              Stockage non sécurisé de l'identifiant de SonarQube.
              Le compte "root" de la base de données n'a pas de mot de passe défini.
              Le mot de passe de l'application email est directement exposé dans le code.
      
          Erreurs de Fiabilité (Reliability) :
          ![Capture d’écran du 2024-12-29 14-31-31](https://github.com/user-attachments/assets/685215c3-d3bd-4b3d-a964-860560b013c3)

              16 erreurs détectées, principalement dues à des problèmes de gestion des ressources :

              ![Capture d’écran du 2024-12-29 10-37-11](https://github.com/user-attachments/assets/9425a970-37f0-4b10-a5d7-3d50e2290951)


             Ces erreurs sont liées à l'utilisation incorrecte de PreparedStatement dans les méthodes DAO du projet, entraînant une gestion inadéquate des ressources                       (connexion, requêtes, etc.).
      
          Erreurs de Maintenabilité :
          
             ![Capture d’écran du 2024-12-29 14-31-31](https://github.com/user-attachments/assets/fd02c087-bb60-406e-b471-43b3ac78a96f)
             ![Capture d’écran du 2024-12-29 14-42-43](https://github.com/user-attachments/assets/1fb1443f-6e70-41ad-8ad0-baefb0b8be13)
             ![Capture d’écran du 2024-12-29 17-59-01](https://github.com/user-attachments/assets/5e86cdbc-4e44-44b1-81f2-d570a1749c8f)
             ![Capture d’écran du 2024-12-29 22-28-10](https://github.com/user-attachments/assets/7c4a2063-03f0-4858-b4cd-04ad886284d9)
             ![Capture d’écran du 2025-01-01 00-27-28](https://github.com/user-attachments/assets/b798e054-e58e-424b-8355-bc2b2b4e3955)
             ![Capture d’écran du 2024-12-31 08-31-37](https://github.com/user-attachments/assets/c14466ba-dad6-4592-9e45-fda8761c028d)
             ![Capture d’écran du 2024-12-30 20-39-54](https://github.com/user-attachments/assets/ee12c73e-8acb-428c-a453-5d7e0711e4d3)


         

              220 erreurs détectées, liées à plusieurs aspects :
                  problème de nommage.
                    Le package Application était écrit en majuscule, ce qui va à l'encontre des conventions de nommage.
                  Problèmes de duplication de code.
                  Mauvais choix de noms de variables.
                  Importations non utilisées laissées dans le code.
                  Utilisation de System.out ou System.err au lieu de Logger, qui est recommandé pour une meilleure gestion des logs.
                  Modificateurs mal utilisés ou inappropriés.
      
          Taux de Couverture des Tests :
              Le taux de couverture des tests de code était nul, bien que toutes les méthodes DAO du projet aient été testées. Cela suggère que les tests existants ne sont                  pas correctement détectés ou configurés pour être pris en compte dans le calcul de la couverture de code par SonarQube.
      
      Actions Correctives Apportées :
      
          Suppression des imports inutilisés.
          Adoption des bonnes pratiques de nommage pour les packages et les variables.
          Utilisation de Logger pour remplacer les appels à System.out ou System.err.
          Révision et modification des modificateurs pour respecter les conventions de codage et améliorer la maintenabilité.
          Investigation en cours pour résoudre le problème de la couverture des tests de code.
      
      
            
      


   
