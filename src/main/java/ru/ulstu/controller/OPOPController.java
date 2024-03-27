package ru.ulstu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.dto.OPOPDto;
import ru.ulstu.mapper.OPOPMapper;
import ru.ulstu.service.OPOPService;

import java.util.List;

@RestController
@RequestMapping("/opop")
public class OPOPController {
    @Autowired
    private OPOPService opopService;
    @Autowired
    private OPOPMapper opopMapper;

    @GetMapping
    public List<OPOPDto> findAllOpops(){
        return opopService.findAllOpops().stream()
                .map(opop -> opopMapper.toOPOPDto(opop))
                .toList();
    }

    @GetMapping("/{id}")
    public OPOPDto findOpop(@PathVariable Long id){
        return opopMapper.toOPOPDto(opopService.findOpopById(id));
    }

    @PostMapping
    public OPOPDto createOpop(@RequestParam String name, @RequestParam Long userId){
        return opopMapper.toOPOPDto(opopService.addOpop(name, userId));
    }

    @PutMapping("/{id}")
    public OPOPDto updateOpop(@PathVariable Long id, @RequestParam String name, @RequestParam Long userId){
        return opopMapper.toOPOPDto(opopService.editOPOP(id, name, userId));
    }

    @DeleteMapping("/{id}")
    public void deleteOpop(@PathVariable Long id){
        opopService.deleteOpop(id);
    }

    @DeleteMapping
    public void deleteAllOpops(){
        opopService.deleteAllOpops();
    }
}
