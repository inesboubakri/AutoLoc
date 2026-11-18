package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Contrat;
import tn.esprit.tpautoloc.domain.Paiement;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IContratRepository;
import tn.esprit.tpautoloc.repository.IPaiementRepository;
import tn.esprit.tpautoloc.service.IPaiementService;
import tn.esprit.tpautoloc.web.dto.PaiementDTO;
import tn.esprit.tpautoloc.web.mapper.PaiementMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;
    private final IContratRepository contratRepository;
    private final PaiementMapper paiementMapper;

    @Override
    public PaiementDTO creer(PaiementDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau paiement ne doit pas avoir d'identifiant");
        }
        Paiement paiement = paiementMapper.toEntity(dto);
        paiement.setContrat(resoudreContrat(dto.getContratId())); // 404 si contrat inexistant
        Paiement saved = paiementRepository.save(paiement);
        return paiementMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementDTO findById(Long id) {
        return paiementMapper.toDto(getPaiement(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementDTO> findAll() {
        return paiementRepository.findAll().stream()
                .map(paiementMapper::toDto)
                .toList();
    }

    @Override
    public PaiementDTO update(Long id, PaiementDTO dto) {
        Paiement existant = getPaiement(id);
        existant.setMontant(dto.getMontant());
        existant.setDatePaiement(dto.getDatePaiement());
        existant.setModePaiement(dto.getModePaiement());
        return paiementMapper.toDto(paiementRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!paiementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paiement", id);
        }
        paiementRepository.deleteById(id);
    }

    private Paiement getPaiement(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
    }

    private Contrat resoudreContrat(Long contratId) {
        if (contratId == null) {
            throw new IllegalArgumentException("Le contrat du paiement est obligatoire");
        }
        return contratRepository.findById(contratId)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", contratId));
    }
}
