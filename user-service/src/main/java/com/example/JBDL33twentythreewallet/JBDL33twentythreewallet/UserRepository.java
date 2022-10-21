package com.example.JBDL33twentythreewallet.JBDL33twentythreewallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByPhoneNumber(String phoneNumber);
}
