package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Employe;

import java.util.List;

public interface IEmployeService {

    Employe add(Employe employe);

    List<Employe> findAll();

    Employe findById(Long id);

    Employe update(Long id, Employe employe);

    void delete(Long id);
}
