package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.domain.Paiement;

public interface IPaiementService {
    Paiement findById(Long id);
    List<Paiement> findAll();
    Paiement create(Long contratId, Paiement paiement);
    Paiement update(Long id, Paiement paiement);
    void deleteById(Long id);
}