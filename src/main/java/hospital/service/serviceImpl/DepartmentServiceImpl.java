package hospital.service.serviceImpl;

import hospital.exception.InvalidException;
import hospital.exception.NotFoundException;
import hospital.model.Appointment;
import hospital.model.Department;
import hospital.model.Doctor;
import hospital.model.Hospital;
import hospital.repository.AppointmentRepository;
import hospital.repository.DepartmentRepository;
import hospital.repository.DoctorRepository;
import hospital.repository.HospitalRepository;
import hospital.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<Department> getAllDepartment(Long departmentId) {
        try{
            if (departmentRepository.getAllDepartment(departmentId).isEmpty()){
                throw new NotFoundException("Not found...");
            }
            return departmentRepository.getAllDepartment(departmentId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return departmentRepository.getAllDepartment(departmentId);
    }

    @Override
    public void saveDepartment(Department department, Long hospitalId) throws InvalidException {
        try {
            Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow();
            if (!department.getName().trim().equals(department.getName()) || department.getName().length()<3 || //|| !department.getName().contains("[A-z]")
            department.getName().length()>20 || hospital==null){
                throw new Exception("Department Name Exception!!!");
            }
            department.setHospital(hospital);
            departmentRepository.save(department);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).get();
    }

    @Override
    public void deleteDepartmentById(Long id) {
        Department department = departmentRepository.findById(id).get();
        for (Doctor doctor : department.getDoctors()) {
            department.getDoctors().remove(doctor);
        }
        for (Appointment appointment : department.getAppointments()) {
            department.getAppointments().remove(appointment);
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public void updateDepartment(Long departmentId, Department department) {
//        try {
//            if (!department.getName().trim().equals(department.getName().length()) || department.getName().length()<3 || department.getName().length()>20
//                    ){ //|| !department.getName().contains("[A-z]")
//                throw new Exception("Invalid name!!!");
//            }
            Department department1 = departmentRepository.findById(departmentId).get();
            department1.setName(department.getName());
            departmentRepository.save(department1);
//        }catch (Exception e){
//            System.out.println("Invalid Department name...");
//        }
    }

    @Override
    public void AssignDepartment(Long doctorId, Long departmentId) throws IOException {
        Department department = departmentRepository.findById(departmentId).get();
        Doctor doctor = doctorRepository.findById(doctorId).get();
        if (doctor.getDepartments() != null){
            for (Department d: doctor.getDepartments()) {
                if(d.getId()==departmentId){
                    throw new IOException("This Department already added...");
                }
            }
        }
        department.addDoctors(doctor);
        doctor.addDepartments(department);
        departmentRepository.save(department);
        doctorRepository.save(doctor);
    }

    @Override
    public void AssignDepartmentToAppointment(Long appointmentId, Long departmentId) throws IOException {
        Department department = departmentRepository.findById(departmentId).orElseThrow();
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        if (appointment.getDepartment() != null){
            for (Department d: appointment.getHospital().getDepartments()) {
                if (d.getId() ==departmentId){
                    throw  new IOException("This Department already assigned....");
                }
            }
        }
        department.addAppointment(appointment);
        appointment.setDepartment(department);
        departmentRepository.save(department);
        appointmentRepository.save(appointment);
    }
}
