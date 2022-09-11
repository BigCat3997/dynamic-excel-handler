# dynamic-excel-handler

Sample which parse an Excel file as dynamic object.

```java
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
```


```java
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
```
