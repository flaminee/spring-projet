package sample.data.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sample.data.jpa.domain.Appointment;
import sample.data.jpa.domain.Client;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentDao extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.prof.id = :id")
    List<Appointment> byIdProf(@Param("id") Long id);

    @Query("SELECT a FROM Appointment a WHERE a.client = :id")
    List<Appointment> byIdClient(@Param("id") Long id);

    @Query("SELECT a FROM Appointment a WHERE a.prof = :id  AND a.client is null ")
    List<Appointment> freeByIdProf(@Param("id") Long id);


}
