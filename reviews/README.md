# Review Service

Le service **Reviews** permet de gérer les avis des utilisateurs sur des produits. Ce service expose des API pour ajouter, consulter, et vérifier les avis associés à un produit spécifique. Il utilise une base de données relationnelle pour stocker les avis et s'intègre avec d'autres services comme le service de catalogues de produits.

## Table des matières
- [Fonctionnalités](#fonctionnalités)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [API](#api)

---

## Fonctionnalités

- **Ajout d'avis** : Permet à un utilisateur authentifié d'ajouter un avis pour un produit existant.
- **Consultation des avis** : Récupère les avis pour un produit spécifique.
- **Validation de produit** : Vérifie si un produit existe dans le catalogue avant d'accepter un avis.
- **Protection via Token** : Authentifie l'utilisateur à l'aide d'un token.

---

## Prérequis

Avant d'exécuter ce service, assurez-vous d'avoir installé :
- [.NET 8 SDK](https://dotnet.microsoft.com/download/dotnet/8.0)
- [PostgreSQL](https://www.postgresql.org/)
- Un client HTTP comme [Postman](https://www.postman.com/) pour tester les APIs.
- Une instance du service **Catalog** fonctionnelle et disponible à l'URL spécifiée.

---

## Installation

**1. Clonez ce dépôt :**
   ```bash
   git clone https://github.com/RamziHaddad/projet-soa-ecommerce-enit-2024-3AINFO1.git
   cd projet-soa-ecommerce-enit-2024-3AINFO1/reviews
   ```

**2. Configurez la chaîne de connexion dans le fichier appsettings.json :**
   ```bash
   {
      "ConnectionStrings": {
          "DefaultConnection": "Host=localhost;Database=ReviewDB;Username=postgres;Password=azerty"
      }
   }
   ```

**3. Appliquez les migrations de la base de données:**
   ```bash
   dotnet ef database update
   ```

**4. Lancez le projet :**
   ```bash
   dotnet run
   ```

## API

**1. Ajouter un avis**
   
   **POST** ```/api/reviews```
   

Ajoute un avis pour un produit existant.

- Headers:

   ```bash
   token : Token d'authentification de l'utilisateur.
   ```
- Body :

   ```bash
   {
     "produitId": "f4f138cb-ccf5-45ef-bb15-bf5dfbb6cb9a",
     "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa7",
     "commentaire": "new comment"
   }
   ```

- Réponses :

  + 201 Created :

   ```bash
   {
      "id": "34ee49dc-74ad-478a-b2e9-fa044dde4ddf",
      "produitId": "f4f138cb-ccf5-45ef-bb15-bf5dfbb6cb9a",
      "userId": "8d84fa4c-6182-49f1-888c-66d92a73ebaf",
      "commentaire": "new comment",
      "dateCreation": "2024-12-23T21:18:47.5435348Z"
    }
    ```

  + 409 Conflict :

   ```bash
   L'utilisateur a déjà laissé un avis pour ce produit.
   ```

    + 404 Not Found:

   ```bash
   Produit non trouvé dans le catalogue.
   ```

**2. Récupérer les avis d'un produit**

**GET** ``` /api/reviews/{idProduit}```

Récupère tous les avis pour un produit donné.

- Paramètres :
   ```bash
   idProduit : L'identifiant du produit (UUID).
   ```

- Réponses :
   ```bash
   200 OK : Retourne une liste de commentaires.
   404 Not Found : Aucun avis trouvé pour le produit.
   ```




