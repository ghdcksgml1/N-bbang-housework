package com.heachi.mysql.define.group.info;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupInfo is a Querydsl query type for GroupInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupInfo extends EntityPathBase<GroupInfo> {

    private static final long serialVersionUID = -204171454L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupInfo groupInfo = new QGroupInfo("groupInfo");

    public final com.heachi.mysql.define.QBaseEntity _super = new com.heachi.mysql.define.QBaseEntity(this);

    public final StringPath bgColor = createString("bgColor");

    public final StringPath colorCode = createString("colorCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath info = createString("info");

    public final StringPath joinCode = createString("joinCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final StringPath name = createString("name");

    public final com.heachi.mysql.define.user.QUser user;

    public QGroupInfo(String variable) {
        this(GroupInfo.class, forVariable(variable), INITS);
    }

    public QGroupInfo(Path<? extends GroupInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupInfo(PathMetadata metadata, PathInits inits) {
        this(GroupInfo.class, metadata, inits);
    }

    public QGroupInfo(Class<? extends GroupInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.heachi.mysql.define.user.QUser(forProperty("user")) : null;
    }

}

