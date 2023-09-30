package com.heachi.mysql.define.housework.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHouseworkCategory is a Querydsl query type for HouseworkCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHouseworkCategory extends EntityPathBase<HouseworkCategory> {

    private static final long serialVersionUID = -508153694L;

    public static final QHouseworkCategory houseworkCategory = new QHouseworkCategory("houseworkCategory");

    public final com.heachi.mysql.define.QBaseEntity _super = new com.heachi.mysql.define.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final StringPath name = createString("name");

    public QHouseworkCategory(String variable) {
        super(HouseworkCategory.class, forVariable(variable));
    }

    public QHouseworkCategory(Path<? extends HouseworkCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHouseworkCategory(PathMetadata metadata) {
        super(HouseworkCategory.class, metadata);
    }

}

