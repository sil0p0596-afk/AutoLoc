# AutoLoc — Notes Atelier 5

## A. Endpoints

| Contrôleur | Verbe | Route | Service appelé | Code succès | Codes erreur |
|---|---|---|---|---|---|
| AgenceController | GET | /api/agences | findAll() | 200 | — |
| AgenceController | GET | /api/agences/{id} | findById(id) | 200 | 404 |
| AgenceController | POST | /api/agences | create(agence) | 201 | 400 |
| AgenceController | PUT | /api/agences/{id} | update(id, agence) | 200 | 404, 400 |
| AgenceController | DELETE | /api/agences/{id} | deleteById(id) | 204 | 404 |
| ClientController | GET | /api/clients | findAll() | 200 | — |
| ClientController | GET | /api/clients/{id} | findById(id) | 200 | 404 |
| ClientController | POST | /api/clients | create(client) | 201 | 400 |
| ClientController | PUT | /api/clients/{id} | update(id, client) | 200 | 404, 400 |
| ClientController | DELETE | /api/clients/{id} | deleteById(id) | 204 | 404 |
| VehiculeController | GET | /api/vehicules | findAll() | 200 | — |
| VehiculeController | GET | /api/vehicules/{id} | findById(id) | 200 | 404 |
| VehiculeController | POST | /api/vehicules | create(vehicule) | 201 | 400 |
| VehiculeController | PUT | /api/vehicules/{id} | update(id, vehicule) | 200 | 404, 400 |
| VehiculeController | DELETE | /api/vehicules/{id} | deleteById(id) | 204 | 404 |
| EmployeController | GET | /api/employes | findAll() | 200 | — |
| EmployeController | GET | /api/employes/{id} | findById(id) | 200 | 404 |
| EmployeController | POST | /api/employes | create(employe) | 201 | 400 |
| EmployeController | PUT | /api/employes/{id} | update(id, employe) | 200 | 404, 400 |
| EmployeController | DELETE | /api/employes/{id} | deleteById(id) | 204 | 404 |
| EquipementController | GET | /api/equipements | findAll() | 200 | — |
| EquipementController | GET | /api/equipements/{id} | findById(id) | 200 | 404 |
| EquipementController | POST | /api/equipements | create(equipement) | 201 | 400 |
| EquipementController | PUT | /api/equipements/{id} | update(id, equipement) | 200 | 404, 400 |
| EquipementController | DELETE | /api/equipements/{id} | deleteById(id) | 204 | 404 |
| ReservationController | GET | /api/reservations | findAll() | 200 | — |
| ReservationController | GET | /api/reservations/{id} | findById(id) | 200 | 404 |
| ReservationController | POST | /api/reservations | create(reservation) | 201 | 400 |
| ReservationController | PUT | /api/reservations/{id} | update(id, reservation) | 200 | 404, 400 |
| ReservationController | DELETE | /api/reservations/{id} | deleteById(id) | 204 | 404 |
| ContratController | GET | /api/contrats | findAll() | 200 | — |
| ContratController | GET | /api/contrats/{id} | findById(id) | 200 | 404 |
| ContratController | POST | /api/contrats | create(contrat) | 201 | 400 |
| ContratController | PUT | /api/contrats/{id} | update(id, contrat) | 200 | 404, 400 |
| ContratController | DELETE | /api/contrats/{id} | deleteById(id) | 204 | 404 |
| PaiementController | GET | /api/paiements | findAll() | 200 | — |
| PaiementController | GET | /api/paiements/{id} | findById(id) | 200 | 404 |
| MaintenanceController | GET | /api/maintenances | findAll() | 200 | — |
| MaintenanceController | GET | /api/maintenances/{id} | findById(id) | 200 | 404 |
| MaintenanceController | POST | /api/maintenances | create(maintenance) | 201 | 400 |
| MaintenanceController | PUT | /api/maintenances/{id} | update(id, maintenance) | 200 | 404, 400 |
| MaintenanceController | DELETE | /api/maintenances/{id} | deleteById(id) | 204 | 404 |

---

## B. Annotations Jackson

| Association | Annotation retenue | Justification |
|---|---|---|
| Contrat.paiements (OneToMany) | `@JsonManagedReference("contrat-paiements")` | Composition : les paiements se créent dans le JSON du contrat (cascade ALL). Jackson sérialise la liste normalement. |
| Paiement.contrat (ManyToOne) | `@JsonBackReference("contrat-paiements")` | Côté "retour" de la composition. Jackson ignore ce champ lors de la sérialisation pour casser la boucle. |
| Agence.vehicules, Agence.employes | `@JsonIgnore` | Listes inverses sans sens dans la réponse JSON d'une agence. L'agence est référencée depuis le véhicule/employé par son id. |
| Client.reservations | `@JsonIgnore` | Même logique : la liste inverse n'est pas utile dans la réponse d'un client. |
| Vehicule.reservations, Vehicule.maintenances | `@JsonIgnore` | Listes inverses ; le véhicule est exposé depuis la réservation/maintenance. |
| Reservation.contrat | `@JsonIgnore` | Côté inverse du OneToOne ; sinon Reservation → Contrat → Reservation boucle. |
| Employe.agence, Vehicule.agence, etc. | `@JsonIgnoreProperties({"vehicules","employes",...})` | On expose l'objet parent mais on filtre ses propres listes inverses pour éviter la récursion profonde. |

**Différence entre `@JsonBackReference` et `@JsonIgnore` :**

- `@JsonBackReference` est conçu pour les associations **bidirectionnelles parent/enfant** : il indique que ce côté est le "retour" de la référence gérée. Le champ est ignoré à la **sérialisation** mais **reste désérialisable** (Jackson le reconstitue automatiquement). À utiliser avec `@JsonManagedReference` sur l'autre côté.
- `@JsonIgnore` est un **exclusion totale** : le champ est ignoré dans les deux sens (sérialisation ET désérialisation). À utiliser quand la liste inverse n'est pas nécessaire du tout dans l'API.

---

## C. Postman et Swagger

**Postman** sert à **tester** l'API de façon manuelle et rejouable. On construit des requêtes HTTP, on vérifie les codes de retour, on écrit des scripts d'assertion dans l'onglet "Scripts > After response", et on enchaîne les appels en mémorisant des variables d'environnement (ex. `contratId`). Exemple concret : après un POST /api/contrats qui renvoie 201, le script `pm.environment.set("contratId", pm.response.json().idContrat)` permet d'utiliser `{{contratId}}` dans les requêtes PUT et DELETE suivantes.

**Swagger UI** sert à **documenter et explorer** l'API. Il est généré automatiquement par SpringDoc en analysant les contrôleurs et leurs annotations (`@Tag`, `@Operation`, `@ApiResponse`). L'interface est disponible à `/api/swagger-ui.html`. Exemple concret : après avoir ajouté `@Tag(name = "Contrats")` sur `ContratController`, Swagger UI regroupe tous ses endpoints sous un accordéon "Contrats" avec les descriptions de chaque réponse (200, 201, 404...).

Ils sont complémentaires : Postman pour les tests fonctionnels en développement, Swagger UI pour la documentation vivante consultable par les consommateurs de l'API.

---

## D. Anomalies SonarQube

| # | Règle | Description de l'anomalie | Correction appliquée |
|---|---|---|---|
| 1 | java:S6813 — Field injection should be avoided | Les anciens contrôleurs utilisaient `@Autowired` sur un champ (injection par attribut). | Remplacé par injection par constructeur avec `@RequiredArgsConstructor` de Lombok et un champ `private final`. |
| 2 | java:S1128 — Unused imports | Après refactoring, certains imports (`java.util.*`, `org.springframework.beans.factory.annotation.*`) restaient inutilisés. | Supprimés dans tous les contrôleurs réécrits. |
| 3 | java:S6539 — Exposing JPA entities directly in REST controllers | Les entités JPA sont exposées directement (champs internes visibles, couplage base/API). | Anomalie documentée et acceptée provisoirement ; sera corrigée au Chapitre 10 avec l'introduction des DTO. |

---

## E. Questions de compréhension

**1. Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?**

Le `DispatcherServlet` est le point d'entrée unique de Spring MVC. Il délègue à `HandlerMapping` (en pratique `RequestMappingHandlerMapping`) qui analyse les annotations `@RequestMapping`, `@GetMapping`, etc. des contrôleurs au démarrage, construit une table de correspondance (verbe HTTP + URL → méthode), et l'utilise à chaque requête pour résoudre la méthode à invoquer.

**2. Pourquoi `ContratController` dépend-il de `IContratService` et non de `ContratRepository` ni de `ContratServiceImpl` ?**

Dépendre de l'interface `IContratService` respecte le principe d'inversion de dépendances (DIP) et la séparation en couches : le contrôleur ne connaît que le contrat métier (l'interface), pas l'implémentation concrète ni les détails de persistance. Cela permet de remplacer ou mocker l'implémentation sans toucher au contrôleur, et évite de court-circuiter la couche service.

**3. Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?**

- `201 Created` signale qu'une ressource a été **créée** ; c'est sémantiquement plus précis que 200 OK.
- `204 No Content` indique que l'opération a réussi mais qu'il n'y a **rien à retourner** dans le corps.
- `404 Not Found` est une erreur **client** (la ressource n'existe pas) : c'est une situation métier attendue, traduite proprement par `GlobalExceptionHandler`. Un 500 serait une erreur serveur imprévue, ce qui serait trompeur et masquerait la vraie cause.

**4. Pourquoi `PaiementController` n'expose-t-il que des GET ? Quelle réponse reçoit un POST /api/paiements ?**

Les paiements sont des enfants du contrat (composition, cascade ALL) : ils sont créés, modifiés et supprimés via le contrat. Exposer un POST /api/paiements contournerait cette règle métier. Un POST sur `/api/paiements` reçoit **405 Method Not Allowed**, car aucune méthode du contrôleur n'est mappée sur POST.

**5. Quels risques présente l'exposition directe des entités JPA ? Pourquoi `Contrat.paiements` utilise-t-il `@JsonManagedReference` alors que `Agence.vehicules` utilise `@JsonIgnore` ?**

Risques : couplage entre le schéma de base de données et le contrat API (tout changement de colonne impacte l'API), exposition de champs internes sensibles, problèmes de sérialisation avec les proxies Hibernate, et impossibilité de façonner la réponse JSON indépendamment du modèle.

- `Contrat.paiements` utilise `@JsonManagedReference` car la relation est une **composition** (les paiements n'existent que dans le contexte d'un contrat, cascade = ALL). Il est utile de les inclure dans le JSON du contrat pour créer contrat + paiements en un seul POST.
- `Agence.vehicules` utilise `@JsonIgnore` car c'est une liste **inverse sans valeur dans la réponse** d'une agence : un véhicule référence son agence par `{ "agence": {"idAgence": 1} }`, la liste inverse n'est pas nécessaire et créerait une boucle infinie.
