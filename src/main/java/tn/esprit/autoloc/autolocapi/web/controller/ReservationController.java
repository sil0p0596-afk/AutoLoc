package tn.esprit.autoloc.autolocapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.service.IReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Réservations", description = "Gestion des réservations de véhicules")
public class ReservationController {

    private final IReservationService reservationService;

    @Operation(summary = "Créer une réservation",
               description = "Corps attendu : { \"dateDebut\": \"2026-11-02\", \"dateFin\": \"2026-11-06\", " +
                             "\"statut\": \"EN_ATTENTE\", \"client\": {\"idClient\": 1}, " +
                             "\"vehicule\": {\"idVehicule\": 1} }. " +
                             "Retourne 400 si dateFin est antérieure à dateDebut.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Réservation créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides (date de fin antérieure ou champ manquant)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.create(reservation);
    }

    @Operation(summary = "Lister toutes les réservations")
    @ApiResponse(responseCode = "200", description = "Liste des réservations retournée")
    @GetMapping
    public List<Reservation> findAll() {
        return reservationService.findAll();
    }

    @Operation(summary = "Consulter une réservation par son identifiant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
        @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @GetMapping("/{id}")
    public Reservation findById(
            @Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        return reservationService.findById(id);
    }

    @Operation(summary = "Modifier une réservation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Réservation mise à jour"),
        @ApiResponse(responseCode = "404", description = "Réservation introuvable"),
        @ApiResponse(responseCode = "400", description = "Données invalides (date de fin antérieure)")
    })
    @PutMapping("/{id}")
    public Reservation update(
            @Parameter(description = "Identifiant de la réservation") @PathVariable Long id,
            @RequestBody Reservation reservation) {
        return reservationService.update(id, reservation);
    }

    @Operation(summary = "Supprimer une réservation")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Réservation supprimée"),
        @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        reservationService.deleteById(id);
    }
}
