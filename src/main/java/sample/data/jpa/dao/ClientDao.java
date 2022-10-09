package sample.data.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sample.data.jpa.domain.Client;

import java.util.Optional;

@Repository
public interface ClientDao extends JpaRepository<Client, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<Client> findByEmail(@Param("email") String email);



}