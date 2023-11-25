package com.heachi.mysql.define.store.repository;

import com.heachi.mysql.define.store.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

}
