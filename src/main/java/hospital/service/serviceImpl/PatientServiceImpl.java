package hospital.service.serviceImpl;

import hospital.exception.NotFoundException;
import hospital.model.Appointment;
import hospital.model.Hospital;
import hospital.model.Patient;
import hospital.repository.AppointmentRepository;
import hospital.repository.HospitalRepository;
import hospital.repository.PatientRepository;
import hospital.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<Patient> getAllPatient(Long patientId) {
        return patientRepository.getAllPatient(patientId);
    }

    @Override
    public void savePatient(Patient patient, Long hospitalId) {
//        if (hospitalRepository.findById(hospitalId).get() == null){
//            throw new NotFoundException(String.format("Hospital with id %d not found",hospitalId));
//        }
        Hospital hospital = hospitalRepository.findById(hospitalId).get();
//        validation(patient.getPhoneNumber());
//        emailValidation(patient.getEmail());
        patient.setHospital(hospital);
        hospital.plusPatient();
        patientRepository.save(patient);
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).get();
    }

    @Override
    public void deletePatientById(Long id) {
        patientRepository.deleteById(id);
    }

    @Override
    public void updatePatient(Long patientId, Patient patient) {
        Patient patient1 = patientRepository.findById(patientId).get();
        patient1.setFirstName(patient.getFirstName());
        patient1.setLastName(patient.getLastName());
        patient1.setGender(patient.getGender());
        patient1.setPhoneNumber(patient.getPhoneNumber());
        patient1.setEmail(patient.getEmail());
        patient1.setAppointments(patient.getAppointments());
        patientRepository.save(patient1);
    }

    @Override
    public void assignPatient(Long appointmentId, Long patientId) throws IOException {
        Patient patient = patientRepository.findById(patientId).get();
        Appointment appointment = appointmentRepository.findById(appointmentId).get();
        if (appointment.getPatient()!=null){
            for (Patient p: appointment.getHospital().getPatients()) {
                if (p.getId()==patientId){
                    throw new IOException("This Patient already added....");
                }
            }
        }
        patient.addAppointment(appointment);
        appointment.setPatient(patient);
        patientRepository.save(patient);
        appointmentRepository.save(appointment);
    }

    public void validation(String phoneNumber) {
        if (phoneNumber.length() == 13
                && phoneNumber.charAt(0) == '+'
                && phoneNumber.charAt(1) == '9'
                && phoneNumber.charAt(2) == '9'
                && phoneNumber.charAt(3) == '6'){
            int count = 0;
            for (Character i : phoneNumber.toCharArray()) {
                if (count != 0){
                    if (!Character.isDigit(i)){
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid number!!!");
                    }
                }
                count++;
            }
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid number!!!");
        }
    }
    public void emailValidation(String email){
        try {
            if (email.trim().length() < 4 && email.isEmpty() && !email.endsWith("@gmail.com") && !email.endsWith("@email.com")
                    && !email.endsWith("@mail.ru")) {
                throw new Exception("Invalid E-mail...");
            }
            for (Hospital hospital : hospitalRepository.findAll()) {
                for (Patient patient : hospital.getPatients()) {
                    if (patient.getEmail().equals(email)){
                        throw new Exception("This e-mail already added...");
                    }
                }
            }
        }catch (Exception e){
            e.getMessage();
        }
    }
}
