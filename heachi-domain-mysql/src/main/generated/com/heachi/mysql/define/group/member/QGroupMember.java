package com.heachi.mysql.define.group.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupMember is a Querydsl query type for GroupMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupMember extends EntityPathBase<GroupMember> {

    private static final long serialVersionUID = 76774106L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupMember groupMember = new QGroupMember("groupMember");

    public final com.heachi.mysql.define.QBaseEntity _super = new com.heachi.mysql.define.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final com.heachi.mysql.define.group.info.QGroupInfo groupInfo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final EnumPath<com.heachi.mysql.define.group.member.constant.GroupMemberRole> role = createEnum("role", com.heachi.mysql.define.group.member.constant.GroupMemberRole.class);

    public final EnumPath<com.heachi.mysql.define.group.member.constant.GroupMemberStatus> status = createEnum("status", com.heachi.mysql.define.group.member.constant.GroupMemberStatus.class);

    public final com.heachi.mysql.define.user.QUser user;

    public QGroupMember(String variable) {
        this(GroupMember.class, forVariable(variable), INITS);
    }

    public QGroupMember(Path<? extends GroupMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupMember(PathMetadata metadata, PathInits inits) {
        this(GroupMember.class, metadata, inits);
    }

    public QGroupMember(Class<? extends GroupMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupInfo = inits.isInitialized("groupInfo") ? new com.heachi.mysql.define.group.info.QGroupInfo(forProperty("groupInfo"), inits.get("groupInfo")) : null;
        this.user = inits.isInitialized("user") ? new com.heachi.mysql.define.user.QUser(forProperty("user")) : null;
    }

}

