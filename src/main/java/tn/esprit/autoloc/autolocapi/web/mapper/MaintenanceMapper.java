package tn.esprit.autoloc.autolocapi.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.autolocapi.domain.Maintenance;
import tn.esprit.autoloc.autolocapi.domain.Vehicule;
import tn.esprit.autoloc.autolocapi.web.dto.MaintenanceDTO;

@Component
public class MaintenanceMapper {

    public MaintenanceDTO toDTO(Maintenance maintenance) {
        if (maintenance == null) return null;
        MaintenanceDTO dto = new MaintenanceDTO();
        dto.setIdMaintenance(maintenance.getIdMaintenance());
        dto.setDateDebut(maintenance.getDateDebut());
        dto.setDateFin(maintenance.getDateFin());
        dto.setDescription(maintenance.getDescription());
        if (maintenance.getVehicule() != null) {
            dto.setIdVehicule(maintenance.getVehicule().getIdVehicule());
        }
        return dto;
    }

    public Maintenance toEntity(MaintenanceDTO dto) {
        if (dto == null) return null;
        Maintenance maintenance = new Maintenance();
        maintenance.setIdMaintenance(dto.getIdMaintenance());
        maintenance.setDateDebut(dto.getDateDebut());
        maintenance.setDateFin(dto.getDateFin());
        maintenance.setDescription(dto.getDescription());
        if (dto.getIdVehicule() != null) {
            Vehicule vehicule = new Vehicule();
            vehicule.setIdVehicule(dto.getIdVehicule());
            maintenance.setVehicule(vehicule);
        }
        return maintenance;
    }
}
