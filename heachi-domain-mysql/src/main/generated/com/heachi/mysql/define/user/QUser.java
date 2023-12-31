package com.heachi.mysql.define.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1064646248L;

    public static final QUser user = new QUser("user");

    public final com.heachi.mysql.define.QBaseEntity _super = new com.heachi.mysql.define.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final StringPath email = createString("email");

    public final ListPath<com.heachi.mysql.define.group.info.GroupInfo, com.heachi.mysql.define.group.info.QGroupInfo> groupInfoList = this.<com.heachi.mysql.define.group.info.GroupInfo, com.heachi.mysql.define.group.info.QGroupInfo>createList("groupInfoList", com.heachi.mysql.define.group.info.GroupInfo.class, com.heachi.mysql.define.group.info.QGroupInfo.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final StringPath name = createString("name");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath platformId = createString("platformId");

    public final EnumPath<com.heachi.mysql.define.user.constant.UserPlatformType> platformType = createEnum("platformType", com.heachi.mysql.define.user.constant.UserPlatformType.class);

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final BooleanPath pushAlarmYn = createBoolean("pushAlarmYn");

    public final EnumPath<com.heachi.mysql.define.user.constant.UserRole> role = createEnum("role", com.heachi.mysql.define.user.constant.UserRole.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

