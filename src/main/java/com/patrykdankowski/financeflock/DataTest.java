package com.patrykdankowski.financeflock;

import com.patrykdankowski.financeflock.entity.BudgetGroup;
import com.patrykdankowski.financeflock.entity.Expense;
import com.patrykdankowski.financeflock.entity.Role;
import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.repository.BudgetGroupRepository;
import com.patrykdankowski.financeflock.repository.ExpenseRepository;
import com.patrykdankowski.financeflock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataTest implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    BudgetGroupRepository budgetGroupRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        //user1

        User user1 = new User();
        user1.setName("Patryk");
        user1.setPassword(passwordEncoder.encode("testoweHaslo"));
        user1.setEmail("patryk@gmail.com");
        user1.setRole(Role.GROUP_ADMIN);
        user1.setShareData(true);

        Expense expense1 = new Expense();
        expense1.setUser(user1);
        expense1.setAmount(BigDecimal.valueOf(3300.50));
        expense1.setExpenseDate(LocalDateTime.now().plusDays(2));
        expense1.setDescription("Testowy opis wydatku dla User1");
        expense1.setLocation("Katowice");

        Expense expense2 = new Expense();
        expense2.setUser(user1);
        expense2.setAmount(BigDecimal.valueOf(4300.50));
        expense2.setExpenseDate(LocalDateTime.now().plusDays(4));
        expense2.setDescription("Testowy opis wydatku dla User1");
        expense2.setLocation("Kraków");
        user1.setExpenseList(List.of(expense1, expense2));

        // user2

        User user2 = new User();
        user2.setName("Kuba");
        user2.setPassword(passwordEncoder.encode("testoweHaslo"));
        user2.setEmail("kuba@gmail.com");
        user2.setRole(Role.GROUP_MEMBER);
        user2.setShareData(true);

        Expense expense3 = new Expense();
        expense3.setUser(user2);
        expense3.setAmount(BigDecimal.valueOf(3300.50));
        expense3.setExpenseDate(LocalDateTime.now().plusDays(2));
        expense3.setDescription("Testowy opis wydatku dla User2");
        expense3.setLocation("Katowice");

        Expense expense4 = new Expense();
        expense4.setUser(user2);
        expense4.setAmount(BigDecimal.valueOf(4300.50));
        expense4.setExpenseDate(LocalDateTime.now().plusDays(4));
        expense4.setDescription("Testowy opis wydatku dla User2");
        expense4.setLocation("Kraków");
        user2.setExpenseList(List.of(expense3, expense4));

        BudgetGroup budgetGroup1 = new BudgetGroup();
        budgetGroup1.setDescription("Testowy opis grupy 1");
        budgetGroup1.setOwner(user1);

        user1.setBudgetGroup(budgetGroup1);
        user2.setBudgetGroup(budgetGroup1);

        Set<User> listWithUsers1 = new HashSet<>();
        listWithUsers1.add(user1);
        listWithUsers1.add(user2);

        // user 3 without group
        User user3 = new User();
        user3.setName("Oskar");
        user3.setPassword(passwordEncoder.encode("testoweHaslo"));
        user3.setEmail("kuba@gmail.com");
        user3.setRole(Role.USER);
        user3.setShareData(true);

        Expense expense5 = new Expense();
        expense5.setUser(user3);
        expense5.setAmount(BigDecimal.valueOf(3300.50));
        expense5.setExpenseDate(LocalDateTime.now().plusDays(2));
        expense5.setDescription("Testowy opis wydatku dla User3");
        expense5.setLocation("Katowice");

        Expense expense6 = new Expense();
        expense6.setUser(user3);
        expense6.setAmount(BigDecimal.valueOf(4300.50));
        expense6.setExpenseDate(LocalDateTime.now().plusDays(4));
        expense6.setDescription("Testowy opis wydatku dla User3");
        expense6.setLocation("Kraków");
        user3.setExpenseList(List.of(expense5, expense6));




        userRepository.saveAll(List.of(user1, user2,user3));
        expenseRepository.saveAll(List.of(expense1, expense2, expense3, expense4));
        budgetGroupRepository.save(budgetGroup1);
    }
}
