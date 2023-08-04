package com.heachi.mysql.define.hello.repository;


import com.heachi.mysql.define.hello.Hello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HelloRepository extends JpaRepository<Hello, Long> {

    @Query(value = "select * from Hello h order by h.id desc limit 1", nativeQuery = true)
    Hello findHelloOrderByDescOne();

}
