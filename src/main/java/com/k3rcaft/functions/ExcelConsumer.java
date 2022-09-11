package com.k3rcaft.functions;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Handle the Excel content as dynamic object
 *
 * @param <T> type of model will be parsed from Excel file
 */
@FunctionalInterface
public interface ExcelConsumer<T> {
    void modelHandler(Cell currentCell, int cellIdx, T consumer);
}