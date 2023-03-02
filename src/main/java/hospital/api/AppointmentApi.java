package hospital.api;

import hospital.model.Appointment;
import hospital.model.Department;
import hospital.model.Doctor;
import hospital.model.Patient;
import hospital.service.AppointmentService;
import hospital.service.DepartmentService;
import hospital.service.DoctorService;
import hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/appointments")
public class AppointmentApi {

    private final AppointmentService appointmentService;
    private final PatientService patientService;

    private final DepartmentService departmentService;

    private final DoctorService doctorService;

    @Autowired
    public AppointmentApi(AppointmentService appointmentService, PatientService patientService, DepartmentService departmentService, DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.departmentService = departmentService;
        this.doctorService = doctorService;
    }

    @GetMapping("/{id}")
    public String getAllAppointment(@PathVariable Long id, Model model,
                                    @ModelAttribute("patient")Patient patient,
                                    @ModelAttribute("doctor")Doctor doctor,
                                    @ModelAttribute("department")Department department,
                                    @ModelAttribute("appointment")Appointment appointment){
        model.addAttribute("departments",departmentService.getAllDepartment(id));
        model.addAttribute("doctors",doctorService.getAllDoctors(id));
        model.addAttribute("patients",patientService.getAllPatient(id));
        model.addAttribute("appointments",appointmentService.getAllAppointment(id));
        model.addAttribute("hospitalId",id);
        return "appointment/appointments";
    }

    @GetMapping("/{id}/newAppointment")
    public String createAppointment(@PathVariable Long id,Model model){
        model.addAttribute("newAppointment",new Appointment());
        model.addAttribute("hospitalId",id);
        return "appointment/addAppointment";
    }

    @PostMapping("/{id}/saveAppointment")
    public String saveAppointment(@ModelAttribute("newAppointment")Appointment appointment,
                                  @PathVariable("id") Long id){
        appointmentService.saveAppointment(appointment,id);
        return "redirect:/appointments/"+id;
    }

    @GetMapping("/edit/{id}")
    public String updateAppointment(@PathVariable Long id,Model model){
        Appointment appointment = appointmentService.getAppointmentById(id);
        model.addAttribute("appointment",appointment);
        model.addAttribute("hospitalId",appointment.getHospital().getId());
        return "appointment/updateAppointment";
    }

    @PostMapping("/{id}/{appointmentId}/update")
    public String saveUpdateAppointment(@ModelAttribute("appointment") Appointment appointment,
                                        @PathVariable("id")Long id,
                                        @PathVariable("appointmentId")Long appointmentId){
        appointmentService.updateAppointment(appointmentId,appointment);
        return "redirect:/appointments/"+id;
    }

    @GetMapping("/{id}/{hosId}")
    public String deleteAppointmentById(@PathVariable("id") Long id,
                                        @PathVariable("hosId")Long hosId){
        appointmentService.deleteAppointmentById(id);
        return "redirect:/appointments/"+hosId;
    }

    @PostMapping("/{hosId}/{appointmentId}/assignPatient")
    public String assignPatient(@PathVariable("hosId")Long hosId,
                                @PathVariable("appointmentId")Long appointmentId,
                                @ModelAttribute("patient")Patient patient) throws IOException {
        Long id = patient.getId();
        patientService.assignPatient(appointmentId,id);
        return "redirect:/appointments/"+hosId;
    }

    @PostMapping("/{hosId}/{appointmentId}/assignDoctor")
    public String assignDoctor(@PathVariable("hosId")Long hosId,
                               @PathVariable("appointmentId")Long appointmentId,
                               @ModelAttribute("doctor") Doctor doctor) throws IOException {
        Long id = doctor.getId();
        doctorService.assignDoctor(appointmentId,id);
        return "redirect:/appointments/"+hosId;
    }

    @PostMapping("/{hosId}/{appointmentId}/assignDepartment")
    public String assignDepartmentToAppointment(@PathVariable("hosId")Long hosId,
                                                @PathVariable("appointmentId")Long appointmentId,
                               @ModelAttribute("department") Department department) throws IOException {
        Long id = department.getId();
        departmentService.AssignDepartmentToAppointment(appointmentId,id);
        return "redirect:/appointments/"+hosId;
    }
}
