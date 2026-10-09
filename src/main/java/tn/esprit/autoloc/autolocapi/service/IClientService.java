package tn.esprit.autoloc.autolocapi.service;

import tn.esprit.autoloc.autolocapi.domain.Client;

import java.util.List;

public interface IClientService {

    Client add(Client client);

    List<Client> findAll();

    Client findById(Long id);

    Client update(Long id, Client client);

    void delete(Long id);
}
