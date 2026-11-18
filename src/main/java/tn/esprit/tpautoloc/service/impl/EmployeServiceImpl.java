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
import tn.esprit.tpautoloc.web.dto.EmployeDTO;
import tn.esprit.tpautoloc.web.mapper.EmployeMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;
    private final EmployeMapper employeMapper;

    @Override
    public EmployeDTO creer(EmployeDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouvel employé ne doit pas avoir d'identifiant");
        }
        validerAttributsObligatoires(dto);
        Employe employe = employeMapper.toEntity(dto);
        employe.setAgence(resoudreAgence(dto.getAgenceId()));
        Employe saved = employeRepository.save(employe);
        return employeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeDTO findById(Long id) {
        return employeMapper.toDto(getEmploye(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeDTO> findAll() {
        return employeRepository.findAll().stream()
                .map(employeMapper::toDto)
                .toList();
    }

    @Override
    public EmployeDTO update(Long id, EmployeDTO dto) {
        validerAttributsObligatoires(dto);
        Employe existant = getEmploye(id);
        existant.setNom(dto.getNom());
        existant.setPrenom(dto.getPrenom());
        existant.setRole(dto.getRole());
        existant.setAgence(resoudreAgence(dto.getAgenceId()));
        return employeMapper.toDto(employeRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }

    // ===================== Partie 1 : affectation / désaffectation =====================

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

    // ===================================================================================

    private Employe getEmploye(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    private void validerAttributsObligatoires(EmployeDTO dto) {
        if (dto.getNom() == null || dto.getNom().isBlank()
                || dto.getPrenom() == null || dto.getPrenom().isBlank()) {
            throw new IllegalArgumentException("Le nom et le prénom de l'employé sont obligatoires");
        }
    }

    private Agence resoudreAgence(Long agenceId) {
        if (agenceId == null) {
            throw new IllegalArgumentException("L'agence de l'employé est obligatoire");
        }
        return agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agenceId));
    }
}
