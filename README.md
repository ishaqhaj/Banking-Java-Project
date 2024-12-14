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
      
      


   
