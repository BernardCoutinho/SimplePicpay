package com.simplepicpay.repositories;

import com.simplepicpay.domain.user.User;
import com.simplepicpay.domain.user.UserType;
import com.simplepicpay.dtos.UserDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Should get user successfully from database")
    void findUserByDocumentSuccess() {
        String document = "11111111111";
        UserDTO userDto = new UserDTO("Jorge", "Ölavo", document,new BigDecimal(20), "testejorge@gmail.com", "password", UserType.COMMON);
        this.createUser(userDto);


        Optional<User> result =  userRepository.findUserByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Shouldn't get user from database when doesn't exists")
    void findUserByDocumentFailed() {
        String document = "11111111111";
        Optional<User> result =  userRepository.findUserByDocument(document);

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findUserById() {
    }

    private User createUser(UserDTO data){
        User user = new User(data);
        entityManager.persist(user);
        return user;
    }
}