# Atelier 3 — Couche Repository Spring Data JPA

## Choix d'interface par entité

Toutes les interfaces étendent JpaRepository<Entité, Long> : CRUD complet,
findAll renvoie une List, saveAndFlush disponible.

- IAgenceRepository extends JpaRepository<Agence, Long>
- IEmployeRepository extends JpaRepository<Employe, Long>
- IVehiculeRepository extends JpaRepository<Vehicule, Long>
- IEquipementRepository extends JpaRepository<Equipement, Long>
- IClientRepository extends JpaRepository<Client, Long>
- IReservationRepository extends JpaRepository<Reservation, Long>
- IContratRepository extends JpaRepository<Contrat, Long>
  Entité fil rouge : CRUD complet observé en premier, avec cascade et
  orphanRemoval vers Paiement.
- IPaiementRepository extends JpaRepository<Paiement, Long>
  Créée pour la lecture ; le code métier passe par Contrat pour créer ou
  retirer un paiement (composition).
- IMaintenanceRepository extends JpaRepository<Maintenance, Long>

Relance de l'application après création des 9 interfaces : les logs indiquent
"Found 9 JPA repository interfaces."

## Anomalies SonarQube for IDE corrigées

1. Import java.util.* inutilisé dans Employe.java
   Règle : java:S1128 — un import non utilisé alourdit le code et nuit à la
   lisibilité.
   Correction : import supprimé (l'entité n'a pas d'attribut de type
   collection).

2. Import java.util.* inutilisé dans Maintenance.java
   Règle : java:S1128 — idem.
   Correction : import supprimé.

3. Import java.util.* inutilisé dans Paiement.java
   Règle : java:S1128 — idem.
   Correction : import supprimé.

4. Import java.util.* inutilisé dans Reservation.java
   Règle : java:S1128 — idem.
   Correction : import supprimé.

Après correction, une nouvelle analyse complète du projet ne signale plus
aucun problème ("No issues to display").