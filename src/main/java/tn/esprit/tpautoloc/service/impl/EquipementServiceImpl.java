package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Equipement;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IEquipementRepository;
import tn.esprit.tpautoloc.service.IEquipementService;
import tn.esprit.tpautoloc.web.dto.EquipementDTO;
import tn.esprit.tpautoloc.web.mapper.EquipementMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;
    private final EquipementMapper equipementMapper;

    @Override
    public EquipementDTO creer(EquipementDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException("Un nouvel équipement ne doit pas avoir d'identifiant");
        }
        validerLibelle(dto);
        Equipement equipement = equipementMapper.toEntity(dto);
        Equipement saved = equipementRepository.save(equipement);
        return equipementMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipementDTO findById(Long id) {
        return equipementMapper.toDto(getEquipement(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipementDTO> findAll() {
        return equipementRepository.findAll().stream()
                .map(equipementMapper::toDto)
                .toList();
    }

    @Override
    public EquipementDTO update(Long id, EquipementDTO dto) {
        validerLibelle(dto);
        Equipement existant = getEquipement(id);
        existant.setLibelle(dto.getLibelle());
        return equipementMapper.toDto(equipementRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipement", id);
        }
        equipementRepository.deleteById(id);
    }

    private Equipement getEquipement(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
    }

    private void validerLibelle(EquipementDTO dto) {
        if (dto.getLibelle() == null || dto.getLibelle().isBlank()) {
            throw new IllegalArgumentException("Le libellé de l'équipement est obligatoire");
        }
    }
}
