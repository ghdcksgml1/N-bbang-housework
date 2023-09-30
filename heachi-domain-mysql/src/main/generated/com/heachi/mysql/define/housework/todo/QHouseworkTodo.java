package com.heachi.mysql.define.housework.todo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHouseworkTodo is a Querydsl query type for HouseworkTodo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHouseworkTodo extends EntityPathBase<HouseworkTodo> {

    private static final long serialVersionUID = -1421769550L;

    public static final QHouseworkTodo houseworkTodo = new QHouseworkTodo("houseworkTodo");

    public final com.heachi.mysql.define.QBaseEntity _super = new com.heachi.mysql.define.QBaseEntity(this);

    public final StringPath category = createString("category");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final StringPath detail = createString("detail");

    public final StringPath houseworkMember = createString("houseworkMember");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> index = createNumber("index", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final EnumPath<com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus> status = createEnum("status", com.heachi.mysql.define.housework.todo.constant.HouseworkTodoStatus.class);

    public final StringPath title = createString("title");

    public QHouseworkTodo(String variable) {
        super(HouseworkTodo.class, forVariable(variable));
    }

    public QHouseworkTodo(Path<? extends HouseworkTodo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHouseworkTodo(PathMetadata metadata) {
        super(HouseworkTodo.class, metadata);
    }

}

