package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.domain.Employe;

public interface IEmployeService {
    Employe create(Employe employe);
    Employe findById(Long id);
    List<Employe> findAll();
    Employe update(Long id, Employe employe);
    void deleteById(Long id);

    // Atelier 7 — Partie 1.4 : affectation / désaffectation
    void affecterEmployeAAgence(Long employeId, Long agenceId);
    void desaffecterEmployeDeAgence(Long employeId);
}