package com.heachi.mysql.define.group.background;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGroupBg is a Querydsl query type for GroupBg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupBg extends EntityPathBase<GroupBg> {

    private static final long serialVersionUID = -387669863L;

    public static final QGroupBg groupBg = new QGroupBg("groupBg");

    public final com.heachi.mysql.define.QBaseEntity _super = new com.heachi.mysql.define.QBaseEntity(this);

    public final StringPath bgColor = createString("bgColor");

    public final StringPath colorCode = createString("colorCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final StringPath gradient = createString("gradient");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public QGroupBg(String variable) {
        super(GroupBg.class, forVariable(variable));
    }

    public QGroupBg(Path<? extends GroupBg> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGroupBg(PathMetadata metadata) {
        super(GroupBg.class, metadata);
    }

}

