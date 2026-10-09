package tn.esprit.autoloc.autolocapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.domain.Agence;
import tn.esprit.autoloc.autolocapi.repository.IAgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository repository;

    @Override
    @Transactional
    public Agence add(Agence agence) {
        agence.setIdAgence(null);
        return repository.save(agence);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Agence> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Agence findById(Long id) {
        return getOrThrow(id);
    }

    @Override
    @Transactional
    public Agence update(Long id, Agence agence) {
        getOrThrow(id);
        agence.setIdAgence(id);
        return repository.save(agence);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Agence getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agence introuvable avec l'id " + id));
    }
}
