package com.gmfs.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemoRepository /*extends JpaRepository<Object, Integer>*/ {
    List<Object> findByDataNumber(String dataNumber);

}
