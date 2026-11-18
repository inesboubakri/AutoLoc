package tn.esprit.tpautoloc.web.request;

import java.time.LocalDate;
import tn.esprit.tpautoloc.domain.Maintenance;
import tn.esprit.tpautoloc.domain.Vehicule;

/**
 * Corps de requete pour creer / modifier une maintenance (correction SonarLint java:S4684).
 * Meme format JSON qu avant : { "dateDebut", "dateFin", "description", "vehicule": { "idVehicule" } }.
 */
public record MaintenanceRequest(LocalDate dateDebut, LocalDate dateFin, String description,
                                 VehiculeRef vehicule) {

    /** Reference vers le vehicule : seul l identifiant est accepte. */
    public record VehiculeRef(Long idVehicule) {
    }

    public Maintenance toEntity() {
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(dateDebut);
        maintenance.setDateFin(dateFin);
        maintenance.setDescription(description);
        if (vehicule != null) {
            Vehicule ref = new Vehicule();
            ref.setIdVehicule(vehicule.idVehicule());
            maintenance.setVehicule(ref);
        }
        return maintenance;
    }
}
