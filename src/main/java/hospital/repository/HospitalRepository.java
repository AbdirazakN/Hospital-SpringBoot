package hospital.repository;

import hospital.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface HospitalRepository extends JpaRepository<Hospital,Long> {
    @Query("select h from Hospital h where h.name ilike  (:keyWord) or h.address ilike  (:keyWord)")
    List<Hospital> search(String keyWord);

}
