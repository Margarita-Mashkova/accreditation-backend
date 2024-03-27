package ru.ulstu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.dto.ValueDto;
import ru.ulstu.dto.ValueIdDto;
import ru.ulstu.mapper.ValueMapper;
import ru.ulstu.service.ValueService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/value")
public class ValueController {
    @Autowired
    private ValueService valueService;
    @Autowired
    private ValueMapper valueMapper;

    @GetMapping("/all")
    public List<ValueDto> findAllValues(){
        return valueService.findAllValues().stream()
                .map(value -> valueMapper.toValueDto(value))
                .toList();
    }

    @GetMapping("/filtered")
    public List<ValueDto> findValuesByOpopAndDate(@RequestParam Long opopId,
                                                  @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd") Date date){
        return valueService.findValuesByOpopAndDate(opopId, date).stream()
                .map(value -> valueMapper.toValueDto(value))
                .toList();
    }

    @GetMapping
    public ValueDto findValue(@RequestParam Long opopId, @RequestParam String variableKey,
                              @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd") Date date){
        ValueIdDto valueIdDto = new ValueIdDto(opopId, variableKey, date);
        return valueMapper.toValueDto(valueService.findValue(valueMapper.fromValueIdDto(valueIdDto)));
    }

    @PostMapping
    public ValueDto createValue(@RequestBody ValueDto valueDto){
        return valueMapper.toValueDto(valueService.addValue(
                valueMapper.fromValueIdDto(valueDto.getId()), valueDto.getValue()));
    }

    @PutMapping
    public ValueDto updateValue(@RequestBody ValueDto valueDto){
        return valueMapper.toValueDto(valueService.updateValue(
                valueMapper.fromValueIdDto(valueDto.getId()), valueDto.getValue()));
    }

    @DeleteMapping
    public void deleteValue(@RequestBody ValueIdDto valueIdDto){
        valueService.deleteValue(valueMapper.fromValueIdDto(valueIdDto));
    }

    @DeleteMapping("/all")
    public void deleteAllValues(){
        valueService.deleteAllValues();
    }
}
