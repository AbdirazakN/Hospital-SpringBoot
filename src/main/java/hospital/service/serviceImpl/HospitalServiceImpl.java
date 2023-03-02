package hospital.service.serviceImpl;

import hospital.model.Hospital;
import hospital.repository.HospitalRepository;
import hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {
    private final HospitalRepository hospitalRepository;

    @Override
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    @Override
    public void saveHospital(Hospital hospital) {
        try {
            if (hosNameValidation(hospital.getName()) || hosAddressValidation(hospital.getAddress())) {
                hospitalRepository.save(hospital);
            } else {
                throw new Exception("Not saved...");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Hospital getHospitalById(Long id) {
        return hospitalRepository.findById(id).get();
    }

    @Override
    public void deleteHospitalById(Long id) {
        hospitalRepository.deleteById(id);
    }

    @Override
    public void updateHospital(Hospital hospital) {
        hospitalRepository.save(hospital);
    }

    @Override
    public List<Hospital> search(String keyWord) {
        if (keyWord != null && !keyWord.trim().isEmpty()) {
            return hospitalRepository.search("%" + keyWord + "%");
        } else {
            return hospitalRepository.findAll();
        }
    }

    public boolean hosNameValidation(String name) {
        try {
            if (name.equals(null) || name.length() < 3 || name.length() > 20) {
                throw new Exception("Hospital name Exception...");
            }else {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean hosAddressValidation(String address) {
        try {
            if (address.equals(null) || address.length() < 3 || address.length() > 20) {
                throw new Exception("Hospital address Exception...");
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
