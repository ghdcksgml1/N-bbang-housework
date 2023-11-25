package com.heachi.mysql.define.store.repository;

import com.heachi.mysql.define.store.Item;

public interface ItemRepositoryCustom {
    public Item findByItemIdJoinFetchUser(Long itemId);
}
