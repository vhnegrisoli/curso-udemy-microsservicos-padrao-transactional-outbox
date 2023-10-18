package br.com.outbox.userservice.core.repository;

import br.com.outbox.userservice.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsByUsername(String username);

    Boolean existsByIdAndUsername(Integer id, String username);
}
