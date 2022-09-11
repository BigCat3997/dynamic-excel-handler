package com.k3rcaft.controllers;

import com.k3rcaft.models.DynamicModel;
import com.k3rcaft.services.ExcelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/excels")
public class ExcelController {

    private final ExcelService excelService;

    @RequestMapping(path = "/import",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<List<DynamicModel>> parseExcelToModels(
            @RequestPart("file") MultipartFile file) {
        var secrets = excelService.parseExcelToModels(file);
        return ResponseEntity.ok(secrets);
    }
}