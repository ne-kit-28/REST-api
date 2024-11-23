package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByAddress_HouseAndSurname(int house, String surname);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Modifying
    @Query(value = "DELETE FROM users WHERE house = ?1", nativeQuery = true)
    void deleteByAddress_House(int house);
}
