package com.heachi.mysql.define.housework.info;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHouseworkInfo is a Querydsl query type for HouseworkInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHouseworkInfo extends EntityPathBase<HouseworkInfo> {

    private static final long serialVersionUID = -1418614910L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHouseworkInfo houseworkInfo = new QHouseworkInfo("houseworkInfo");

    public final com.heachi.mysql.define.QBaseEntity _super = new com.heachi.mysql.define.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final DatePath<java.time.LocalDate> dayDate = createDate("dayDate", java.time.LocalDate.class);

    public final StringPath detail = createString("detail");

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final com.heachi.mysql.define.group.info.QGroupInfo groupInfo;

    public final com.heachi.mysql.define.housework.category.QHouseworkCategory houseworkCategory;

    public final ListPath<com.heachi.mysql.define.housework.member.HouseworkMember, com.heachi.mysql.define.housework.member.QHouseworkMember> houseworkMembers = this.<com.heachi.mysql.define.housework.member.HouseworkMember, com.heachi.mysql.define.housework.member.QHouseworkMember>createList("houseworkMembers", com.heachi.mysql.define.housework.member.HouseworkMember.class, com.heachi.mysql.define.housework.member.QHouseworkMember.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final StringPath monthDate = createString("monthDate");

    public final StringPath title = createString("title");

    public final EnumPath<com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType> type = createEnum("type", com.heachi.mysql.define.housework.info.constant.HouseworkPeriodType.class);

    public final StringPath weekDate = createString("weekDate");

    public QHouseworkInfo(String variable) {
        this(HouseworkInfo.class, forVariable(variable), INITS);
    }

    public QHouseworkInfo(Path<? extends HouseworkInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHouseworkInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHouseworkInfo(PathMetadata metadata, PathInits inits) {
        this(HouseworkInfo.class, metadata, inits);
    }

    public QHouseworkInfo(Class<? extends HouseworkInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupInfo = inits.isInitialized("groupInfo") ? new com.heachi.mysql.define.group.info.QGroupInfo(forProperty("groupInfo"), inits.get("groupInfo")) : null;
        this.houseworkCategory = inits.isInitialized("houseworkCategory") ? new com.heachi.mysql.define.housework.category.QHouseworkCategory(forProperty("houseworkCategory")) : null;
    }

}

