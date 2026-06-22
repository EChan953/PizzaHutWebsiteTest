package org.test.utils;

import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.util.*;

public class ExcelReader {
    public static List<Map<String, String>> readExcelData (String filePath, String sheetName){

        //Initialize List of Map
        List<Map<String, String>> dataList = new ArrayList<>();

       try{
           FileInputStream fis = new FileInputStream(filePath);
           Workbook workbook = WorkbookFactory.create(fis);
           Sheet sheet = workbook.getSheet(sheetName);

           Row headerRow = sheet.getRow(0);
           int colCount = headerRow.getPhysicalNumberOfCells();
           DataFormatter formatter = new DataFormatter();

           for (int i = 1; i <= sheet.getLastRowNum(); i++) {
               Row row = sheet.getRow(i);
               //skip null rows
               if (row == null) continue;
               Map<String, String> rowData = new HashMap<>();
               for (int j = 0; j < colCount; j++) {
                   String header = formatter.formatCellValue(headerRow.getCell(j));
                   String value = formatter.formatCellValue(row.getCell(j));
                   rowData.put(header, value);
               }
               dataList.add(rowData);
           }
           workbook.close();
           fis.close();
       }
       catch(Exception e){
           e.printStackTrace();
       }
       return dataList;
    }


    public static List<Map<String, String>> filterByTestCase(
            List<Map<String, String>> dataList,
            String testCaseID) {

        List<Map<String, String>> filtered = new ArrayList<>();

        for (Map<String, String> data : dataList) {

            String testCaseCell = data.get("Test Case ID");

            if (testCaseCell != null &&
                    testCaseCell.contains(testCaseID)) {

                filtered.add(data);
            }
        }

        return filtered;
    }

}
