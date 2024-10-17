package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.Users;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
	
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<Users> findOneByActivationKey(String activationKey);

    List<Users> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<Users> findOneByResetKey(String resetKey);
    Optional<Users> findByEmail(String email);

    Optional<Users> findOneByEmailIgnoreCase(String email);

    Optional<Users> findOneByEmailIgnoreCaseAndActivated(String email, Boolean activated);
//    Optional<Users> findByEmailOrUsernameIgnoreCaseAndActivated(String email, Boolean activated);
    @Query("SELECT u FROM Users u WHERE LOWER(u.email) = LOWER(:emailOrUsername) OR LOWER(u.username) = LOWER(:emailOrUsername) AND u.activated = :activated")
    Optional<Users> findByEmailOrUsernameIgnoreCaseAndActivated(@Param("emailOrUsername") String emailOrUsername, @Param("activated") boolean activated);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<Users> findOneWithAuthoritiesByEmail(String email);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<Users> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<Users> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
    
}
