package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Agence;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.exception.ResourceNotFoundException;
import tn.esprit.tpautoloc.repository.IAgenceRepository;
import tn.esprit.tpautoloc.repository.IClientRepository;

import java.time.LocalDate;
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

@SpringBootTest
class TpAutolocApplicationTests {

    @Autowired
    private IAgenceRepository agenceRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Test
    void contextLoads() {
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
        System.out.println("Agence enregistrée : " + agenceSauvegardee.getIdAgence()
                + " - " + agenceSauvegardee.getNom());
        assertNotNull(agenceSauvegardee.getIdAgence());

        // findAll()
        List<Agence> toutesLesAgences = agenceRepository.findAll();
        System.out.println("Nombre total d'agences : " + toutesLesAgences.size());
        toutesLesAgences.forEach(a -> System.out.println(" - " + a.getNom()));

        // findById()
        Optional<Agence> agenceTrouvee = agenceRepository.findById(agenceSauvegardee.getIdAgence());
        assertTrue(agenceTrouvee.isPresent());
        System.out.println("Agence trouvée par ID : " + agenceTrouvee.get().getNom());

        // existsById()
        boolean existe = agenceRepository.existsById(agenceSauvegardee.getIdAgence());
        assertTrue(existe);
        System.out.println("L'agence existe : " + existe);

        // count()
        long nombreAgences = agenceRepository.count();
        System.out.println("Nombre d'agences en base : " + nombreAgences);

        // deleteById()
        agenceRepository.deleteById(agenceSauvegardee.getIdAgence());
        boolean existeApresSuppression = agenceRepository.existsById(agenceSauvegardee.getIdAgence());
        assertFalse(existeApresSuppression);
        System.out.println("L'agence existe après suppression : " + existeApresSuppression);
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
        client1.setDateInscription(LocalDate.now());

        Client client2 = new Client();
        client2.setNom("Trabelsi");
        client2.setPrenom("Yassine");
        client2.setEmail("yassine.trabelsi@email.com");
        client2.setTelephone("98333444");
        client2.setNumPermis("TN789012");
        client2.setDateInscription(LocalDate.now());

        // save()
        Client client1Sauvegarde = clientRepository.save(client1);
        Client client2Sauvegarde = clientRepository.save(client2);
        System.out.println("Client 1 enregistré : " + client1Sauvegarde.getIdClient()
                + " - " + client1Sauvegarde.getNom());
        System.out.println("Client 2 enregistré : " + client2Sauvegarde.getIdClient()
                + " - " + client2Sauvegarde.getNom());
        assertNotNull(client1Sauvegarde.getIdClient());
        assertNotNull(client2Sauvegarde.getIdClient());

        // findAll()
        List<Client> tousLesClients = clientRepository.findAll();
        System.out.println("Nombre total de clients : " + tousLesClients.size());
        tousLesClients.forEach(c -> System.out.println(" - " + c.getNom() + " " + c.getPrenom()));

        // findById()
        Optional<Client> clientTrouve = clientRepository.findById(client1Sauvegarde.getIdClient());
        assertTrue(clientTrouve.isPresent());
        System.out.println("Client trouvé par ID : " + clientTrouve.get().getNom());

        // existsById() avec un identifiant existant
        boolean existeExistant = clientRepository.existsById(client1Sauvegarde.getIdClient());
        assertTrue(existeExistant);
        System.out.println("Le client (ID existant) existe : " + existeExistant);

        // existsById() avec un identifiant inexistant
        boolean existeInexistant = clientRepository.existsById(999999L);
        assertFalse(existeInexistant);
        System.out.println("Le client (ID inexistant) existe : " + existeInexistant);

        // count()
        long nombreClients = clientRepository.count();
        System.out.println("Nombre de clients en base : " + nombreClients);
    }
    @Autowired
    private IVehiculeRepository vehiculeRepository;

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
        System.out.println("Véhicule enregistré : " + vehiculeSauvegarde.getIdVehicule()
                + " - " + vehiculeSauvegarde.getMarque() + " " + vehiculeSauvegarde.getModele());
        assertNotNull(vehiculeSauvegarde.getIdVehicule());

        // findAll()
        List<Vehicule> tousLesVehicules = vehiculeRepository.findAll();
        System.out.println("Nombre total de véhicules : " + tousLesVehicules.size());
        tousLesVehicules.forEach(v -> System.out.println(" - " + v.getMarque() + " " + v.getModele()));

        // count()
        long nombreVehicules = vehiculeRepository.count();
        System.out.println("Nombre de véhicules en base : " + nombreVehicules);

        // Modification d'une propriété puis save()
        vehiculeSauvegarde.setStatut(StatutVehicule.LOUE);
        Vehicule vehiculeModifie = vehiculeRepository.save(vehiculeSauvegarde);
        System.out.println("Statut mis à jour : " + vehiculeModifie.getStatut());
        assertEquals(StatutVehicule.LOUE, vehiculeModifie.getStatut());

        // deleteById()
        vehiculeRepository.deleteById(vehiculeModifie.getIdVehicule());
        boolean existeApresSuppression = vehiculeRepository.existsById(vehiculeModifie.getIdVehicule());
        assertFalse(existeApresSuppression);
        System.out.println("Le véhicule existe après suppression : " + existeApresSuppression);
    }
    @Autowired
    private IEmployeRepository employeRepository;

    @Test
    void testEmployeRepository() {
        // Création d'un employé
        Employe employe = new Employe();
        employe.setNom("Khemiri");
        employe.setPrenom("Sami");
        employe.setRole(RoleEmploye.AGENT);

        // save()
        Employe employeSauvegarde = employeRepository.save(employe);
        System.out.println("Employé enregistré : " + employeSauvegarde.getIdEmploye()
                + " - " + employeSauvegarde.getNom());
        assertNotNull(employeSauvegarde.getIdEmploye());

        // findAll()
        List<Employe> tousLesEmployes = employeRepository.findAll();
        System.out.println("Nombre total d'employés : " + tousLesEmployes.size());
        tousLesEmployes.forEach(e -> System.out.println(" - " + e.getNom() + " " + e.getPrenom()));

        // existsById()
        boolean existe = employeRepository.existsById(employeSauvegarde.getIdEmploye());
        assertTrue(existe);
        System.out.println("L'employé existe : " + existe);

        // deleteById()
        employeRepository.deleteById(employeSauvegarde.getIdEmploye());
        boolean existeApresSuppression = employeRepository.existsById(employeSauvegarde.getIdEmploye());
        assertFalse(existeApresSuppression);
        System.out.println("L'employé existe après suppression : " + existeApresSuppression);
    }

    @Autowired
    private IMaintenanceRepository maintenanceRepository;

    @Test
    void testMaintenanceRepository() {
        // Création d'une maintenance
        Maintenance maintenance = new Maintenance();
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDateFin(LocalDate.now().plusDays(3));
        maintenance.setDescription("Vidange et contrôle freins");

        // save()
        Maintenance maintenanceSauvegardee = maintenanceRepository.save(maintenance);
        System.out.println("Maintenance enregistrée : " + maintenanceSauvegardee.getIdMaintenance()
                + " - " + maintenanceSauvegardee.getDescription());
        assertNotNull(maintenanceSauvegardee.getIdMaintenance());

        // findById()
        Optional<Maintenance> maintenanceTrouvee = maintenanceRepository.findById(maintenanceSauvegardee.getIdMaintenance());
        assertTrue(maintenanceTrouvee.isPresent());
        System.out.println("Maintenance trouvée par ID : " + maintenanceTrouvee.get().getDescription());

        // existsById()
        boolean existe = maintenanceRepository.existsById(maintenanceSauvegardee.getIdMaintenance());
        assertTrue(existe);
        System.out.println("La maintenance existe : " + existe);

        // deleteById()
        maintenanceRepository.deleteById(maintenanceSauvegardee.getIdMaintenance());
        boolean existeApresSuppression = maintenanceRepository.existsById(maintenanceSauvegardee.getIdMaintenance());
        assertFalse(existeApresSuppression);
        System.out.println("La maintenance existe après suppression : " + existeApresSuppression);
    }

    @Autowired
    private IEquipementRepository equipementRepository;

    @Test
    void testEquipementRepository() {
        // Création d'un équipement
        Equipement equipement = new Equipement();
        equipement.setLibelle("GPS");

        // save()
        Equipement equipementSauvegarde = equipementRepository.save(equipement);
        System.out.println("Équipement enregistré : " + equipementSauvegarde.getIdEquipement()
                + " - " + equipementSauvegarde.getLibelle());
        assertNotNull(equipementSauvegarde.getIdEquipement());

        // findAll()
        List<Equipement> tousLesEquipements = equipementRepository.findAll();
        System.out.println("Nombre total d'équipements : " + tousLesEquipements.size());
        tousLesEquipements.forEach(e -> System.out.println(" - " + e.getLibelle()));

        // existsById()
        boolean existe = equipementRepository.existsById(equipementSauvegarde.getIdEquipement());
        assertTrue(existe);
        System.out.println("L'équipement existe : " + existe);

        // deleteById()
        equipementRepository.deleteById(equipementSauvegarde.getIdEquipement());
        boolean existeApresSuppression = equipementRepository.existsById(equipementSauvegarde.getIdEquipement());
        assertFalse(existeApresSuppression);
        System.out.println("L'équipement existe après suppression : " + existeApresSuppression);
    }
    @Autowired
    private IAgenceService agenceService;

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
        System.out.println("Agence créée (via service) : " + agenceCreee.getId()
                + " - " + agenceCreee.getNom());
        assertNotNull(agenceCreee.getId());

        // findAll()
        List<AgenceDTO> toutesLesAgences = agenceService.findAll();
        System.out.println("Nombre total d'agences : " + toutesLesAgences.size());
        toutesLesAgences.forEach(a -> System.out.println(" - " + a.getNom()));

        // findById()
        AgenceDTO agenceTrouvee = agenceService.findById(agenceCreee.getId());
        assertNotNull(agenceTrouvee);
        System.out.println("Agence trouvée par ID : " + agenceTrouvee.getNom());

        // "count" via findAll().size() (le service ne expose pas count())
        System.out.println("Nombre d'agences en base : " + agenceService.findAll().size());

        // update()
        agenceCreee.setVille("Sfax Centre");
        AgenceDTO agenceModifiee = agenceService.update(agenceCreee.getId(), agenceCreee);
        System.out.println("Ville mise à jour : " + agenceModifiee.getVille());
        assertEquals("Sfax Centre", agenceModifiee.getVille());

        // deleteById()
        agenceService.deleteById(agenceCreee.getId());
        assertThrows(ResourceNotFoundException.class,
                () -> agenceService.findById(agenceCreee.getId()));
        System.out.println("L'agence a bien été supprimée (ResourceNotFoundException confirmée)");
    }
    @Autowired
    private IClientService clientService;

    @Test
    void testClientService() {
        // Création de deux clients (DTO)
        ClientDTO client1 = ClientDTO.builder()
                .nom("Jlassi")
                .prenom("Nour")
                .email("nour.jlassi@email.com")
                .telephone("98555666")
                .numPermis("TN555666")
                .dateInscription(LocalDate.now())
                .build();

        ClientDTO client2 = ClientDTO.builder()
                .nom("Gharbi")
                .prenom("Karim")
                .email("karim.gharbi@email.com")
                .telephone("98777888")
                .numPermis("TN777888")
                .dateInscription(LocalDate.now())
                .build();

        // creer()
        ClientDTO client1Cree = clientService.creer(client1);
        ClientDTO client2Cree = clientService.creer(client2);
        System.out.println("Client 1 créé (via service) : " + client1Cree.getId()
                + " - " + client1Cree.getNom());
        System.out.println("Client 2 créé (via service) : " + client2Cree.getId()
                + " - " + client2Cree.getNom());

        // findAll()
        List<ClientDTO> tousLesClients = clientService.findAll();
        System.out.println("Nombre total de clients : " + tousLesClients.size());

        // findById()
        ClientDTO clientTrouve = clientService.findById(client1Cree.getId());
        assertNotNull(clientTrouve);
        System.out.println("Client trouvé par ID : " + clientTrouve.getNom());

        // "count"
        System.out.println("Nombre de clients en base : " + clientService.findAll().size());

        // deleteById() du premier client
        clientService.deleteById(client1Cree.getId());
        assertThrows(ResourceNotFoundException.class,
                () -> clientService.findById(client1Cree.getId()));
        System.out.println("Le client 1 a bien été supprimé");

        // nettoyage du second client créé pour ce test
        clientService.deleteById(client2Cree.getId());
    }
    @Autowired
    private IVehiculeService vehiculeService;

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
        System.out.println("Véhicule créé (via service) : " + vehiculeCree.getId()
                + " - " + vehiculeCree.getMarque() + " " + vehiculeCree.getModele());
        assertNotNull(vehiculeCree.getId());
        assertEquals(agence.getId(), vehiculeCree.getAgenceId());

        // findAll()
        List<VehiculeDTO> tousLesVehicules = vehiculeService.findAll();
        System.out.println("Nombre total de véhicules : " + tousLesVehicules.size());

        // findById()
        VehiculeDTO vehiculeTrouve = vehiculeService.findById(vehiculeCree.getId());
        assertNotNull(vehiculeTrouve);
        System.out.println("Véhicule trouvé par ID : " + vehiculeTrouve.getMarque());

        // "count"
        System.out.println("Nombre de véhicules en base : " + vehiculeService.findAll().size());

        // deleteById()
        vehiculeService.deleteById(vehiculeCree.getId());
        assertThrows(ResourceNotFoundException.class,
                () -> vehiculeService.findById(vehiculeCree.getId()));
        System.out.println("Le véhicule a bien été supprimé");

        // nettoyage de l'agence créée pour ce test
        agenceService.deleteById(agence.getId());
    }

}
