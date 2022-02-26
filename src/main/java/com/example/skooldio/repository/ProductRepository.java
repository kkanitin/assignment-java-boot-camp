package com.example.skooldio.repository;

import com.example.skooldio.entity.Product;
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
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("UPDATE Product p SET p.name = :name , p.detail = :detail , p.quantity = :quantity , p.priceBaht = :priceBaht WHERE p.id = :id")
    void update(@Param("id") Long id, @Param("name") String name, @Param("detail") String detail,
                @Param("quantity") int quantity, @Param("priceBaht") Double priceBaht);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = :quantity WHERE p.id = :id")
    void updateQuantity(@Param("id") Long id, @Param("quantity") int quantity);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Optional<List<Product>> listByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.name LIKE %:name%")
    int countByName(String name);
}
