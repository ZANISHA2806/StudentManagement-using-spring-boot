package com.example.studentmanagement.security;

import com.example.studentmanagement.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    public boolean canAccessStudent(
            UserDetails userDetails,
            Long studentId) {

        CustomUserDetails customUser =
                (CustomUserDetails) userDetails;

        User user = customUser.getUser();

        if (user.getRole().getName().equals("ROLE_ADMIN")) {
            return true;
        }

        if (user.getRole().getName().equals("ROLE_TEACHER")) {
            return true;
        }

        return user.getStudent() != null
                && user.getStudent().getId().equals(studentId);
    }
}