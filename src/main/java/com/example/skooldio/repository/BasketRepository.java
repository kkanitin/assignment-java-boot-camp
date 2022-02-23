package com.example.skooldio.repository;

import com.example.skooldio.entity.Basket;
import com.example.skooldio.entity.Product;
import com.example.skooldio.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    @Query("SELECT b from Basket b where b.user = :userId")
    Optional<List<Basket>> listByUserId(@Param("userId") User userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Basket b SET b.product = :productId ,b.quantity = :quantity WHERE b.id = :id")
    void updateExceptUserId(@Param("id") Long id, @Param("productId") Product productId, @Param("quantity") int quantity);

    @Transactional
    @Modifying
    @Query("UPDATE Basket b SET b.quantity = :quantity WHERE b.id = :id")
    void updateQuantity(@Param("id") Long id, @Param("quantity") int quantity);

    @Query("SELECT b from Basket b where b.user = :userId AND b.product = :productId")
    Optional<Basket> getByUserIdAndProductId(@Param("userId") User userId, @Param("productId") Product productId);
}
