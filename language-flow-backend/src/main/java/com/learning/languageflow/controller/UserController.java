package com.learning.languageflow.controller;

import com.learning.languageflow.dto.ApiResponse;
import com.learning.languageflow.dto.DailyGoalUpdateRequest;
import com.learning.languageflow.dto.DtoMapper;
import com.learning.languageflow.dto.ProgressUpdateRequest;
import com.learning.languageflow.dto.UserDto;
import com.learning.languageflow.model.User;
import com.learning.languageflow.service.ProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final ProgressService progressService;
    private final DtoMapper dtoMapper;

    public UserController(ProgressService progressService, DtoMapper dtoMapper) {
        this.progressService = progressService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<User> users = progressService.getAllUsers();
        List<UserDto> dtos = users.stream()
                .map(dtoMapper::toUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok("Users fetched successfully", dtos));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserDto>> getUserProfile(@PathVariable String username) {
        try {
            User user = progressService.getUserProgress(username);
            return ResponseEntity.ok(ApiResponse.ok("User profile fetched successfully", dtoMapper.toUserDto(user)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/{username}/daily-goal")
    public ResponseEntity<ApiResponse<Void>> updateDailyGoal(
            @PathVariable String username,
            @RequestBody DailyGoalUpdateRequest request) {
        try {
            progressService.updateDailyGoal(username, request.getDailyGoalMinutes());
            return ResponseEntity.ok(ApiResponse.ok("Daily goal updated successfully", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PutMapping("/{username}/progress")
    public ResponseEntity<ApiResponse<Void>> updateProgress(
            @PathVariable String username,
            @RequestBody ProgressUpdateRequest request) {
        try {
            progressService.updateProgressPercentage(username, request.getProgressPercentage());
            return ResponseEntity.ok(ApiResponse.ok("Progress updated successfully", null));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/{username}/streak/increment")
    public ResponseEntity<ApiResponse<Void>> incrementStreak(@PathVariable String username) {
        try {
            progressService.incrementStreak(username);
            return ResponseEntity.ok(ApiResponse.ok("Streak incremented successfully", null));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @PostMapping("/{username}/streak/reset")
    public ResponseEntity<ApiResponse<Void>> resetStreak(@PathVariable String username) {
        try {
            progressService.resetStreak(username);
            return ResponseEntity.ok(ApiResponse.ok("Streak reset successfully", null));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable int id) {
        boolean deleted = progressService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.ok("User deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found with id: " + id));
        }
    }
}
