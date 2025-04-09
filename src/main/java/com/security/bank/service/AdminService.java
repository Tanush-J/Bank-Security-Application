package com.security.bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.bank.dto.AdminDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.AccountType;
import com.security.bank.entity.BranchType;
import com.security.bank.entity.Role;
import com.security.bank.entity.User;
import com.security.bank.repository.AccountRepository;
import com.security.bank.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    //• POST "/admin/add" (Body AdminDto admin): It registers a new User and by default assigns it ROLE _ ADMIN, the password is encoded using BCryptPasswordEncoder.
    public void createAdmin(AdminDto adminDto) {
        String encodedPassword = passwordEncoder.encode(adminDto.getPassword());
        User user = new User();
        user.setAddress(adminDto.getAddress());
        user.setName(adminDto.getName());
        user.setNumber(adminDto.getNumber());
        user.setUsername(adminDto.getUsername());
        user.setPassword(encodedPassword);
        user.setIdentityProof(adminDto.getIdentityProof());
        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");
        user.setRoles(role);
        userRepository.save(user);
    }

    //• GET "/admin/getAllUser": It fetches the list of all users present in the database.
    public List<User> getAllUser() {
        return userRepository.findAll();
    }


    //• GET "/admin/getUserByName/{username}" (@PathVariable String username): It fetches a user by the given name.
    public User getUserByName(String username) {
        Optional<User> user =  userRepository.findByUsername(username);
        if(!user.isPresent()) {
            throw new RuntimeException("user with given username not find!");
        }
        return user.get();
    }

    //• DELETE "/admin/deleteUser/{userId}" (@PathVariable Long userId): It first confirms the existence of the user by the given userId. If found, it deletes the user from the database & returns this message in string format: "Deleted Successfully". If the user is not found, it returns this message in string format: "Error in deletion".
    public String deleteUser(Long userId) {
        Optional<User> user =  userRepository.findById(userId);
        if(!user.isPresent()) {
            return "Error in deletion";
        }
        userRepository.delete(user.get());
        return "Deleted Successfully";
    }
    

    //• PUT "/admin/account/deactivate" (@RequestParam Long userId,@RequestParam Long accountId): It first confirms the existence of the given user and account. If found, it modifies the account status to INACTIVE for the given accountId & returns this message in string format: "Deactivated Account for User with id: "+userId; If the given user and account are not found then it returns this message in string format: "ERROR".
    public String deactivateAccount(Long userId, Long accountId) {
        Optional<User> user =  userRepository.findById(userId);
        if(!user.isPresent()) {
            return "Error";
        }
        User user2 = user.get();
        Account account2 = user2.getAccountList().stream()
            .filter(acc -> acc.getId().equals(accountId))
            .findFirst()
            .orElse(null);
        if (account2 == null) {
            return "Error";
        }
        account2.setStatus("INACTIVE");
        accountRepository.save(account2);
        return "Deactivated Account for User with id: "+userId;
    }

    //• PUT  "/admin/account/activate" (@RequestParam Long userId,@RequestParam Long accountId): It first confirms the existence of the given user and account. If found, it modifies the account status to ACTIVE for the given accountId & returns this message in string format: "Activated Account for User with id: "+userId; If the given user and account are not found then it returns this message in string format: "ERROR".
    public String activaeAccount(Long userId, Long accountId) {
        Optional<User> user =  userRepository.findById(userId);
        if(!user.isPresent()) {
            return "Error";
        }
        User user2 = user.get();
        Account account2 = user2.getAccountList().stream()
            .filter(acc -> acc.getId().equals(accountId))
            .findFirst()
            .orElse(null);
        if (account2 == null) {
            return "Error";
        }
        account2.setStatus("INACTIVE");
        accountRepository.save(account2);
        return "Activated Account for User with id: "+userId;
    }

    //• GET "/admin/account/getActiveAccountsList": It fetches the list of Active accounts from the database.
    public List<Account> getActiveAccountList() {
        List<Account> accounts = accountRepository.findAllActiveAccounts();
        return accounts;
    }

    //• GET "/admin/account/getInActiveAccountsList": It fetches the list of INACTIVE accounts from the database.
    public List<Account> getInActiveAccountList() {
        List<Account> accounts = accountRepository.findAllInActiveAccounts();
        return accounts;
    }

    //• GET "/admin/account/getActiveAccountsList" (@PathVariable AccountType accType): It fetches the list of accounts by accType from the database.
    public List<Account> getAccountListByAccType(AccountType accType) {
        List<Account> accounts = accountRepository.findAllByAccountType(accType);
        return accounts;
    }

    //• GET "/admin/account/getInActiveAccountsList" (@PathVariable BranchType branchType): It fetches the list of accounts by branchType from the database.
    public List<Account> getAccountListByBranchType(BranchType branchType) {
        List<Account> accounts = accountRepository.findAllByBranch(branchType);
        return accounts;
    }
}
