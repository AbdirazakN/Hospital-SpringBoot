package hospital.api;

import hospital.model.Hospital;
import hospital.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hospitals")
public class HospitalApi {

    private final HospitalService hospitalService;

    @Autowired
    public HospitalApi(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public String getAllHospitals(Model model,@RequestParam(name = "keyWord",required = false) String keyWord){
        model.addAttribute("keyWord",keyWord);
        model.addAttribute("hospitals",hospitalService.search(keyWord));
        return "hospital/hospitals";
    }

    @GetMapping("/new")
    public String createHospital(Model model){
        model.addAttribute("newHospital",new Hospital());
        return "hospital/addHospitals";
    }

    @PostMapping("/save")
    public String saveHospital(@ModelAttribute("newHospital") Hospital hospital){
        hospitalService.saveHospital(hospital);
        return "redirect:/hospitals";
    }

    @GetMapping("{hospitalId}/delete")
    public String deleteHospitalById(@PathVariable("hospitalId") Long id){
        hospitalService.deleteHospitalById(id);
        return "redirect:/hospitals";
    }

    @GetMapping("/edit/{id}")
    public String updateHospital(@PathVariable("id")Long id,Model model){
        model.addAttribute("hospital", hospitalService.getHospitalById(id));
        return "hospital/updateHospital";
    }

    @PostMapping("/{id}/update")
    public String saveUpdateHospital(@ModelAttribute("hospital")Hospital hospital){
        hospitalService.updateHospital(hospital);
        return "redirect:/hospitals";
    }


}
