package tn.esprit.tpautoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpautoloc.domain.Client;
import tn.esprit.tpautoloc.domain.Reservation;
import tn.esprit.tpautoloc.domain.enums.StatutReservation;
import tn.esprit.tpautoloc.service.IReservationService;
import tn.esprit.tpautoloc.service.observer.ReservationSubject;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceTest {

    // Le service à tester
    private final IReservationService reservationService;
    private final ReservationSubject reservationSubject;

    @Autowired
    ReservationServiceTest(IReservationService reservationService,
                           ReservationSubject reservationSubject) {
        this.reservationService = reservationService;
        this.reservationSubject = reservationSubject;
    }

    private Client creerClient() {
        Client client = new Client();
        client.setNom("Ben Ali");
        client.setPrenom("Sarra");
        client.setEmail("sarra.benali@email.com");
        client.setTelephone("98123456");
        return client;
    }

    @Test
    void testCreerReservation() {
        assertNotNull(reservationSubject);

        // Console attendue :
        // [EMAIL] Confirmation envoyee au client ...
        // [SMS] Confirmation envoyee au <telephone> ...
        Reservation reservation = reservationService.creerReservation(creerClient());

        assertNotNull(reservation);
        assertEquals(StatutReservation.CONFIRMEE, reservation.getStatut());
    }

    @Test
    void testAnnulerReservation() {
        Reservation reservation = reservationService.creerReservation(creerClient());

        // Console attendue :
        // [EMAIL] Annulation notifiee au client ...
        // [SMS] Annulation notifiee au <telephone> ...
        Reservation annulee = reservationService.annulerReservation(reservation);

        assertEquals(StatutReservation.ANNULEE, annulee.getStatut());
    }
}
