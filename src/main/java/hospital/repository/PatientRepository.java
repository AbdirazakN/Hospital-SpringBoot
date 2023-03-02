package hospital.repository;

import hospital.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
    @Query("select p from Patient p where p.hospital.id=:patientId")
    List<Patient> getAllPatient(Long patientId);
}
