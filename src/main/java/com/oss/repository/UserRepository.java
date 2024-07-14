package com.oss.repository;

import com.oss.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    Page<User> findByFullNameContaining(String keyword, Pageable pageable);
    Page<User> findByRole_RoleName(String roleName, Pageable pageable);
    Page<User> findByFullNameContainingAndRole_RoleName(String keyword, String roleName, Pageable pageable);
    Page<User> findAll(Pageable pageable);
    User findByEmail(String email);
    User findByVerificationCode(String code);
    User findUserByUsername(String username);
    Optional<User> findByEmailAndVerificationCode(String email, String verificationCode);

}
