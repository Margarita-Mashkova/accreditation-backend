package ru.ulstu.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportFileDto {
    private String filename;
    private byte[] data;
}
