package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.common.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
 class UserWarmup implements CommandLineRunner {

    UserWarmup(final PasswordEncoder passwordEncoder, final UserCommandRepository userCommandRepository, final UserQueryRepository userQueryRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userCommandRepository = userCommandRepository;
        this.userQueryRepository = userQueryRepository;
    }

    private final PasswordEncoder passwordEncoder;
    private final UserCommandRepository userCommandRepository;
    private final UserQueryRepository userQueryRepository;

    @Override
    public void run(final String... args) throws Exception {

        if (userQueryRepository.count() == 0) {
            User user1 = new User();
            user1.setName("Patryk");
            user1.setPassword(passwordEncoder.encode("Qweasdzxc123!"));
            user1.setEmail("patryk@gmail.com");
            user1.setRole(Role.USER);
            user1.setShareData(true);

            User user2 = new User();
            user2.setName("Kuba");
            user2.setPassword(passwordEncoder.encode("Qweasdzxc123!"));
            user2.setEmail("kuba@gmail.com");
            user2.setRole(Role.USER);
            user2.setShareData(true);

            userCommandRepository.saveAll(List.of(user1, user2));
        }
    }
}
