package tn.esprit.autoloc.autolocapi.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratDTO {

    private Long idContrat;

    @NotNull(message = "La date de signature est obligatoire")
    private LocalDate dateSignature;

    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant total doit être positif")
    private BigDecimal montantTotal;

    private boolean valide;

    /** Identifiant de la réservation liée (optionnel à la création) */
    private Long idReservation;

    /** Paiements en cascade — validés à la création */
    @Valid
    private List<PaiementDTO> paiements = new ArrayList<>();
}
