package hospital.repository;

import hospital.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    @Query("select d from Doctor d where d.hospital.id=:doctorId")
    List<Doctor> getAllDoctors(Long doctorId);
}
