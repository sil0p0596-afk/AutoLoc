package tn.esprit.autoloc.autolocapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.domain.Maintenance;
import tn.esprit.autoloc.autolocapi.repository.IMaintenanceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository repository;

    @Override
    @Transactional
    public Maintenance add(Maintenance maintenance) {
        maintenance.setIdMaintenance(null);
        return repository.save(maintenance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Maintenance findById(Long id) {
        return getOrThrow(id);
    }

    @Override
    @Transactional
    public Maintenance update(Long id, Maintenance maintenance) {
        getOrThrow(id);
        maintenance.setIdMaintenance(id);
        return repository.save(maintenance);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Maintenance getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maintenance introuvable avec l'id " + id));
    }
}
