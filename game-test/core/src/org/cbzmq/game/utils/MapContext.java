package org.cbzmq.game.utils;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Iterator;

public class MapContext {

    private XSSFSheet mapSheet;
    private XSSFSheet widthAnaHeightSheet;
    private XSSFWorkbook workbook;

    private String excelPath;
    private byte[][] cells;

    private int width;
    private int height;

    private XSSFRow currentRow;


    public MapContext(String excelPath) throws Exception {
        XSSFWorkbook book = null;

        try {
            book = ExcelUtils.getBook(excelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (book == null) {
            throw new Exception("地图文件不存在" + excelPath);
        }
        XSSFSheet mapSheet = book.getSheet("map");
        if (mapSheet == null) {
            throw new Exception("地图文件不存在" + excelPath + " sheetName:map");
        }
        XSSFSheet widthAnaHeightSheet = book.getSheet("xy");
        if (widthAnaHeightSheet == null) {
            throw new Exception("地图文件不存在" + excelPath + " sheetName:xy");
        }

        this.excelPath = excelPath;
        this.mapSheet = mapSheet;
        this.widthAnaHeightSheet = widthAnaHeightSheet;
        this.workbook = book;
    }

//    public  MapContext (String excelPath, String sheetName, int width, int height) throws Exception {
//
//        this.cells = new byte[width][height];
//    }

    public void createRow(int row) {
        currentRow = mapSheet.createRow(row);
    }

    public void writeCell(int cellIndex,String data) {
        currentRow.createCell(cellIndex).setCellValue(data);
    }

    public void writeXy(int x, int y) {
        XSSFRow row = widthAnaHeightSheet.createRow(0);
        row.createCell(0).setCellValue(x);
        row.createCell(1).setCellValue(y);
    }

    public void finishFlush() {
        try {
            ExcelUtils.writeFlush(workbook, excelPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGameMap() {
        XSSFRow row1 = widthAnaHeightSheet.getRow(0);
        this.width = (int) Double.parseDouble(row1.getCell(0).toString());
        this.height = (int) Double.parseDouble(row1.getCell(1).toString());
        cells = new byte[width][height];
        Iterator<Row> rowIterator = mapSheet.rowIterator();
        int rowIndex = 0;

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() > 0) {
                int cellIndex = 0;
                Iterator<Cell> cellIterator = row.iterator();

                while (cellIterator.hasNext()) {
                    Cell next = cellIterator.next();
                    try {
                        String s = next.toString();
                        byte value = (byte) Double.parseDouble(s);
                        cells[cellIndex ][rowIndex] = value;
                    } catch (NumberFormatException e) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cellIndex++;
                }

            }
            rowIndex++;
        }
        System.out.println(cells);
    }

    public byte getCell(int x, int y) {
        if (x < 0 || x >= width) return 0;
        if (y < 0 || y >= height) return 0;
        return cells[x][y];
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
