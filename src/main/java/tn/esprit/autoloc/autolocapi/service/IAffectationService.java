package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.web.dto.ContratDTO;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeDTO;

import java.time.LocalDate;

public interface IAffectationService {

    /**
     * Affecte une réservation existante à un client identifié par nom + prénom.
     */
    ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom);

    /**
     * Affecte un équipement à un véhicule existant.
     */
    VehiculeDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement);

    /**
     * Crée une maintenance pour le véhicule identifié par son immatriculation
     * et retourne le véhicule mis à jour.
     */
    VehiculeDTO affecterMaintenanceAVehicule(String description, String immatriculation);

    /**
     * Crée un contrat lié à la première réservation dont la période
     * chevauche [dateDebut, dateFin] et dont la date de signature est donnée.
     */
    ContratDTO affecterContratAReservation(LocalDate dateSignature, LocalDate dateDebut, LocalDate dateFin);

    /**
     * Crée une réservation et son contrat associé en une seule opération.
     */
    ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO);
}
