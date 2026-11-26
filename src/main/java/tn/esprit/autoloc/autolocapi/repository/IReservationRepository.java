package tn.esprit.autoloc.autolocapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.autolocapi.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateDebutLessThanEqualAndDateFinGreaterThanEqual(
            LocalDate dateFin, LocalDate dateDebut);
}
