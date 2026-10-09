package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Agence;

import java.util.List;

public interface IAgenceService {

    Agence add(Agence agence);

    List<Agence> findAll();

    Agence findById(Long id);

    Agence update(Long id, Agence agence);

    void delete(Long id);
}
