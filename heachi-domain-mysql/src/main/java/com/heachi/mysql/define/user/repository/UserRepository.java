package com.heachi.mysql.define.user.repository;

import com.heachi.mysql.define.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * SELECT u FROM User u WHERE u.email = :email
     * @param email
     */
    public Optional<User> findByEmail(Object email);

    /**
     * SELECT u FROM USER u WHERE u.platformId = :platformId
     */
    public Optional<User> findByPlatformId(String platformId);

}
