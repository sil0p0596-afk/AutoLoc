package tn.esprit.autoloc.autolocapi.web.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.domain.Paiement;
import tn.esprit.autoloc.autolocapi.domain.Reservation;
import tn.esprit.autoloc.autolocapi.web.dto.ContratDTO;
import tn.esprit.autoloc.autolocapi.web.dto.PaiementDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContratMapper {

    private final PaiementMapper paiementMapper;

    public ContratDTO toDTO(Contrat contrat) {
        if (contrat == null) return null;
        ContratDTO dto = new ContratDTO();
        dto.setIdContrat(contrat.getIdContrat());
        dto.setDateSignature(contrat.getDateSignature());
        dto.setMontantTotal(contrat.getMontantTotal());
        dto.setValide(contrat.isValide());
        if (contrat.getReservation() != null) {
            dto.setIdReservation(contrat.getReservation().getIdReservation());
        }
        if (contrat.getPaiements() != null) {
            List<PaiementDTO> paiementsDTO = contrat.getPaiements().stream()
                    .map(paiementMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setPaiements(paiementsDTO);
        }
        return dto;
    }

    public Contrat toEntity(ContratDTO dto) {
        if (dto == null) return null;
        Contrat contrat = new Contrat();
        contrat.setIdContrat(dto.getIdContrat());
        contrat.setDateSignature(dto.getDateSignature());
        contrat.setMontantTotal(dto.getMontantTotal());
        contrat.setValide(dto.isValide());
        if (dto.getIdReservation() != null) {
            Reservation reservation = new Reservation();
            reservation.setIdReservation(dto.getIdReservation());
            contrat.setReservation(reservation);
        }
        if (dto.getPaiements() != null) {
            List<Paiement> paiements = new ArrayList<>();
            for (PaiementDTO p : dto.getPaiements()) {
                Paiement paiement = paiementMapper.toEntity(p);
                paiement.setContrat(contrat); // lien bidirectionnel pour la cascade
                paiements.add(paiement);
            }
            contrat.setPaiements(paiements);
        }
        return contrat;
    }
}
