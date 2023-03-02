package hospital.service.serviceImpl;

import hospital.exception.NotFoundException;
import hospital.model.Appointment;
import hospital.model.Hospital;
import hospital.repository.AppointmentRepository;
import hospital.repository.HospitalRepository;
import hospital.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final HospitalRepository hospitalRepository;

    @Override
    public List<Appointment> getAllAppointment(Long appointmentId) {
        try {
            return appointmentRepository.findAll();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
        return appointmentRepository.findAll();
    }

    @Override
    public void saveAppointment(Appointment appointment, Long hospitalId) {
        try {
            if (!(hospitalId==null && appointment==null)){
                validationDate(appointment.getDate());

            }
            Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow();
            appointment.setHospital(hospital);
            appointmentRepository.save(appointment);
        } catch (Exception e) {
            System.out.println("Not found...");
        }
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        try {
            return appointmentRepository.findById(id).orElseThrow();
        } catch (NotFoundException e) {
            System.out.println("Not found...");
        }
        return appointmentRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteAppointmentById(Long id) {
        try {
            appointmentRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Delete failed..."));
        }

    }

    @Override
    public void updateAppointment(Long appointmentId, Appointment appointment) {
        try {
            if (appointment.getDepartment() == null || appointment.getPatient() == null || appointment.getDate() == null || appointment.getDoctor() == null ||
                    appointment.getHospital() == null || appointment == null) {
                throw new NotFoundException("Not found any field...");
            }
            appointmentRepository.save(appointment);
        } catch (Exception e) {
            System.out.println("Not found any field...");
        }
        appointmentRepository.save(appointment);
    }

    public void validationDate(LocalDate date) {
        if (date == null || date.isBefore(LocalDate.now().minusDays(1)) &&
                date.isAfter(LocalDate.now().plusDays(20))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data...");
        }
    }
}
