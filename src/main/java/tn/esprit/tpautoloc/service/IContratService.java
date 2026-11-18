package tn.esprit.tpautoloc.service;

import java.util.List;
import tn.esprit.tpautoloc.web.dto.ContratDTO;

public interface IContratService {
    ContratDTO creer(ContratDTO dto);
    ContratDTO findById(Long id);
    List<ContratDTO> findAll();
    ContratDTO update(Long id, ContratDTO dto);
    void deleteById(Long id);
}
