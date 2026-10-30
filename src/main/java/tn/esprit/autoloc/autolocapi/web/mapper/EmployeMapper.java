package tn.esprit.autoloc.autolocapi.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.domain.Employe;
import tn.esprit.autoloc.autolocapi.web.dto.EmployeDTO;

@Component
public class EmployeMapper {

    public EmployeDTO toDTO(Employe employe) {
        if (employe == null) return null;
        EmployeDTO dto = new EmployeDTO();
        dto.setIdEmploye(employe.getIdEmploye());
        dto.setNom(employe.getNom());
        dto.setPrenom(employe.getPrenom());
        dto.setRole(employe.getRole());
        if (employe.getAgence() != null) {
            dto.setIdAgence(employe.getAgence().getIdAgence());
        }
        return dto;
    }

    public Employe toEntity(EmployeDTO dto) {
        if (dto == null) return null;
        Employe employe = new Employe();
        employe.setIdEmploye(dto.getIdEmploye());
        employe.setNom(dto.getNom());
        employe.setPrenom(dto.getPrenom());
        employe.setRole(dto.getRole());
        if (dto.getIdAgence() != null) {
            Agence agence = new Agence();
            agence.setIdAgence(dto.getIdAgence());
            employe.setAgence(agence);
        }
        return employe;
    }
}
