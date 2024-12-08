# Documentation du Microservice de Commande

## Vue d'ensemble
gère le processus de commande depuis la validation du panier, à la reservation, au paiement jusqu'au déclenchement de la livraison.

## Architecture 

## Technologies
- Spring Boot
- PostgreSQL
- Apache Kafka

## Configuration

### Propriétés de l'Application
- **Service Name**: order
- **Port**: 8084
- **Database**: PostgreSQL
- **Kafka Broker**: localhost:9092

## Sujets Kafka

### Topics Sortants
1. `inventory-reserve-request`
2. `payment-process-request`
3. `shipping-schedule-request`
4. `notification-send-request`

### Topics Entrants
1. `inventory-reserve-result`
2. `payment-process-result`
3. `shipping-schedule-result`
4. `notification-send-result`

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
