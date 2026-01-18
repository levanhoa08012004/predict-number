package com.example.predict_numbers.mapper;

import com.example.predict_numbers.dto.request.CreateUserRequest;
import com.example.predict_numbers.dto.response.UserResponse;
import com.example.predict_numbers.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)public interface UserMapper {
    UserResponse toUserResponse(User user);

    @Mapping(target = "password", ignore = true) // xử lý ở service
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "turns", ignore = true)
    User toUser(CreateUserRequest createUserRequest);

}
