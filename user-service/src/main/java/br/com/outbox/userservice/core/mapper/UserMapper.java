package br.com.outbox.userservice.core.mapper;

import br.com.outbox.userservice.core.dto.Event;
import br.com.outbox.userservice.core.dto.UserDTO;
import br.com.outbox.userservice.core.enums.EUserAction;
import br.com.outbox.userservice.core.model.User;
import br.com.outbox.userservice.core.utils.RequestUtil;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class UserMapper {

    public UserDTO toUserDto(User user) {
        return UserDTO
            .builder()
            .id(user.getId())
            .username(user.getUsername())
            .fullName(user.getFullName())
            .document(user.getDocument())
            .email(user.getEmail())
            .birthday(user.getBirthday())
            .status(user.getStatus())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }

    public User toUserEntity(UserDTO userDTO) {
        return User
            .builder()
            .id(userDTO.getId())
            .username(userDTO.getUsername())
            .fullName(userDTO.getFullName())
            .document(userDTO.getDocument())
            .email(userDTO.getEmail())
            .birthday(userDTO.getBirthday())
            .status(userDTO.getStatus())
            .build();
    }

    public Event toEvent(User user, EUserAction action) {
        return Event
            .builder()
            .user(toUserDto(user))
            .action(action)
            .httpMethod(RequestUtil.getCurrentHttpMethod())
            .url(RequestUtil.getCurrentUrl())
            .createdAt(LocalDateTime.now())
            .build();
    }
}
