package com.heachi.mysql.define.housework.period;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHouseworkPeriod is a Querydsl query type for HouseworkPeriod
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHouseworkPeriod extends EntityPathBase<HouseworkPeriod> {

    private static final long serialVersionUID = 424739304L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHouseworkPeriod houseworkPeriod = new QHouseworkPeriod("houseworkPeriod");

    public final com.heachi.mysql.define.QBaseEntity _super = new com.heachi.mysql.define.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final DatePath<java.util.Date> date = createDate("date", java.util.Date.class);

    public final TimePath<java.util.Date> endTime = createTime("endTime", java.util.Date.class);

    public final com.heachi.mysql.define.housework.info.QHouseworkInfo houseworkInfo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final EnumPath<com.heachi.mysql.define.housework.period.constant.HouseworkPeriodType> type = createEnum("type", com.heachi.mysql.define.housework.period.constant.HouseworkPeriodType.class);

    public QHouseworkPeriod(String variable) {
        this(HouseworkPeriod.class, forVariable(variable), INITS);
    }

    public QHouseworkPeriod(Path<? extends HouseworkPeriod> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHouseworkPeriod(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHouseworkPeriod(PathMetadata metadata, PathInits inits) {
        this(HouseworkPeriod.class, metadata, inits);
    }

    public QHouseworkPeriod(Class<? extends HouseworkPeriod> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.houseworkInfo = inits.isInitialized("houseworkInfo") ? new com.heachi.mysql.define.housework.info.QHouseworkInfo(forProperty("houseworkInfo"), inits.get("houseworkInfo")) : null;
    }

}

