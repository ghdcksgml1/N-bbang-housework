package com.heachi.mysql.define.housework.save;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHouseworkSave is a Querydsl query type for HouseworkSave
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHouseworkSave extends EntityPathBase<HouseworkSave> {

    private static final long serialVersionUID = -686535968L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHouseworkSave houseworkSave = new QHouseworkSave("houseworkSave");

    public final com.heachi.mysql.define.QBaseEntity _super = new com.heachi.mysql.define.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final com.heachi.mysql.define.group.info.QGroupInfo groupInfo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final StringPath name = createString("name");

    public QHouseworkSave(String variable) {
        this(HouseworkSave.class, forVariable(variable), INITS);
    }

    public QHouseworkSave(Path<? extends HouseworkSave> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHouseworkSave(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHouseworkSave(PathMetadata metadata, PathInits inits) {
        this(HouseworkSave.class, metadata, inits);
    }

    public QHouseworkSave(Class<? extends HouseworkSave> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupInfo = inits.isInitialized("groupInfo") ? new com.heachi.mysql.define.group.info.QGroupInfo(forProperty("groupInfo"), inits.get("groupInfo")) : null;
    }

}

