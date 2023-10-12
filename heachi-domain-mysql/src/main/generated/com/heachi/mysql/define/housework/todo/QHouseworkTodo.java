package com.heachi.mysql.define.housework.todo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHouseworkTodo is a Querydsl query type for HouseworkTodo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHouseworkTodo extends EntityPathBase<HouseworkTodo> {

    private static final long serialVersionUID = -1421769550L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHouseworkTodo houseworkTodo = new QHouseworkTodo("houseworkTodo");

    public final com.heachi.mysql.define.QBaseEntity _super = new com.heachi.mysql.define.QBaseEntity(this);

    public final StringPath category = createString("category");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final DatePath<java.util.Date> date = createDate("date", java.util.Date.class);

    public final StringPath detail = createString("detail");

    public final com.heachi.mysql.define.group.info.QGroupInfo groupInfo;

    public final com.heachi.mysql.define.housework.info.QHouseworkInfo houseworkInfo;

    public final StringPath houseworkMember = createString("houseworkMember");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> index = createNumber("index", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final EnumPath<com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus> status = createEnum("status", com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus.class);

    public final StringPath title = createString("title");

    public final StringPath verificationPhotoURL = createString("verificationPhotoURL");

    public final DateTimePath<java.time.LocalDateTime> verificationTime = createDateTime("verificationTime", java.time.LocalDateTime.class);

    public final StringPath verifierId = createString("verifierId");

    public QHouseworkTodo(String variable) {
        this(HouseworkTodo.class, forVariable(variable), INITS);
    }

    public QHouseworkTodo(Path<? extends HouseworkTodo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHouseworkTodo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHouseworkTodo(PathMetadata metadata, PathInits inits) {
        this(HouseworkTodo.class, metadata, inits);
    }

    public QHouseworkTodo(Class<? extends HouseworkTodo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupInfo = inits.isInitialized("groupInfo") ? new com.heachi.mysql.define.group.info.QGroupInfo(forProperty("groupInfo"), inits.get("groupInfo")) : null;
        this.houseworkInfo = inits.isInitialized("houseworkInfo") ? new com.heachi.mysql.define.housework.info.QHouseworkInfo(forProperty("houseworkInfo"), inits.get("houseworkInfo")) : null;
    }

}

