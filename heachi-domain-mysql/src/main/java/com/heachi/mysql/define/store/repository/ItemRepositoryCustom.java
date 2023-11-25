package com.heachi.mysql.define.store.repository;

import com.heachi.mysql.define.store.Item;

import java.util.Optional;

public interface ItemRepositoryCustom {
    public Optional<Item> findByItemIdJoinFetchUser(Long itemId);
}
