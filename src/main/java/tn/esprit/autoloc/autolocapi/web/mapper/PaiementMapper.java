package tn.esprit.autoloc.autolocapi.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.domain.Paiement;
import tn.esprit.autoloc.autolocapi.web.dto.PaiementDTO;

@Component
public class PaiementMapper {

    public PaiementDTO toDTO(Paiement paiement) {
        if (paiement == null) return null;
        PaiementDTO dto = new PaiementDTO();
        dto.setIdPaiement(paiement.getIdPaiement());
        dto.setMontant(paiement.getMontant());
        dto.setDatePaiement(paiement.getDatePaiement());
        dto.setModePaiement(paiement.getModePaiement());
        if (paiement.getContrat() != null) {
            dto.setIdContrat(paiement.getContrat().getIdContrat());
        }
        return dto;
    }

    public Paiement toEntity(PaiementDTO dto) {
        if (dto == null) return null;
        Paiement paiement = new Paiement();
        paiement.setIdPaiement(dto.getIdPaiement());
        paiement.setMontant(dto.getMontant());
        paiement.setDatePaiement(dto.getDatePaiement());
        paiement.setModePaiement(dto.getModePaiement());
        // Le contrat est lié côté service, pas ici
        return paiement;
    }
}
