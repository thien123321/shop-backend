package com.minhthien.web.shop.controller.admin;

import com.minhthien.web.shop.dto.user.UserResponse;
import com.minhthien.web.shop.enums.UserStatus;
import com.minhthien.web.shop.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminUserService adminUserService;

    /* GET /api/admin/users?page=0&size=10 */
    @GetMapping
    public Page<UserResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return adminUserService.getAllUsers(page, size);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return adminUserService.getUserById(id);
    }


    @PutMapping("/{id}/lock")
    public void lockUser(@PathVariable Long id) {
        adminUserService.lockUser(id);
    }

    @PutMapping("/{id}/unlock")
    public void unlockUser(@PathVariable Long id) {
        adminUserService.unlockUser(id);
    }

    @PutMapping("/{id}/status")
    public void updateStatus(
            @PathVariable Long id,
            @RequestParam UserStatus status
    ) {
        adminUserService.updateStatus(id, status);
    }
}
