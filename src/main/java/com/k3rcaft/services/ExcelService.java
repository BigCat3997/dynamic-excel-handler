package com.k3rcaft.services;

import com.k3rcaft.models.DynamicModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelService {

    List<DynamicModel> parseExcelToModels(MultipartFile multipartFile);
}