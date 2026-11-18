package tn.esprit.tpautoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tn.esprit.tpautoloc.domain.Equipement;
import tn.esprit.tpautoloc.web.dto.EquipementDTO;

@Mapper(componentModel = "spring")
public interface EquipementMapper {

    @Mapping(target = "id", source = "idEquipement")
    EquipementDTO toDto(Equipement equipement);

    @Mapping(target = "idEquipement", source = "id")
    @Mapping(target = "vehicules", ignore = true)
    Equipement toEntity(EquipementDTO dto);
}
