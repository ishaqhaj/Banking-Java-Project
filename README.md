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


      
      


   
