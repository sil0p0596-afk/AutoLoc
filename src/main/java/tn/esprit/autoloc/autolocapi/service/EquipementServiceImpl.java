package tn.esprit.autoloc.autolocapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.domain.Equipement;
import tn.esprit.autoloc.autolocapi.repository.IEquipementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository repository;

    @Override
    @Transactional
    public Equipement add(Equipement equipement) {
        equipement.setIdEquipement(null);
        return repository.save(equipement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipement> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Equipement findById(Long id) {
        return getOrThrow(id);
    }

    @Override
    @Transactional
    public Equipement update(Long id, Equipement equipement) {
        getOrThrow(id);
        equipement.setIdEquipement(id);
        return repository.save(equipement);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Equipement getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipement introuvable avec l'id " + id));
    }
}
