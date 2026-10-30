package tn.esprit.autoloc.autolocapi.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.domain.Equipement;
import tn.esprit.autoloc.autolocapi.web.dto.EquipementDTO;

@Component
public class EquipementMapper {

    public EquipementDTO toDTO(Equipement equipement) {
        if (equipement == null) return null;
        EquipementDTO dto = new EquipementDTO();
        dto.setIdEquipement(equipement.getIdEquipement());
        dto.setLibelle(equipement.getLibelle());
        return dto;
    }

    public Equipement toEntity(EquipementDTO dto) {
        if (dto == null) return null;
        Equipement equipement = new Equipement();
        equipement.setIdEquipement(dto.getIdEquipement());
        equipement.setLibelle(dto.getLibelle());
        return equipement;
    }
}
