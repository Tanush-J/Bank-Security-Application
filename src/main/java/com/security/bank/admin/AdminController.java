package com.security.bank.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.security.bank.dto.AdminDto;
import com.security.bank.entity.Account;
import com.security.bank.entity.AccountType;
import com.security.bank.entity.BranchType;
import com.security.bank.entity.User;
import com.security.bank.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    //• POST "/admin/add" (Body AdminDto admin): It registers a new User and by default assigns it ROLE _ ADMIN, the password is encoded using BCryptPasswordEncoder.
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void createAdmin(@RequestBody AdminDto adminDto) {
        adminService.createAdmin(adminDto);
    }

    //• GET "/admin/getAllUser": It fetches the list of all users present in the database.
    @GetMapping("/getAllUser")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUser() {
        return adminService.getAllUser();
    }


    //• GET "/admin/getUserByName/{username}" (@PathVariable String username): It fetches a user by the given name.
    @GetMapping("/getUserByName/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserByName(@PathVariable String username) {
        return adminService.getUserByName(username);
    }

    //• DELETE "/admin/deleteUser/{userId}" (@PathVariable Long userId): It first confirms the existence of the user by the given userId. If found, it deletes the user from the database & returns this message in string format: "Deleted Successfully". If the user is not found, it returns this message in string format: "Error in deletion".
    @DeleteMapping("/deleteUser/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long userId) {
        return adminService.deleteUser(userId);
    }
    

    //• PUT "/admin/account/deactivate" (@RequestParam Long userId,@RequestParam Long accountId): It first confirms the existence of the given user and account. If found, it modifies the account status to INACTIVE for the given accountId & returns this message in string format: "Deactivated Account for User with id: "+userId; If the given user and account are not found then it returns this message in string format: "ERROR".
    @PutMapping("/account/deactivate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ADMIN')")
    public String deactivateAccount(@RequestParam Long userId,@RequestParam Long accountId) {
        return adminService.deactivateAccount(userId, accountId);
    }

    //• PUT  "/admin/account/activate" (@RequestParam Long userId,@RequestParam Long accountId): It first confirms the existence of the given user and account. If found, it modifies the account status to ACTIVE for the given accountId & returns this message in string format: "Activated Account for User with id: "+userId; If the given user and account are not found then it returns this message in string format: "ERROR".
    @PutMapping("/account/activate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('ADMIN')")
    public String activaeAccount(@RequestParam Long userId,@RequestParam Long accountId) {
        return  adminService.activaeAccount(userId, accountId);
    }

    //• GET "/admin/account/getActiveAccountsList": It fetches the list of Active accounts from the database.
    @GetMapping("/account/getActiveAccountsList")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Account> getActiveAccountList() {
        return adminService.getActiveAccountList();
    }

    //• GET "/admin/account/getInActiveAccountsList": It fetches the list of INACTIVE accounts from the database.
    @GetMapping("/account/getInActiveAccountsList")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Account> getInActiveAccountList() {
        return adminService.getInActiveAccountList();
    }

    //• GET "/admin/account/getActiveAccountsList" (@PathVariable AccountType accType): It fetches the list of accounts by accType from the database.
    @GetMapping("/accountList/ByAccountType/{accType}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Account> getAccountListByAccType(@PathVariable AccountType accType) {
        return adminService.getAccountListByAccType(accType);
    }

    //• GET "/admin/account/getInActiveAccountsList" (@PathVariable BranchType branchType): It fetches the list of accounts by branchType from the database.
    @GetMapping("/accountList/ByBranchType/{branchType}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Account> getAccountListByBranchType(@PathVariable BranchType branchType) {
        return adminService.getAccountListByBranchType(branchType);
    }
}
