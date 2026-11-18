package tn.esprit.tpautoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpautoloc.domain.Maintenance;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IMaintenanceRepository;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.service.IMaintenanceService;
import tn.esprit.tpautoloc.web.dto.MaintenanceDTO;
import tn.esprit.tpautoloc.web.mapper.MaintenanceMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final MaintenanceMapper maintenanceMapper;

    @Override
    public MaintenanceDTO creer(MaintenanceDTO dto) {
        if (dto.getId() != null) {
            throw new IllegalArgumentException("Une nouvelle maintenance ne doit pas avoir d'identifiant");
        }
        validerDates(dto);
        Maintenance maintenance = maintenanceMapper.toEntity(dto);
        maintenance.setVehicule(resoudreVehicule(dto));
        Maintenance saved = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceDTO findById(Long id) {
        return maintenanceMapper.toDto(getMaintenance(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceDTO> findAll() {
        return maintenanceRepository.findAll().stream()
                .map(maintenanceMapper::toDto)
                .toList();
    }

    @Override
    public MaintenanceDTO update(Long id, MaintenanceDTO dto) {
        validerDates(dto);
        Maintenance existant = getMaintenance(id);
        existant.setDateDebut(dto.getDateDebut());
        existant.setDateFin(dto.getDateFin());
        existant.setDescription(dto.getDescription());
        existant.setVehicule(resoudreVehicule(dto));
        return maintenanceMapper.toDto(maintenanceRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance", id);
        }
        maintenanceRepository.deleteById(id);
    }

    private Maintenance getMaintenance(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
    }

    private void validerDates(MaintenanceDTO dto) {
        if (dto.getDateDebut() == null || dto.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates de début et de fin sont obligatoires");
        }
        if (dto.getDateFin().isBefore(dto.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
    }

    private Vehicule resoudreVehicule(MaintenanceDTO dto) {
        if (dto.getVehiculeId() == null) {
            throw new IllegalArgumentException("Le véhicule de la maintenance est obligatoire");
        }
        return vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", dto.getVehiculeId()));
    }
}
