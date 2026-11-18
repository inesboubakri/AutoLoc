package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.service.IAgenceService;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;
import tn.esprit.tpautoloc.web.mapper.AgenceMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final AgenceMapper agenceMapper;

    @Override
    public AgenceDTO creer(AgenceDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle agence ne doit pas avoir d'identifiant");
        }
        validerNom(dto);
        Agence agence = agenceMapper.toEntity(dto);
        Agence saved = agenceRepository.save(agence);
        return agenceMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AgenceDTO findById(Long id) {
        return agenceMapper.toDto(getAgence(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgenceDTO> findAll() {
        return agenceRepository.findAll().stream()
                .map(agenceMapper::toDto)
                .toList();
    }

    @Override
    public AgenceDTO update(Long id, AgenceDTO dto) {
        validerNom(dto);
        Agence existante = getAgence(id);
        existante.setNom(dto.getNom());
        existante.setVille(dto.getVille());
        existante.setAdresse(dto.getAdresse());
        existante.setTelephone(dto.getTelephone());
        return agenceMapper.toDto(agenceRepository.save(existante));
    }

    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        agenceRepository.deleteById(id);
    }

    private Agence getAgence(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    private void validerNom(AgenceDTO dto) {
        if (dto.getNom() == null || dto.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'agence est obligatoire");
        }
    }
}
