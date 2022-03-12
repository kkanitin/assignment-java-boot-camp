package com.example.skooldio.repository;

import com.example.skooldio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> getByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
    void updateExceptUsername(@Param("id") Long id, @Param("name") String name);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.username = :username , u.name = :name WHERE id = :id")
    void update(@Param("id") Long id, @Param("username") String username, @Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
    Optional<User> login(@Param("username") String user, @Param("password") String pass);
}
