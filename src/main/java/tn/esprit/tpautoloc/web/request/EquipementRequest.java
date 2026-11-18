package tn.esprit.tpautoloc.web.request;

import tn.esprit.tpautoloc.domain.Equipement;

/**
 * Corps de requete pour creer / modifier un equipement (correction SonarLint java:S4684).
 * Seuls les champs modifiables par le client sont exposes : l identifiant ne peut pas etre force.
 */
public record EquipementRequest(String libelle) {

    public Equipement toEntity() {
        Equipement equipement = new Equipement();
        equipement.setLibelle(libelle);
        return equipement;
    }
}
