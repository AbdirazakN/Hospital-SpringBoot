package hospital.service.serviceImpl;

import hospital.model.Appointment;
import hospital.model.Doctor;
import hospital.model.Hospital;
import hospital.repository.AppointmentRepository;
import hospital.repository.DoctorRepository;
import hospital.repository.HospitalRepository;
import hospital.service.DoctorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final HospitalRepository hospitalRepository;
    private final AppointmentRepository appointmentRepository;


    @Override
    public List<Doctor> getAllDoctors(Long doctorId) {
        return doctorRepository.getAllDoctors(doctorId);
    }

    @Transactional
    @Override
    public void saveDoctor(Doctor doctor, Long hospitalId) {
        try {
            Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow();
            if (!doctor.getEmail().trim().equals(doctor.getEmail()) || !doctor.getEmail().endsWith("@mail.ru") || !doctor.getEmail().endsWith("@gmail.com")
            || !doctor.getEmail().endsWith("@inbox.ru") || !doctor.getEmail().endsWith("@email.com") || !doctor.getEmail().matches("[a-zA-Z]*")){
                throw new Exception("Invalid data to save...");
            }
            for (Hospital hospital1 : hospitalRepository.findAll()) {
                if (hospital1.getId()==doctor.getHospital().getId()){
                    for (Doctor hospital1Doctor : hospital1.getDoctors()) {
                        if (hospital1Doctor.getEmail().equals(doctor.getEmail())) {
                            throw new Exception("This E-mail already added...");
                        }
                    }
                }
            }
            if (!doctor.getFirstName().trim().equals(doctor.getFirstName()) || doctor.getFirstName().isEmpty() || doctor.getFirstName().length()<3 ||
            !doctor.getFirstName().contains("{A-Za-z}")){
                throw new Exception("Invalid Name....");
            }
            if (!doctor.getLastName().trim().equals(doctor.getLastName()) || doctor.getLastName().isEmpty() || doctor.getLastName().length()<3 ||
                    !doctor.getLastName().contains("{A-Za-z}")){
                throw new Exception("Invalid Name....");
            }
            if (!doctor.getPosition().trim().equals(doctor.getPosition()) || doctor.getPosition().isEmpty() || doctor.getPosition().length()<3 ||
                    !doctor.getPosition().contains("{A-Za-z}")){
                throw new Exception("Invalid Position....");
            }
            doctor.setHospital(hospital);
            hospital.plusDoctor();
            doctorRepository.save(doctor);
        }catch (Exception e){
            System.out.println("Failed Saved...");
        }
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).get();
    }

    @Override
    public void deleteDoctorById(Long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public void updateDoctor(Long doctorId, Doctor doctor) {
//        validation(doctor.getEmail());
        Doctor doctor1 = doctorRepository.findById(doctorId).get();
        doctor1.setFirstName(doctor.getFirstName());
        doctor1.setLastName(doctor.getLastName());
        doctor1.setEmail(doctor.getEmail());
        doctor1.setPosition(doctor.getPosition());
        doctorRepository.save(doctor1);
    }

    @Override
    public void assignDoctor(Long appointmentId, Long doctorId) throws IOException {
        Doctor doctor = doctorRepository.findById(doctorId).get();
        Appointment appointment = appointmentRepository.findById(appointmentId).get();
        if(appointment.getDoctor()!=null){
            for (Doctor d: appointment.getHospital().getDoctors()) {
                if(d.getId()==doctorId){
                    throw new IOException("This is already assigned....");
                }
            }
        }
        doctor.addAppointments(appointment);
        appointment.setDoctor(doctor);
        doctorRepository.save(doctor);
        appointmentRepository.save(appointment);
    }

    public void validation(String email) {
        if (email.trim().length() == 0
                || !email.endsWith("@mail.ru")
                || !email.endsWith("@gmail.com")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid E-mail!!!");
        }
    }
}
