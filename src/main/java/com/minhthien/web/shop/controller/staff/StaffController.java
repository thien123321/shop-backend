package com.minhthien.web.shop.controller.staff;


import com.minhthien.web.shop.dto.user.UserRequest;
import com.minhthien.web.shop.dto.user.UserResponse;
import com.minhthien.web.shop.service.staff.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
@PreAuthorize("hasAnyRole('USER','ADMIN','STAFF')")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        return staffService.createUser(request);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request
    ) {
        return staffService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        staffService.deleteUser(id);
    }
}
