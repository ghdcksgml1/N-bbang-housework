package com.heachi.mysql.define.store.repository;

import com.heachi.mysql.define.store.Item;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.heachi.mysql.define.store.QItem.item;
import static com.heachi.mysql.define.user.QUser.user;

@Component
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public Item findByItemIdJoinFetchUser(Long itemId) {
        return queryFactory.selectFrom(item)
                .innerJoin(item.user, user).fetchJoin()
                .where(item.id.eq(itemId))
                .fetchOne();
    }
}
