package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Vehicule;

import java.util.List;

public interface IVehiculeService {

    Vehicule add(Vehicule vehicule);

    List<Vehicule> findAll();

    Vehicule findById(Long id);

    Vehicule update(Long id, Vehicule vehicule);

    void delete(Long id);
}
