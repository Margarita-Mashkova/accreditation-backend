package ru.ulstu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public List<VariableDto> findAllVariables(){
        return variableService.findAllVariables().stream()
                .map(variable -> variableMapper.toVariableDto(variable))
                .toList();
    }

    @GetMapping("/{key}")
    public VariableDto findVariable(@PathVariable String key){
        return variableMapper.toVariableDto(variableService.findVariableByKey(key));
    }

    @PostMapping
    public VariableDto createVariable(@RequestBody VariableDto variableDto){
        return variableMapper.toVariableDto(variableService.addVariable(variableDto.getKey(), variableDto.getName()));
    }

    @PutMapping("/{key}")
    public VariableDto updateVariable(@PathVariable String key, @RequestParam String name){
        return variableMapper.toVariableDto(variableService.editVariable(key, name));
    }

    @DeleteMapping("/{key}")
    public void deleteVariable(@PathVariable String key){
        variableService.deleteVariable(key);
    }

    @DeleteMapping
    public void deleteAllVariables(){
        variableService.deleteAllVariables();
    }
}
