package ru.ulstu.service.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.model.OPOP;
import ru.ulstu.repository.OPOPRepository;

import java.util.Optional;

@Service
public class OPOPService {
    @Autowired
    private OPOPRepository opopRepository;

    @Transactional(readOnly = true)
    public OPOP findOPOPById(Long id){
        final Optional<OPOP> opop = opopRepository.findById(id);
        return opop.orElseThrow(() -> new OPOPNotFoundException(id));
    }
}
