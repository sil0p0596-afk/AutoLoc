package tn.esprit.autoloc.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.domain.*;
import tn.esprit.autoloc.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autoloc.autolocapi.repository.*;
import tn.esprit.autoloc.autolocapi.web.dto.ContratDTO;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.autolocapi.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.autolocapi.web.dto.VehiculeDTO;
import tn.esprit.autoloc.autolocapi.web.mapper.ContratMapper;
import tn.esprit.autoloc.autolocapi.web.mapper.VehiculeMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AffectationServiceImpl implements IAffectationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IEquipementRepository equipementRepository;
    private final IMaintenanceRepository maintenanceRepository;
    private final IContratRepository contratRepository;

    private final VehiculeMapper vehiculeMapper;
    private final ContratMapper contratMapper;

    // -------------------------------------------------------------------------
    // 1. Affecter une réservation à un client (par nom + prénom)
    // -------------------------------------------------------------------------
    @Override
    @Transactional
    public ReservationResponseDTO affecterReservationAClient(Long idReservation,
                                                              String nom,
                                                              String prenom) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", idReservation));

        Client client = clientRepository.findByNomAndPrenom(nom, prenom)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aucun client trouvé avec le nom « " + nom + " » et prénom « " + prenom + " »"));

        reservation.setClient(client);
        Reservation saved = reservationRepository.save(reservation);
        return toResponseDTO(saved);
    }

    // -------------------------------------------------------------------------
    // 2. Affecter un équipement à un véhicule
    // -------------------------------------------------------------------------
    @Override
    @Transactional
    public VehiculeDTO affecterVehiculeAEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", idVehicule));

        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipement));

        vehicule.getEquipements().add(equipement);
        return vehiculeMapper.toDTO(vehiculeRepository.save(vehicule));
    }

    // -------------------------------------------------------------------------
    // 3. Affecter une maintenance à un véhicule (identifié par immatriculation)
    // -------------------------------------------------------------------------
    @Override
    @Transactional
    public VehiculeDTO affecterMaintenanceAVehicule(String description, String immatriculation) {
        Vehicule vehicule = vehiculeRepository.findByImmatriculation(immatriculation)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aucun véhicule trouvé avec l'immatriculation « " + immatriculation + " »"));

        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDescription(description);
        maintenance.setVehicule(vehicule);
        maintenanceRepository.save(maintenance);

        // Mise à jour du statut du véhicule
        vehicule.setStatut(StatutVehicule.MAINTENANCE);
        return vehiculeMapper.toDTO(vehiculeRepository.save(vehicule));
    }

    // -------------------------------------------------------------------------
    // 4. Affecter un contrat à la réservation qui chevauche la période donnée
    // -------------------------------------------------------------------------
    @Override
    @Transactional
    public ContratDTO affecterContratAReservation(LocalDate dateSignature,
                                                   LocalDate dateDebut,
                                                   LocalDate dateFin) {
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException(
                    "La date de fin ne peut pas être antérieure à la date de début");
        }

        List<Reservation> reservations = reservationRepository
                .findByDateDebutLessThanEqualAndDateFinGreaterThanEqual(dateFin, dateDebut);

        if (reservations.isEmpty()) {
            throw new IllegalArgumentException(
                    "Aucune réservation ne correspond à la période [" + dateDebut + " → " + dateFin + "]");
        }

        Reservation reservation = reservations.get(0);

        // Calcul du montant total basé sur le tarif journalier du véhicule
        long nbJours = dateDebut.until(dateFin).getDays();
        if (nbJours < 1) nbJours = 1;

        BigDecimal montantTotal = BigDecimal.ZERO;
        if (reservation.getVehicule() != null
                && reservation.getVehicule().getTarifJournalier() != null) {
            montantTotal = reservation.getVehicule()
                    .getTarifJournalier()
                    .multiply(BigDecimal.valueOf(nbJours));
        }

        Contrat contrat = new Contrat();
        contrat.setDateSignature(dateSignature);
        contrat.setMontantTotal(montantTotal);
        contrat.setValide(true);
        contrat.setReservation(reservation);

        return contratMapper.toDTO(contratRepository.save(contrat));
    }

    // -------------------------------------------------------------------------
    // 5. Créer une réservation ET son contrat associé en une seule opération
    // -------------------------------------------------------------------------
    @Override
    @Transactional
    public ReservationResponseDTO creerReservationEtContratAssocie(
            ReservationWithContratRequestDTO requestDTO) {

        if (requestDTO.getDateFin().isBefore(requestDTO.getDateDebut())) {
            throw new IllegalArgumentException(
                    "La date de fin ne peut pas être antérieure à la date de début");
        }

        Client client = clientRepository.findById(requestDTO.getIdClient())
                .orElseThrow(() -> new ResourceNotFoundException("Client", requestDTO.getIdClient()));

        Vehicule vehicule = vehiculeRepository.findById(requestDTO.getIdVehicule())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", requestDTO.getIdVehicule()));

        // Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setDateDebut(requestDTO.getDateDebut());
        reservation.setDateFin(requestDTO.getDateFin());
        reservation.setStatut(requestDTO.getStatut());
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        Reservation savedReservation = reservationRepository.save(reservation);

        // Créer le contrat lié
        Contrat contrat = new Contrat();
        contrat.setDateSignature(requestDTO.getDateSignature());
        contrat.setMontantTotal(requestDTO.getMontantTotal());
        contrat.setValide(requestDTO.isValide());
        contrat.setReservation(savedReservation);
        contratRepository.save(contrat);

        return toResponseDTO(savedReservation);
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------
    private ReservationResponseDTO toResponseDTO(Reservation r) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setIdReservation(r.getIdReservation());
        dto.setDateDebut(r.getDateDebut());
        dto.setDateFin(r.getDateFin());
        dto.setStatut(r.getStatut());
        if (r.getClient() != null) {
            dto.setIdClient(r.getClient().getIdClient());
            dto.setNomClient(r.getClient().getNom());
            dto.setPrenomClient(r.getClient().getPrenom());
        }
        if (r.getVehicule() != null) {
            dto.setIdVehicule(r.getVehicule().getIdVehicule());
            dto.setImmatriculationVehicule(r.getVehicule().getImmatriculation());
        }
        return dto;
    }
}
