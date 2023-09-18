package com.heachi.redis.define.state;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LoginStateRepository extends CrudRepository<LoginState, UUID> {
}