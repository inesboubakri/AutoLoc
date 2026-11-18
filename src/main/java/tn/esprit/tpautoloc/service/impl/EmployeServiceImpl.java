package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IEmployeRepository;
import tn.esprit.tpautoloc.service.IEmployeService;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;

    @Override
    public Employe create(Employe employe) {
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException(
                    "Un nouvel employé ne doit pas avoir d'identifiant");
        }
        validerAttributsObligatoires(employe);
        employe.setAgence(resoudreAgence(employe));
        return employeRepository.save(employe);
    }

    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe update(Long id, Employe employe) {
        validerAttributsObligatoires(employe);
        Employe existant = findById(id);
        existant.setNom(employe.getNom());
        existant.setPrenom(employe.getPrenom());
        existant.setRole(employe.getRole());
        existant.setAgence(resoudreAgence(employe));
        return employeRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }

    // ===================== Atelier 7 — Partie 1.4 : affectation / désaffectation =====================

    @Override
    @Transactional
    public void affecterEmployeAAgence(Long employeId, Long agenceId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", employeId));

        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agenceId));

        // Employe porte la clé étrangère id_agence : c'est le côté propriétaire
        employe.setAgence(agence);
        employeRepository.save(employe);
    }

    @Override
    @Transactional
    public void desaffecterEmployeDeAgence(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", employeId));

        if (employe.getAgence() == null) {
            throw new IllegalArgumentException(
                    "L'employé " + employeId + " n'est rattaché à aucune agence");
        }

        employe.setAgence(null);
        employeRepository.save(employe);
    }

    // ==================================================================================================

    private void validerAttributsObligatoires(Employe employe) {
        if (employe.getNom() == null || employe.getNom().isBlank()
                || employe.getPrenom() == null || employe.getPrenom().isBlank()) {
            throw new IllegalArgumentException("Le nom et le prénom de l'employé sont obligatoires");
        }
    }

    private Agence resoudreAgence(Employe employe) {
        if (employe.getAgence() == null || employe.getAgence().getIdAgence() == null) {
            throw new IllegalArgumentException("L'agence de l'employé est obligatoire");
        }
        Long agenceId = employe.getAgence().getIdAgence();
        return agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agenceId)); // 404 si absente
    }
}