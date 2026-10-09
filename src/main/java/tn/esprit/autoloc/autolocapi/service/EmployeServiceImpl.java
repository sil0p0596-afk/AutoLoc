package tn.esprit.autoloc.autolocapi.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.autolocapi.domain.Employe;
import tn.esprit.autoloc.autolocapi.repository.IEmployeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository repository;

    @Override
    @Transactional
    public Employe add(Employe employe) {
        employe.setIdEmploye(null);
        return repository.save(employe);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employe> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employe findById(Long id) {
        return getOrThrow(id);
    }

    @Override
    @Transactional
    public Employe update(Long id, Employe employe) {
        getOrThrow(id);
        employe.setIdEmploye(id);
        return repository.save(employe);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Employe getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employe introuvable avec l'id " + id));
    }
}
