package tn.esprit.autoloc.autolocapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.autolocapi.service.IAffectationService;
import tn.esprit.autoloc.autolocapi.web.dto.ContratDTO;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeDTO;

import java.time.LocalDate;

@RestController
@RequestMapping("/affectations")
@RequiredArgsConstructor
@Tag(name = "Affectations", description = "Services d'affectation métier")
public class AffectationController {

    private final IAffectationService affectationService;

    // 1 -----------------------------------------------------------------------
    @Operation(summary = "Affecter une réservation à un client (nom + prénom)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Réservation affectée au client"),
        @ApiResponse(responseCode = "404", description = "Réservation ou client introuvable"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/reservations/{idReservation}/client")
    public ReservationResponseDTO affecterReservationAClient(
            @PathVariable Long idReservation,
            @RequestParam String nom,
            @RequestParam String prenom) {
        return affectationService.affecterReservationAClient(idReservation, nom, prenom);
    }

    // 2 -----------------------------------------------------------------------
    @Operation(summary = "Affecter un équipement à un véhicule")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Équipement affecté au véhicule"),
        @ApiResponse(responseCode = "404", description = "Véhicule ou équipement introuvable")
    })
    @PutMapping("/vehicules/{idVehicule}/equipements/{idEquipement}")
    public VehiculeDTO affecterVehiculeAEquipement(
            @PathVariable Long idVehicule,
            @PathVariable Long idEquipement) {
        return affectationService.affecterVehiculeAEquipement(idVehicule, idEquipement);
    }

    // 3 -----------------------------------------------------------------------
    @Operation(summary = "Créer une maintenance pour un véhicule (par immatriculation)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Maintenance créée, véhicule en statut MAINTENANCE"),
        @ApiResponse(responseCode = "400", description = "Immatriculation introuvable")
    })
    @PostMapping("/vehicules/maintenance")
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeDTO affecterMaintenanceAVehicule(
            @RequestParam String description,
            @RequestParam String immatriculation) {
        return affectationService.affecterMaintenanceAVehicule(description, immatriculation);
    }

    // 4 -----------------------------------------------------------------------
    @Operation(summary = "Affecter un contrat à une réservation selon une période",
               description = "Trouve la première réservation qui chevauche [dateDebut, dateFin] " +
                             "et crée un contrat lié à celle-ci.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Contrat créé et lié à la réservation"),
        @ApiResponse(responseCode = "400", description = "Aucune réservation trouvée pour la période")
    })
    @PostMapping("/contrats")
    @ResponseStatus(HttpStatus.CREATED)
    public ContratDTO affecterContratAReservation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateSignature,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return affectationService.affecterContratAReservation(dateSignature, dateDebut, dateFin);
    }

    // 5 -----------------------------------------------------------------------
    @Operation(summary = "Créer une réservation et son contrat associé en une seule opération")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Réservation et contrat créés"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Client ou véhicule introuvable")
    })
    @PostMapping("/reservations-avec-contrat")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDTO creerReservationEtContratAssocie(
            @Valid @RequestBody ReservationWithContratRequestDTO requestDTO) {
        return affectationService.creerReservationEtContratAssocie(requestDTO);
    }
}
