package com.example.skooldio.repository;

import com.example.skooldio.entity.User;
import com.example.skooldio.entity.UserCard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserCardRepository extends JpaRepository<UserCard, Long> {

    @Query("SELECT u FROM UserCard u WHERE u.user = :userId")
    Optional<List<UserCard>> listByUserId(@Param("userId") User userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE UserCard u SET u.cardNo = :cardNo , u.cardType = :cardType , u.ccvOrCvv = :ccvOrCvv , u.expireMonth = :expireMonth , u.expireYear = :expireYear WHERE u.id = :id")
    void updateCard(Long id, @Param("cardNo") String cardNo, @Param("cardType") String cardType,
                    @Param("ccvOrCvv") String ccvOrCvv, @Param("expireMonth") String expireMonth, @Param("expireYear") String expireYear);
}
