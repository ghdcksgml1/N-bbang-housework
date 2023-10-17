package com.heachi.redis.define.state.repository;

import com.heachi.redis.define.state.LoginState;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LoginStateRepository extends CrudRepository<LoginState, UUID> {
}
