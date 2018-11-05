package com.karaush.demo.repositories;

import com.karaush.demo.models.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query(value = "select * from records m order by m.created desc", nativeQuery = true)
    List<Record> fetchAllSortedByDate();

    @Modifying
    @Query(value = " delete from records where id in (select id from records m order by m.created desc offset ?1)", nativeQuery = true)
    void dropLast(Integer offset);


}
