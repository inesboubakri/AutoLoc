package tn.esprit.tpautoloc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IClientRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import tn.esprit.tpautoloc.domain.Vehicule;
import tn.esprit.tpautoloc.repository.IVehiculeRepository;
import tn.esprit.tpautoloc.domain.enums.CategorieVehicule;
import tn.esprit.tpautoloc.domain.enums.StatutVehicule;
import java.math.BigDecimal;
import tn.esprit.tpautoloc.domain.Employe;
import tn.esprit.tpautoloc.domain.Maintenance;
import tn.esprit.tpautoloc.domain.Equipement;
import tn.esprit.tpautoloc.domain.enums.RoleEmploye;
import tn.esprit.tpautoloc.repository.IEmployeRepository;
import tn.esprit.tpautoloc.repository.IMaintenanceRepository;
import tn.esprit.tpautoloc.repository.IEquipementRepository;
import tn.esprit.tpautoloc.service.IAgenceService;
import tn.esprit.tpautoloc.service.IClientService;
import tn.esprit.tpautoloc.service.IVehiculeService;
import tn.esprit.tpautoloc.web.dto.AgenceDTO;
import tn.esprit.tpautoloc.web.dto.ClientDTO;
import tn.esprit.tpautoloc.web.dto.VehiculeDTO;

@Slf4j
@SpringBootTest
class TpAutolocApplicationTests {

    private final IAgenceRepository agenceRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IEmployeRepository employeRepository;
    private final IMaintenanceRepository maintenanceRepository;
    private final IEquipementRepository equipementRepository;
    private final IAgenceService agenceService;
    private final IClientService clientService;
    private final IVehiculeService vehiculeService;

    @Autowired
    TpAutolocApplicationTests(IAgenceRepository agenceRepository,
                              IClientRepository clientRepository,
                              IVehiculeRepository vehiculeRepository,
                              IEmployeRepository employeRepository,
                              IMaintenanceRepository maintenanceRepository,
                              IEquipementRepository equipementRepository,
                              IAgenceService agenceService,
                              IClientService clientService,
                              IVehiculeService vehiculeService) {
        this.agenceRepository = agenceRepository;
        this.clientRepository = clientRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.employeRepository = employeRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.equipementRepository = equipementRepository;
        this.agenceService = agenceService;
        this.clientService = clientService;
        this.vehiculeService = vehiculeService;
    }

    @Test
    void contextLoads() {
        // Vide volontairement : ce test vérifie seulement que le contexte Spring démarre sans erreur.
    }

    @Test
    void testAgenceRepository() {
        // Création d'une agence
        Agence agence = new Agence();
        agence.setNom("Agence Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("12 Avenue Habib Bourguiba");
        agence.setTelephone("71123456");

        // save()
        Agence agenceSauvegardee = agenceRepository.save(agence);
        log.info("Agence enregistrée : {} - {}", agenceSauvegardee.getIdAgence(), agenceSauvegardee.getNom());
        assertNotNull(agenceSauvegardee.getIdAgence());

        // findAll()
        List<Agence> toutesLesAgences = agenceRepository.findAll();
        log.info("Nombre total d'agences : {}", toutesLesAgences.size());
        toutesLesAgences.forEach(a -> log.info(" - {}", a.getNom()));

        // findById()
        Optional<Agence> agenceTrouvee = agenceRepository.findById(agenceSauvegardee.getIdAgence());
        assertTrue(agenceTrouvee.isPresent());
        log.info("Agence trouvée par ID : {}", agenceTrouvee.get().getNom());

        // existsById()
        boolean existe = agenceRepository.existsById(agenceSauvegardee.getIdAgence());
        assertTrue(existe);
        log.info("L'agence existe : {}", existe);

        // count()
        long nombreAgences = agenceRepository.count();
        log.info("Nombre d'agences en base : {}", nombreAgences);

        // deleteById()
        agenceRepository.deleteById(agenceSauvegardee.getIdAgence());
        boolean existeApresSuppression = agenceRepository.existsById(agenceSauvegardee.getIdAgence());
        assertFalse(existeApresSuppression);
        log.info("L'agence existe après suppression : {}", existeApresSuppression);
    }

    @Test
    void testClientRepository() {
        // Création de deux clients
        Client client1 = new Client();
        client1.setNom("Ben Salah");
        client1.setPrenom("Amira");
        client1.setEmail("amira.bensalah@email.com");
        client1.setTelephone("98111222");
        client1.setNumPermis("TN123456");
        client1.setDateInscription(LocalDate.now(ZoneId.systemDefault()));

        Client client2 = new Client();
        client2.setNom("Trabelsi");
        client2.setPrenom("Yassine");
        client2.setEmail("yassine.trabelsi@email.com");
        client2.setTelephone("98333444");
        client2.setNumPermis("TN789012");
        client2.setDateInscription(LocalDate.now(ZoneId.systemDefault()));

        // save()
        Client client1Sauvegarde = clientRepository.save(client1);
        Client client2Sauvegarde = clientRepository.save(client2);
        log.info("Client 1 enregistré : {} - {}", client1Sauvegarde.getIdClient(), client1Sauvegarde.getNom());
        log.info("Client 2 enregistré : {} - {}", client2Sauvegarde.getIdClient(), client2Sauvegarde.getNom());
        assertNotNull(client1Sauvegarde.getIdClient());
        assertNotNull(client2Sauvegarde.getIdClient());

        // findAll()
        List<Client> tousLesClients = clientRepository.findAll();
        log.info("Nombre total de clients : {}", tousLesClients.size());
        tousLesClients.forEach(c -> log.info(" - {} {}", c.getNom(), c.getPrenom()));

        // findById()
        Optional<Client> clientTrouve = clientRepository.findById(client1Sauvegarde.getIdClient());
        assertTrue(clientTrouve.isPresent());
        log.info("Client trouvé par ID : {}", clientTrouve.get().getNom());

        // existsById() avec un identifiant existant
        boolean existeExistant = clientRepository.existsById(client1Sauvegarde.getIdClient());
        assertTrue(existeExistant);
        log.info("Le client (ID existant) existe : {}", existeExistant);

        // existsById() avec un identifiant inexistant
        boolean existeInexistant = clientRepository.existsById(999999L);
        assertFalse(existeInexistant);
        log.info("Le client (ID inexistant) existe : {}", existeInexistant);

        // count()
        long nombreClients = clientRepository.count();
        log.info("Nombre de clients en base : {}", nombreClients);
    }
    @Test
    void testVehiculeRepository() {
        // Création d'un véhicule
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation("123 TUN 4567");
        vehicule.setMarque("Renault");
        vehicule.setModele("Clio");
        vehicule.setCategorie(CategorieVehicule.CITADINE);
        vehicule.setTarifJournalier(new BigDecimal("80.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);

        // save()
        Vehicule vehiculeSauvegarde = vehiculeRepository.save(vehicule);
        log.info("Véhicule enregistré : {} - {} {}", vehiculeSauvegarde.getIdVehicule(), vehiculeSauvegarde.getMarque(), vehiculeSauvegarde.getModele());
        assertNotNull(vehiculeSauvegarde.getIdVehicule());

        // findAll()
        List<Vehicule> tousLesVehicules = vehiculeRepository.findAll();
        log.info("Nombre total de véhicules : {}", tousLesVehicules.size());
        tousLesVehicules.forEach(v -> log.info(" - {} {}", v.getMarque(), v.getModele()));

        // count()
        long nombreVehicules = vehiculeRepository.count();
        log.info("Nombre de véhicules en base : {}", nombreVehicules);

        // Modification d'une propriété puis save()
        vehiculeSauvegarde.setStatut(StatutVehicule.LOUE);
        Vehicule vehiculeModifie = vehiculeRepository.save(vehiculeSauvegarde);
        log.info("Statut mis à jour : {}", vehiculeModifie.getStatut());
        assertEquals(StatutVehicule.LOUE, vehiculeModifie.getStatut());

        // deleteById()
        vehiculeRepository.deleteById(vehiculeModifie.getIdVehicule());
        boolean existeApresSuppression = vehiculeRepository.existsById(vehiculeModifie.getIdVehicule());
        assertFalse(existeApresSuppression);
        log.info("Le véhicule existe après suppression : {}", existeApresSuppression);
    }
    @Test
    void testEmployeRepository() {
        // Création d'un employé
        Employe employe = new Employe();
        employe.setNom("Khemiri");
        employe.setPrenom("Sami");
        employe.setRole(RoleEmploye.AGENT);

        // save()
        Employe employeSauvegarde = employeRepository.save(employe);
        log.info("Employé enregistré : {} - {}", employeSauvegarde.getIdEmploye(), employeSauvegarde.getNom());
        assertNotNull(employeSauvegarde.getIdEmploye());

        // findAll()
        List<Employe> tousLesEmployes = employeRepository.findAll();
        log.info("Nombre total d'employés : {}", tousLesEmployes.size());
        tousLesEmployes.forEach(e -> log.info(" - {} {}", e.getNom(), e.getPrenom()));

        // existsById()
        boolean existe = employeRepository.existsById(employeSauvegarde.getIdEmploye());
        assertTrue(existe);
        log.info("L'employé existe : {}", existe);

        // deleteById()
        employeRepository.deleteById(employeSauvegarde.getIdEmploye());
        boolean existeApresSuppression = employeRepository.existsById(employeSauvegarde.getIdEmploye());
        assertFalse(existeApresSuppression);
        log.info("L'employé existe après suppression : {}", existeApresSuppression);
    }

    @Test
    void testMaintenanceRepository() {
        // Création d'une maintenance
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.now(ZoneId.systemDefault()));
        maintenance.setDateFin(LocalDate.now(ZoneId.systemDefault()).plusDays(3));
        maintenance.setDescription("Vidange et contrôle freins");

        // save()
        Maintenance maintenanceSauvegardee = maintenanceRepository.save(maintenance);
        log.info("Maintenance enregistrée : {} - {}", maintenanceSauvegardee.getIdMaintenance(), maintenanceSauvegardee.getDescription());
        assertNotNull(maintenanceSauvegardee.getIdMaintenance());

        // findById()
        Optional<Maintenance> maintenanceTrouvee = maintenanceRepository.findById(maintenanceSauvegardee.getIdMaintenance());
        assertTrue(maintenanceTrouvee.isPresent());
        log.info("Maintenance trouvée par ID : {}", maintenanceTrouvee.get().getDescription());

        // existsById()
        boolean existe = maintenanceRepository.existsById(maintenanceSauvegardee.getIdMaintenance());
        assertTrue(existe);
        log.info("La maintenance existe : {}", existe);

        // deleteById()
        maintenanceRepository.deleteById(maintenanceSauvegardee.getIdMaintenance());
        boolean existeApresSuppression = maintenanceRepository.existsById(maintenanceSauvegardee.getIdMaintenance());
        assertFalse(existeApresSuppression);
        log.info("La maintenance existe après suppression : {}", existeApresSuppression);
    }

    @Test
    void testEquipementRepository() {
        // Création d'un équipement
        Equipement equipement = new Equipement();
        equipement.setLibelle("GPS");

        // save()
        Equipement equipementSauvegarde = equipementRepository.save(equipement);
        log.info("Équipement enregistré : {} - {}", equipementSauvegarde.getIdEquipement(), equipementSauvegarde.getLibelle());
        assertNotNull(equipementSauvegarde.getIdEquipement());

        // findAll()
        List<Equipement> tousLesEquipements = equipementRepository.findAll();
        log.info("Nombre total d'équipements : {}", tousLesEquipements.size());
        tousLesEquipements.forEach(e -> log.info(" - {}", e.getLibelle()));

        // existsById()
        boolean existe = equipementRepository.existsById(equipementSauvegarde.getIdEquipement());
        assertTrue(existe);
        log.info("L'équipement existe : {}", existe);

        // deleteById()
        equipementRepository.deleteById(equipementSauvegarde.getIdEquipement());
        boolean existeApresSuppression = equipementRepository.existsById(equipementSauvegarde.getIdEquipement());
        assertFalse(existeApresSuppression);
        log.info("L'équipement existe après suppression : {}", existeApresSuppression);
    }
    @Test
    void testAgenceService() {
        // Création d'une agence (DTO)
        AgenceDTO agence = AgenceDTO.builder()
                .nom("Agence Sfax Nord")
                .ville("Sfax")
                .adresse("45 Rue de la Liberté")
                .telephone("74222333")
                .build();

        // creer()
        AgenceDTO agenceCreee = agenceService.creer(agence);
        log.info("Agence créée (via service) : {} - {}", agenceCreee.getId(), agenceCreee.getNom());
        assertNotNull(agenceCreee.getId());

        // findAll()
        List<AgenceDTO> toutesLesAgences = agenceService.findAll();
        log.info("Nombre total d'agences : {}", toutesLesAgences.size());
        toutesLesAgences.forEach(a -> log.info(" - {}", a.getNom()));

        // findById()
        AgenceDTO agenceTrouvee = agenceService.findById(agenceCreee.getId());
        assertNotNull(agenceTrouvee);
        log.info("Agence trouvée par ID : {}", agenceTrouvee.getNom());

        // "count" via findAll().size() (le service ne expose pas count())
        log.info("Nombre d'agences en base : {}", agenceService.findAll().size());

        // update()
        agenceCreee.setVille("Sfax Centre");
        AgenceDTO agenceModifiee = agenceService.update(agenceCreee.getId(), agenceCreee);
        log.info("Ville mise à jour : {}", agenceModifiee.getVille());
        assertEquals("Sfax Centre", agenceModifiee.getVille());

        // deleteById()
        agenceService.deleteById(agenceCreee.getId());
        assertThrows(ResourceNotFoundException.class,
                () -> agenceService.findById(agenceCreee.getId()));
        log.info("L'agence a bien été supprimée (ResourceNotFoundException confirmée)");
    }
    @Test
    void testClientService() {
        // Création de deux clients (DTO)
        ClientDTO client1 = ClientDTO.builder()
                .nom("Jlassi")
                .prenom("Nour")
                .email("nour.jlassi@email.com")
                .telephone("98555666")
                .numPermis("TN555666")
                .dateInscription(LocalDate.now(ZoneId.systemDefault()))
                .build();

        ClientDTO client2 = ClientDTO.builder()
                .nom("Gharbi")
                .prenom("Karim")
                .email("karim.gharbi@email.com")
                .telephone("98777888")
                .numPermis("TN777888")
                .dateInscription(LocalDate.now(ZoneId.systemDefault()))
                .build();

        // creer()
        ClientDTO client1Cree = clientService.creer(client1);
        ClientDTO client2Cree = clientService.creer(client2);
        log.info("Client 1 créé (via service) : {} - {}", client1Cree.getId(), client1Cree.getNom());
        log.info("Client 2 créé (via service) : {} - {}", client2Cree.getId(), client2Cree.getNom());

        // findAll()
        List<ClientDTO> tousLesClients = clientService.findAll();
        log.info("Nombre total de clients : {}", tousLesClients.size());

        // findById()
        ClientDTO clientTrouve = clientService.findById(client1Cree.getId());
        assertNotNull(clientTrouve);
        log.info("Client trouvé par ID : {}", clientTrouve.getNom());

        // "count"
        log.info("Nombre de clients en base : {}", clientService.findAll().size());

        // deleteById() du premier client
        clientService.deleteById(client1Cree.getId());
        assertThrows(ResourceNotFoundException.class,
                () -> clientService.findById(client1Cree.getId()));
        log.info("Le client 1 a bien été supprimé");

        // nettoyage du second client créé pour ce test
        clientService.deleteById(client2Cree.getId());
    }
    @Test
    void testVehiculeService() {
        // Un véhicule doit être rattaché à une agence
        AgenceDTO agence = agenceService.creer(AgenceDTO.builder()
                .nom("Agence Test Véhicule").ville("Tunis").build());

        // Création d'un véhicule (DTO)
        VehiculeDTO vehicule = VehiculeDTO.builder()
                .immatriculation("789 TUN 1234")
                .marque("Peugeot")
                .modele("208")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("90.00"))
                .statut(StatutVehicule.DISPONIBLE)
                .agenceId(agence.getId())
                .build();

        // creer()
        VehiculeDTO vehiculeCree = vehiculeService.creer(vehicule);
        log.info("Véhicule créé (via service) : {} - {} {}", vehiculeCree.getId(), vehiculeCree.getMarque(), vehiculeCree.getModele());
        assertNotNull(vehiculeCree.getId());
        assertEquals(agence.getId(), vehiculeCree.getAgenceId());

        // findAll()
        List<VehiculeDTO> tousLesVehicules = vehiculeService.findAll();
        log.info("Nombre total de véhicules : {}", tousLesVehicules.size());

        // findById()
        VehiculeDTO vehiculeTrouve = vehiculeService.findById(vehiculeCree.getId());
        assertNotNull(vehiculeTrouve);
        log.info("Véhicule trouvé par ID : {}", vehiculeTrouve.getMarque());

        // "count"
        log.info("Nombre de véhicules en base : {}", vehiculeService.findAll().size());

        // deleteById()
        vehiculeService.deleteById(vehiculeCree.getId());
        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.findById(vehiculeCree.getId()));
        log.info("Le véhicule a bien été supprimé");

        // nettoyage de l'agence créée pour ce test
        agenceService.deleteById(agence.getId());
    }

}
