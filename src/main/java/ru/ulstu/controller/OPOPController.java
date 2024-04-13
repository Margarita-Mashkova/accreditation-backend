package ru.ulstu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public List<OPOPDto> findAllOpops(){
        return opopService.findAllOpops().stream()
                .map(opop -> opopMapper.toOPOPDto(opop))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/page")
    public List<OPOPDto> findAllOpopsByPage(@RequestParam int pageNumber){
        return opopService.findAllOpopsByPage(pageNumber).stream()
                .map(opop -> opopMapper.toOPOPDto(opop))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/pages")
    public int getAmountPages(){
        return opopService.getAmountPages();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{id}")
    public OPOPDto findOpop(@PathVariable Long id){
        return opopMapper.toOPOPDto(opopService.findOpopById(id));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/levels")
    public List<String> findAllOpopsLevels(){
        return opopService.findAllOpopLevels();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public OPOPDto createOpop(@RequestParam String name, @RequestParam String level, @RequestParam Long userId){
        return opopMapper.toOPOPDto(opopService.addOpop(name, level, userId));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    public OPOPDto updateOpop(@PathVariable Long id, @RequestParam String name, @RequestParam String level,
                              @RequestParam Long userId){
        return opopMapper.toOPOPDto(opopService.editOPOP(id, name, level, userId));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    public void deleteOpop(@PathVariable Long id){
        opopService.deleteOpop(id);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping
    public void deleteAllOpops(){
        opopService.deleteAllOpops();
    }
}
