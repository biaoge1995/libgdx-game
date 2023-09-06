package org.cbzmq.game.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;

/**
 * @author chenbiao
 * @date 2020-11-16 23:21
 */
public class ExcelUtils {

    public static XSSFWorkbook getBook(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        XSSFWorkbook book = new XSSFWorkbook(fileInputStream);
        return book;
    }

    public static void writeFlush(XSSFWorkbook wb,String filePath) throws IOException {
        OutputStream os = new FileOutputStream(new File(filePath));
        wb.write(os);
        os.close();
    }



    /**
     * 根据行和列的索引获取单元格的数据
     *
     * @param row
     * @param column
     * @return
     */
    public static String getExcelDateByIndex(XSSFSheet sheet, int row, int column) {
        XSSFRow row1 = sheet.getRow(row);
        if (column >= row1.getFirstCellNum() && column < row1.getLastCellNum()) {
            String cell = row1.getCell(column).toString();
            return cell;
        }
        return null;
    }

    public static void getCell(XSSFSheet sheet, int row) {
        XSSFRow row1 = sheet.getRow(row);
        Iterator<Cell> cellIterator = row1.cellIterator();
        while (cellIterator.hasNext()) {
            Cell next = cellIterator.next();
            System.out.println(next.toString());
        }
    }

    /**
     * 根据某一列值为“******”的这一行，来获取该行第x列的值
     *
     * @param caseName
     * @param currentColumn 当前单元格列的索引
     * @param targetColumn  目标单元格列的索引
     * @return
     */
    public static String getCellByCaseName(XSSFSheet sheet, String caseName, int currentColumn, int targetColumn) {
        String operateSteps = "";
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; i++) {
            XSSFRow row = sheet.getRow(i);
            String cell = row.getCell(currentColumn).toString();
            if (cell.equals(caseName)) {
                operateSteps = row.getCell(targetColumn).toString();
                break;
            }
        }
        return operateSteps;
    }

    //打印excel数据
    public void readExcelData(XSSFSheet sheet) {
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; i++) {
            //获取列数
            XSSFRow row = sheet.getRow(i);
            int columns = row.getPhysicalNumberOfCells();
            for (int j = 0; j < columns; j++) {
                String cell = row.getCell(j).toString();
                System.out.println(cell);
            }
        }
    }

    //测试方法
    public static void main(String[] args) throws IOException {
        XSSFWorkbook book = getBook("C:\\Users\\chenbiao\\Desktop\\导入修改.xlsx");
        //获取第二行第4列
//        String cell2 = getExcelDateByIndex(sheet1,0, 1);
//        //根据第3列值为“customer23”的这一行，来获取该行第2列的值
//        String cell3 = getCellByCaseName(sheet1,"customer23", 2,1);
//        System.out.println(cell2);
//        System.out.println(cell3);
    }
}
