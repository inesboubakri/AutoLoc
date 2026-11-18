package tn.esprit.tpautoloc.service.impl;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IVehiculeService;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;
import tn.esprit.tpautoloc.web.mapper.VehiculeMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private static final String ENTITE_VEHICULE = "Vehicule";

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public VehiculeDTO creer(VehiculeDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        validerTarif(dto.getTarifJournalier());
        Vehicule vehicule = vehiculeMapper.toEntity(dto);
        vehicule.setAgence(resoudreAgence(dto.getAgenceId()));
        Vehicule saved = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculeDTO findById(Long id) {
        return vehiculeMapper.toDto(getVehicule(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDTO> findAll() {
        return vehiculeRepository.findAll().stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }

    @Override
    public VehiculeDTO update(Long id, VehiculeDTO dto) {
        validerTarif(dto.getTarifJournalier());
        Vehicule existant = getVehicule(id);
        existant.setImmatriculation(dto.getImmatriculation());
        existant.setMarque(dto.getMarque());
        existant.setModele(dto.getModele());
        existant.setCategorie(dto.getCategorie());
        existant.setTarifJournalier(dto.getTarifJournalier());
        existant.setStatut(dto.getStatut());
        existant.setAgence(resoudreAgence(dto.getAgenceId()));
        return vehiculeMapper.toDto(vehiculeRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException(ENTITE_VEHICULE, id);
        }
        vehiculeRepository.deleteById(id);
    }

    // ===================== Partie 1 : affectation / désaffectation =====================

    @Override
    @Transactional
    public void affecterVehiculeAAgence(Long vehiculeId, Long agenceId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITE_VEHICULE, vehiculeId));

        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agenceId));

        // Vehicule porte la clé étrangère id_agence : c'est le côté propriétaire
        vehicule.setAgence(agence);
        vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional
    public void desaffecterVehiculeDeAgence(Long vehiculeId) {
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITE_VEHICULE, vehiculeId));

        if (vehicule.getAgence() == null) {
            throw new IllegalArgumentException(
                    "Le véhicule " + vehiculeId + " n'est rattaché à aucune agence");
        }

        vehicule.setAgence(null);
        vehiculeRepository.save(vehicule);
    }

    // ===================================================================================

    private Vehicule getVehicule(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ENTITE_VEHICULE, id));
    }

    private void validerTarif(BigDecimal tarifJournalier) {
        if (tarifJournalier != null && tarifJournalier.signum() < 0) {
            throw new IllegalArgumentException("Le tarif journalier ne peut pas être négatif");
        }
    }

    private Agence resoudreAgence(Long agenceId) {
        if (agenceId == null) {
            throw new IllegalArgumentException("L'agence du véhicule est obligatoire");
        }
        return agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agenceId));
    }
}
