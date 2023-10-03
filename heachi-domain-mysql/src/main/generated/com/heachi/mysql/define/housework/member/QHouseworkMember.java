package com.heachi.mysql.define.housework.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHouseworkMember is a Querydsl query type for HouseworkMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHouseworkMember extends EntityPathBase<HouseworkMember> {

    private static final long serialVersionUID = 375675290L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHouseworkMember houseworkMember = new QHouseworkMember("houseworkMember");

    public final com.heachi.mysql.define.QBaseEntity _super = new com.heachi.mysql.define.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final com.heachi.mysql.define.group.member.QGroupMember groupMember;

    public final com.heachi.mysql.define.housework.info.QHouseworkInfo houseworkInfo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public QHouseworkMember(String variable) {
        this(HouseworkMember.class, forVariable(variable), INITS);
    }

    public QHouseworkMember(Path<? extends HouseworkMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHouseworkMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHouseworkMember(PathMetadata metadata, PathInits inits) {
        this(HouseworkMember.class, metadata, inits);
    }

    public QHouseworkMember(Class<? extends HouseworkMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupMember = inits.isInitialized("groupMember") ? new com.heachi.mysql.define.group.member.QGroupMember(forProperty("groupMember"), inits.get("groupMember")) : null;
        this.houseworkInfo = inits.isInitialized("houseworkInfo") ? new com.heachi.mysql.define.housework.info.QHouseworkInfo(forProperty("houseworkInfo"), inits.get("houseworkInfo")) : null;
    }

}

