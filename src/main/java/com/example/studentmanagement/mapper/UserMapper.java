package com.example.studentmanagement.mapper;

import com.example.studentmanagement.dto.UserRequest;
import com.example.studentmanagement.dto.UserResponse;
import com.example.studentmanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {


    // this line tells spring that dont try to map request.role to user.role ill do it manually in service
    @Mapping(target="student", ignore=true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserRequest request);

    @Mapping(target = "role", source = "role.name")
    UserResponse toResponse(User user);
}