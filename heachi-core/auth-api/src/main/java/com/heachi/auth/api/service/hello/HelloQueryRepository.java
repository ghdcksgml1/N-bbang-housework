package com.heachi.auth.api.service.hello;


import com.heachi.mysql.define.hello.Hello;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.heachi.mysql.define.hello.QHello.hello1;

@Repository
@RequiredArgsConstructor
public class HelloQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Hello find() {
        return (Hello) queryFactory.from(hello1)
                .fetchOne();
    }

}
