package com.heachi.mysql.define.store.repository;

import com.heachi.mysql.define.store.Item;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.heachi.mysql.define.store.QItem.item;
import static com.heachi.mysql.define.user.QUser.user;

@Component
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<Item> findByItemIdJoinFetchUser(Long itemId) {
        return Optional.ofNullable(queryFactory.selectFrom(item)
                .innerJoin(item.user, user).fetchJoin()
                .where(item.id.eq(itemId))
                .fetchOne());
    }
}
