package bliznyuk.stanislav.usereditor.repository;

import bliznyuk.stanislav.usereditor.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select b from User b where b.name = :name")
    List<User> findByName(@Param("name") String name);

    @Query("select b from User b where PARSEDATETIME(b.birthday,'yyyy-MM-dd') > PARSEDATETIME(:date,'yyyy-MM-dd')")
    List<User> dateLater(@Param("date") String date);

    @Query("select b from User b where PARSEDATETIME(b.birthday,'yyyy-MM-dd') < PARSEDATETIME(:date,'yyyy-MM-dd')")
    List<User> dateEarlier(@Param("date") String date);

    @Query("select b from User b where b.surname = :surname")
    List<User> findBySurname(@Param("surname") String surname);

    @Query("select friends from User b where b.id = :id")
    List<User> getFriends(@Param("id") Long id);


}
