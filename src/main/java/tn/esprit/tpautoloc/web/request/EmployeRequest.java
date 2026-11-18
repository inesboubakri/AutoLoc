package tn.esprit.tpautoloc.web.request;

import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;

/**
 * Corps de requete pour creer / modifier un employe (correction SonarLint java:S4684).
 * Meme format JSON qu avant : { "nom", "prenom", "role", "agence": { "idAgence" } }.
 */
public record EmployeRequest(String nom, String prenom, RoleEmploye role, AgenceRef agence) {

    /** Reference vers l agence : seul l identifiant est accepte. */
    public record AgenceRef(Long idAgence) {
    }

    public Employe toEntity() {
        Employe employe = new Employe();
        employe.setNom(nom);
        employe.setPrenom(prenom);
        employe.setRole(role);
        if (agence != null) {
            Agence ref = new Agence();
            ref.setIdAgence(agence.idAgence());
            employe.setAgence(ref);
        }
        return employe;
    }
}
