package ru.ulstu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.dto.IndicatorDto;
import ru.ulstu.dto.VariableDto;
import ru.ulstu.mapper.VariableMapper;
import ru.ulstu.service.VariableService;

import java.util.List;

@RestController
@RequestMapping("/variable")
public class VariableController {
    @Autowired
    private VariableService variableService;

    @Autowired
    private VariableMapper variableMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public List<VariableDto> findAllVariables(){
        return variableService.findAllVariables().stream()
                .map(variable -> variableMapper.toVariableDto(variable))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/page")
    public List<VariableDto> findAllIndicatorsByPage(@RequestParam int pageNumber){
        return variableService.findAllVariablesByPage(pageNumber).stream()
                .map(variable -> variableMapper.toVariableDto(variable))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/pages")
    public int getAmountPages(){
        return variableService.getAmountPages();
    }


    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{key}")
    public VariableDto findVariable(@PathVariable String key){
        return variableMapper.toVariableDto(variableService.findVariableByKey(key));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public VariableDto createVariable(@RequestBody VariableDto variableDto){
        return variableMapper.toVariableDto(variableService.addVariable(variableDto.getKey(), variableDto.getName()));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{key}")
    public VariableDto updateVariable(@PathVariable String key, @RequestParam String name){
        return variableMapper.toVariableDto(variableService.editVariable(key, name));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{key}")
    public void deleteVariable(@PathVariable String key){
        variableService.deleteVariable(key);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping
    public void deleteAllVariables(){
        variableService.deleteAllVariables();
    }
}
