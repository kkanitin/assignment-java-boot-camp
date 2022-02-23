package com.example.skooldio.repository;

import com.example.skooldio.entity.Transaction;
import com.example.skooldio.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t from Transaction t where t.user = :userId")
    Optional<List<Transaction>> listByUserId(@Param(value = "userId") User userId, Pageable pageable);

    @Query("SELECT COUNT(t) from Transaction t where t.user = :userId")
    Long countByUserId(@Param(value = "userId") User userId);

    @Transactional
    @Modifying
    @Query("UPDATE Transaction t SET t.status = :status , t.updatedDate = :updatedDate WHERE t.id = :transactionId")
    void updateStatus(@Param(value = "transactionId") Long transaction,
                      @Param(value = "status") String status,
                      @Param(value = "updatedDate") Date updatedDate);

    @Query("SELECT MAX(t.groupNumber) from Transaction t")
    Integer getMaxGroupNumber();

    @Query("SELECT t FROM Transaction t WHERE t.groupNumber = :groupNumber")
    Optional<List<Transaction>> listByGroupNumber(@Param(value = "groupNumber") Integer groupNumber, Pageable pageable);
}
