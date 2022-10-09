package sample.data.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sample.data.jpa.domain.Client;
import sample.data.jpa.domain.Prof;

import java.util.Optional;

// Imports ...

@Repository
public interface ProfDao extends JpaRepository<Prof, Long> {

  @Query("SELECT u FROM User u WHERE u.email = :email")
  Optional<Prof> findByEmail(@Param("email") String email);


}