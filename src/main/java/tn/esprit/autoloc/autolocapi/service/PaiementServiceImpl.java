package tn.esprit.autoloc.autolocapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.domain.Paiement;
import tn.esprit.autoloc.autolocapi.repository.IPaiementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository repository;

    @Override
    @Transactional
    public Paiement add(Paiement paiement) {
        paiement.setIdPaiement(null);
        return repository.save(paiement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Paiement findById(Long id) {
        return getOrThrow(id);
    }

    @Override
    @Transactional
    public Paiement update(Long id, Paiement paiement) {
        getOrThrow(id);
        paiement.setIdPaiement(id);
        return repository.save(paiement);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Paiement getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paiement introuvable avec l'id " + id));
    }
}
