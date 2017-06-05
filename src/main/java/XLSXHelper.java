import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

class XLSXHelper {

    public static void setCellValue(int row, String columnName, String value, int headRow, int sheet, XSSFWorkbook workBook) throws Exception {
        int column = getColumnPlaceByHeaderName(workBook, sheet, columnName, headRow);
        setCellValue(workBook.getSheetAt(sheet).getRow(row).getCell(column), value);
    }

    public static void saveFile(String filePath, XSSFWorkbook workBook) throws IOException {

        FileOutputStream outputStream = new FileOutputStream(new File(filePath));
        workBook.write(outputStream);
        outputStream.close();

    }

    private static ArrayList<String> getColNames(int sheetPlace, XSSFWorkbook workBook, int headRow) {
        workBook.getForceFormulaRecalculation();
        XSSFSheet sheet = workBook.getSheetAt(sheetPlace);
        Iterator<Row> rowIterator = sheet.iterator();
        ArrayList<String> headers = new ArrayList<>();
        while (rowIterator.hasNext() && headRow != 0) {
            Row row = rowIterator.next();
            if ((headRow--) == 1) {

                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    headers.add(cell.getStringCellValue());
                }
            }
        }
        return headers;
    }

    private static void setCellValue(XSSFCell cell, String value) throws Exception {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cell.getRichStringCellValue().setString(value);
                break;

            case Cell.CELL_TYPE_NUMERIC:
                if (value.trim().isEmpty() || value.equals("-")) {
                    cell.getCellStyle().setDataFormat(Cell.CELL_TYPE_STRING);
                    cell.setCellValue(value);
                } else {
                    cell.setCellValue(Double.parseDouble(value));
                }
                break;

            case Cell.CELL_TYPE_BOOLEAN:
                cell.setCellValue(Boolean.parseBoolean(value));
                break;
            case Cell.CELL_TYPE_BLANK:
                cell.setCellValue(value);
                break;
        }
    }

    private static int getColumnPlaceByHeaderName(XSSFWorkbook workBook, int sheetPlace, String columnName, int headRow) throws Exception {
        ArrayList<String> headers = getColNames(sheetPlace, workBook, headRow);
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).equals(columnName)) {
                return i;
            }
        }
        throw new Exception("Not found header - " + columnName);
    }
}