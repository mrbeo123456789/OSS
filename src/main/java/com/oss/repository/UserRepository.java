package com.oss.repository;

import com.oss.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:keyword% AND u.isDelete = false")
    Page<User> findByFullNameContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role.roleName = :roleName AND u.isDelete = false")
    Page<User> findByRole_RoleName(@Param("roleName") String roleName, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:keyword% AND u.role.roleName = :roleName AND u.isDelete = false")
    Page<User> findByFullNameContainingAndRole_RoleName(@Param("keyword") String keyword, @Param("roleName") String roleName, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isDelete = false")
    Page<User> findAll(Pageable pageable);
    User findByEmail(String email);
    User findByVerificationCode(String code);
    User findUserByUsername(String username);
    Optional<User> findByEmailAndVerificationCode(String email, String verificationCode);

}
