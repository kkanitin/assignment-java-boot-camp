package com.example.skooldio.repository;

import com.example.skooldio.entity.Address;
import com.example.skooldio.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Address a SET a.houseNo = :houseNo , " +
            "a.buildingName = :buildingName , a.floor = :floor ,  " +
            "a.village = :village , a.soi = :soi , a.road = :road , " +
            "a.khet = :khet , a.kwang = :kwang , a.priority = :priority ," +
            "a.province = :province , a.postCode = :postCode WHERE a.id = :id")
    void updateExceptUserId(@Param("id") Long id,
                            @Param("houseNo") String houseNo,
                            @Param("buildingName") String buildingName,
                            @Param("floor") String floor,
                            @Param("village") String village,
                            @Param("soi") String soi,
                            @Param("road") String road,
                            @Param("khet") String khet,
                            @Param("kwang") String kwang,
                            @Param("province") String province,
                            @Param("postCode") int postCode,
                            @Param("priority") int priority);

    @Query("SELECT a from Address a where a.user = :userId")
    List<Address> getByUserId(@Param("userId") User user, Pageable pageable);

    @Query("SELECT COUNT(a) from Address a where a.user = :userId")
    int countByUserId(@Param("userId") User user);
}
