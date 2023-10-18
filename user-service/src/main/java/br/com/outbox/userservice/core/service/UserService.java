package br.com.outbox.userservice.core.service;

import br.com.outbox.userservice.config.exception.ValidationException;
import br.com.outbox.userservice.core.dto.UserDTO;
import br.com.outbox.userservice.core.enums.EUserAction;
import br.com.outbox.userservice.core.mapper.UserMapper;
import br.com.outbox.userservice.core.model.User;
import br.com.outbox.userservice.core.repository.UserRepository;
import br.com.outbox.userservice.core.utils.JsonUtil;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.outbox.userservice.core.enums.EUserAction.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final JsonUtil jsonUtil;
    private final EntityManager entityManager;
    private final OutboxService outboxService;
    private final UserRepository userRepository;

    @Transactional
    public User create(UserDTO userDTO) {
        return processUser(userDTO, CREATE_USER);
    }

    @Transactional
    public User update(UserDTO userDTO, Integer id) {
        userDTO.setId(id);
        return processUser(userDTO, UPDATE_USER);
    }

    private User processUser(UserDTO userDTO, EUserAction action) {
        checkExistingUsername(userDTO, action);
        var user = save(UserMapper.toUserEntity(userDTO));
        user = findById(user.getId());
        sendToOutbox(user, action);
        return user;
    }

    private void checkExistingUsername(UserDTO userDTO, EUserAction action) {
        if (CREATE_USER.equals(action)
            && userRepository.existsByUsername(userDTO.getUsername())) {
            throw new ValidationException("Already existing username!");
        }
        if (UPDATE_USER.equals(action)
            && !userRepository.existsByIdAndUsername(userDTO.getId(), userDTO.getUsername())) {
            throw new ValidationException("You can't change the username!");
        }
    }

    @Transactional
    public User findById(Integer id) {
        return userRepository
            .findById(id)
            .orElseThrow(() -> new ValidationException("User not found by ID!"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User delete(Integer id) {
        var user = findById(id);
        userRepository.delete(user);
        sendToOutbox(user, DELETE_USER);
        return user;
    }

    private void sendToOutbox(User user, EUserAction action) {
        var event = UserMapper.toEvent(user, action);
        outboxService.create(action.getTopic(), jsonUtil.toJson(event));
    }

    private User save(User user) {
        userRepository.saveAndFlush(user);
        entityManager.clear();
        return user;
    }
}