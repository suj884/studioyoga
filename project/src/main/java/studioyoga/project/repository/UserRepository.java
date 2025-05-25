package studioyoga.project.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studioyoga.project.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    
    List<User> findByFirstLastNameContainingIgnoreCaseOrSecondLastNameContainingIgnoreCaseOrNameContainingIgnoreCase(String firstLastName, String secondLastName, String name);

    List<User> findByRol_Name(String roleName);

}
