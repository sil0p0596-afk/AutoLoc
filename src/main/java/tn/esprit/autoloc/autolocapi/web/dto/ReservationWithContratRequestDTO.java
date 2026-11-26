package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.autolocapi.domain.StatutReservation;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de requête pour créer simultanément
 * une réservation et son contrat associé.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationWithContratRequestDTO {

    // --- Réservation ---
    @NotNull(message = "L'identifiant du client est obligatoire")
    private Long idClient;

    @NotNull(message = "L'identifiant du véhicule est obligatoire")
    private Long idVehicule;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate dateFin;

    @NotNull(message = "Le statut est obligatoire")
    private StatutReservation statut;

    // --- Contrat associé ---
    @NotNull(message = "La date de signature du contrat est obligatoire")
    private LocalDate dateSignature;

    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant total doit être positif")
    private BigDecimal montantTotal;

    private boolean valide;
}
