package com.k3rcaft.utils;

import com.k3rcaft.functions.ExcelConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
public final class ExcelUtil {

    public final static String excelFormatType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    private ExcelUtil() {
    }

    public static boolean isExcelFormat(MultipartFile file) {
        return excelFormatType.equals(file.getContentType());
    }

    public static <T> List<T> excelToModels(InputStream inputStream, Supplier<T> supplier, ExcelConsumer<T> excelConsumer) {
        try {
            var workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // get 0 index as default
            Iterator<Row> rows = sheet.iterator();
            var models = new ArrayList<T>();
            int rowNumber = 0;

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                T model = supplier.get();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    excelConsumer.modelHandler(currentCell, cellIdx, model);
                    cellIdx++;
                }

                models.add(model);
            }

            workbook.close();
            log.info("Converted model: {}", models);
            return models;
        } catch (IOException e) {
            throw new RuntimeException("Fail to parse Excel file: " + e.getMessage());
        }
    }
}

