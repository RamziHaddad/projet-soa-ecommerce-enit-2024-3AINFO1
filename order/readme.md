# Documentation du Microservice de Commande

## Vue d'ensemble
gère le processus de commande depuis la validation du panier, à la reservation, au paiement jusqu'au déclenchement de la livraison.

## Architecture
Ce microservice interagit avec plusieurs autres microservices pour gérer l'ensemble du processus de commande en utilisant des appels REST pour communiquer entre les services.

## Technologies
- Spring Boot
- PostgreSQL
- REST pour la communication inter-microservices

## Configuration

### Propriétés de l'Application
- **Service Name**: order
- **Port**: 8084
- **Database**: PostgreSQL

## URLs des Services
1. `cart.service.url`
2. `inventory.service.url`
3. `payment.service.url`
4. `shipping.service.url`
5. `mail.service.url`

## Modèles de Domaine


### Workflow des Statuts de Commande
```
CREATED → RESERVED → PAID → SHIPPING_SCHEDULED → COMPLETED
                  ↓         ↓
               FAILED ← CANCELED
```

### Statuts des Commandes
- `CREATED`: État initial de la commande
- `RESERVED`: Inventaire réservé
- `PAID`: Paiement traité
- `SHIPPING_SCHEDULED`: Expédition organisée
- `COMPLETED`: Commande exécutée
- `CANCELED`: Commande annulée
- `FAILED`: Échec du traitement de la commande

## Workflow de Traitement de Commande
1. Création de Commande
    - Définit le statut initial à `CREATED`
    - Enregistre la commande en base de données
    - Envoie une demande de réservation d'inventaire

2. Réservation d'Inventaire
    - Valide la disponibilité des produits
    - Met à jour le statut de la commande à `RESERVED` ou `FAILED`
    - Déclenche le traitement du paiement si réussi

3. Traitement du Paiement
    - Traite le paiement de la commande
    - Met à jour le statut de la commande à `PAID` ou `FAILED`
    - Initie la planification de l'expédition si réussi

4. Planification de l'Expédition
    - Organise la logistique d'expédition
    - Met à jour le statut de la commande à `SHIPPING_SCHEDULED` ou `FAILED`
    - Déclenche la notification client

5. Notification
    - Envoie la confirmation de commande
    - Met à jour le statut final de la commande à `COMPLETED` ou `FAILED`