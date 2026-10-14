# Stratégie de fetch et de cascade — AutoLoc

| Association | Fetch | Cascade | Justification |
|---|---|---|---|
| Contrat → Paiement | LAZY | ALL + orphanRemoval | Un paiement n'existe que rattaché à son contrat (composition) ; supprimer le contrat supprime ses paiements, et retirer un paiement de la liste le supprime en base. |
| Agence → Vehicule | LAZY | Aucune | Un véhicule survit à son agence (il peut être réaffecté) ; charger une agence ne doit pas charger toute sa flotte. |
| Agence → Employe | LAZY | Aucune | Un employé a un cycle de vie indépendant (mutation possible) ; la liste n'est chargée qu'à la demande. |
| Vehicule ↔ Equipement | LAZY | Aucune | Les équipements sont partagés entre plusieurs véhicules : supprimer un véhicule ne doit pas supprimer un équipement. Un Set est utilisé pour éviter les doublons. |
| Client → Reservation | LAZY | PERSIST | Enregistrer un nouveau client enregistre aussi ses nouvelles réservations ; pas de REMOVE afin de conserver l'historique des réservations. |
| Reservation → Vehicule | LAZY | Aucune | Le véhicule est une entité indépendante ; une réservation ne fait que le référencer. |
| Reservation ↔ Contrat | LAZY | ALL (côté Reservation) | Le contrat découle de la réservation : il est créé, mis à jour et supprimé avec elle. |
| Vehicule → Maintenance | LAZY | PERSIST | Une maintenance est enregistrée avec son véhicule ; pas de REMOVE afin de garder l'historique d'entretien. |

## Principes retenus

- Les associations @ManyToOne et @OneToOne sont forcées en LAZY (EAGER par défaut) pour éviter de charger toute la chaîne d'objets liés.
- Les associations @OneToMany et @ManyToMany restent en LAZY : une collection n'est chargée que si elle est utilisée.
- La cascade REMOVE n'est appliquée qu'en cas de composition, quand l'enfant ne peut pas exister sans son parent.
- Pas de @Data sur les entités : le toString/hashCode généré parcourrait les deux côtés des relations bidirectionnelles et provoquerait un StackOverflowError. On utilise @Getter/@Setter ciblés.