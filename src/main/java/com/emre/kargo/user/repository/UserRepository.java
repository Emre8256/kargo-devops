package com.emre.kargo.user.repository;

import com.emre.kargo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByPhoneAndIdNot(String phone, Long id);
    //update işleminde girilen mail ve telefonun var olup olmadığını kontrol ederken mevcut
    //userın mailini ve telefonunu algılamasın diye
}
