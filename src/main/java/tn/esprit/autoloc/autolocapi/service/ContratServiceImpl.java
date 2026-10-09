package tn.esprit.autoloc.autolocapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.domain.Contrat;
import tn.esprit.autoloc.autolocapi.repository.IContratRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository repository;

    @Override
    @Transactional
    public Contrat add(Contrat contrat) {
        contrat.setIdContrat(null);
        return repository.save(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat findById(Long id) {
        return getOrThrow(id);
    }

    @Override
    @Transactional
    public Contrat update(Long id, Contrat contrat) {
        getOrThrow(id);
        contrat.setIdContrat(id);
        return repository.save(contrat);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Contrat getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrat introuvable avec l'id " + id));
    }
}
