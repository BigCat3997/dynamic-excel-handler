package com.k3rcaft.services.impls;

import com.k3rcaft.models.DynamicModel;
import com.k3rcaft.services.ExcelService;
import com.k3rcaft.utils.ExcelUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public List<DynamicModel> parseExcelToModels(MultipartFile multipartFile) {
        try {
            return ExcelUtil.excelToModels(multipartFile.getInputStream(),
                    DynamicModel::new,
                    (cell, cellIdx, secret) -> {
                        switch (cellIdx) {
                            case 0:
                                secret.setName(cell.getStringCellValue());
                                break;
                            case 1:
                                secret.setValue(cell.getStringCellValue());
                                break;
                            default:
                                break;
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse excel data: " + e.getMessage());
        }
    }
}
