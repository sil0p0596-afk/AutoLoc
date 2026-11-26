package tn.esprit.autoloc.autolocapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.autolocapi.domain.StatutReservation;

import java.time.LocalDate;

/**
 * DTO de réponse enrichi pour une réservation :
 * expose les informations client et véhicule à plat
 * pour éviter toute dépendance à l'entité JPA.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {

    private Long idReservation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutReservation statut;

    // Client à plat
    private Long idClient;
    private String nomClient;
    private String prenomClient;

    // Véhicule à plat
    private Long idVehicule;
    private String immatriculationVehicule;
}
