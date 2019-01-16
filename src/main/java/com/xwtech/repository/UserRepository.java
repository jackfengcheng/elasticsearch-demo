package com.xwtech.repository;

import com.xwtech.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户respsitory
 */
public interface UserRepository extends JpaRepository<User,Integer> {
}
