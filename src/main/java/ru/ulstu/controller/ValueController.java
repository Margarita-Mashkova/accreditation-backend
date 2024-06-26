package ru.ulstu.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ulstu.dto.ReportFileDto;
import ru.ulstu.dto.ValueDto;
import ru.ulstu.dto.ValueIdDto;
import ru.ulstu.mapper.ValueMapper;
import ru.ulstu.service.ValueService;

import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/value")
public class ValueController {
    @Autowired
    private ValueService valueService;
    @Autowired
    private ValueMapper valueMapper;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/all")
    public List<ValueDto> findAllValues(){
        return valueService.findAllValues().stream()
                .map(value -> valueMapper.toValueDto(value))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/filtered")
    public List<ValueDto> findValuesByOpopAndDate(@RequestParam Long opopId,
                                                  @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd") Date date){
        return valueService.findValuesByOpopAndDate(opopId, date).stream()
                .map(value -> valueMapper.toValueDto(value))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/by-opop")
    public List<Date> findDatesByOpop(@RequestParam Long opopId){
        return valueService.findDatesByOpop(opopId);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ValueDto findValue(@RequestParam Long opopId, @RequestParam String variableKey,
                              @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd") Date date){
        ValueIdDto valueIdDto = new ValueIdDto(opopId, variableKey, date);
        return valueMapper.toValueDto(valueService.findValue(valueMapper.fromValueIdDto(valueIdDto)));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ValueDto createValue(@RequestBody ValueDto valueDto){
        return valueMapper.toValueDto(valueService.addValue(
                valueMapper.fromValueIdDto(valueDto.getId()), valueDto.getValue()));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/list")
    public List<ValueDto> addValuesList(@RequestBody List<ValueDto> valueDtoList) {
        return valueService.addValuesList(valueDtoList.stream()
                .map(valueDto -> valueMapper.fromValueDto(valueDto))
                .toList())
                .stream()
                .map(value -> valueMapper.toValueDto(value))
                .toList();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping
    public ValueDto updateValue(@RequestBody ValueDto valueDto){
        return valueMapper.toValueDto(valueService.updateValue(
                valueMapper.fromValueIdDto(valueDto.getId()), valueDto.getValue()));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping
    public void deleteValue(@RequestBody ValueIdDto valueIdDto){
        valueService.deleteValue(valueMapper.fromValueIdDto(valueIdDto));
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/all")
    public void deleteAllValues(){
        valueService.deleteAllValues();
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/patternFile")
    public ReportFileDto getPatternFile(@RequestParam Long opopId,
                                        @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd") Date date){
        return valueService.generatePatternFile(opopId, date);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(path = "/readFile",  consumes = MULTIPART_FORM_DATA_VALUE)
    public List<ValueDto> readValuesFromFile(@RequestParam MultipartFile file,
                                             @RequestParam Long opopId,
                                             @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd") Date date){
        return valueService.readValuesFromFile(file, opopId, date);
    }
}
