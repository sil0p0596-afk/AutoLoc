package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Paiement;

import java.util.List;

public interface IPaiementService {

    Paiement add(Paiement paiement);

    List<Paiement> findAll();

    Paiement findById(Long id);

    Paiement update(Long id, Paiement paiement);

    void delete(Long id);
}
