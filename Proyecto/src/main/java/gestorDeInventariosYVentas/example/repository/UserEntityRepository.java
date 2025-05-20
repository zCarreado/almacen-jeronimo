package gestorDeInventariosYVentas.example.repository;

import gestorDeInventariosYVentas.example.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByUserName(String userName);
}
