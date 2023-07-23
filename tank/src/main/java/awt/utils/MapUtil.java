package awt.utils;

import awt.model.Constans;
import awt.model.domain.Element;
import awt.game.GameMapContext;
import awt.proto.enums.ElementType;
import awt.proto.enums.GameRole;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author chenbiao
 * @date 2020-12-23 23:51
 */
public class MapUtil {

    public static void main(String[] args) throws Exception {
        loadGameMap();
    }

    public static GameMapContext loadGameMap(String mapName) throws Exception {
        return loadGameMap(ConfigUtil.getItem("excel_path"),mapName);
    }

    public static GameMapContext loadGameMap() throws Exception {
       return loadGameMap(ConfigUtil.getItem("excel_path"),"sheet2");
    }
    public static GameMapContext loadGameMap(String excelPath, String sheetName) throws Exception {

        XSSFWorkbook book = null;
        Vector<Element> elementList = new Vector<Element>();
        try {
            book = ExcelUtils.getBook(excelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (book == null) {
            return null;
        }
        XSSFSheet sheet = book.getSheet(sheetName);
        if(sheet==null){
            throw new Exception("地图文件不存在"+ConfigUtil.getItem("excel_path")+" sheetName:"+sheetName);
        }
        Iterator<Row> rowIterator = sheet.rowIterator();
        int rowIndex = 0;
        int maxX = 0;
        int maxY = 0;
        Vector<Vector<Element>> element2D = new Vector<>();
        System.out.println("地图最大边界"+maxX+","+maxY);

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() > 0) {
                int cellIndex = 0;
                Iterator<Cell> cellIterator = row.iterator();
                Vector<Element> element1D = new Vector<Element>();
                while (cellIterator.hasNext()) {
                    Cell next = cellIterator.next();
                    try {
                        String s = next.toString();
                        int code = (int) Double.parseDouble(s);
                        ElementType elementType = ElementType.valueOf(code);
//                        ElementType elementType = ElementType.parseInt(code);
                        Class<?>[] classes = {int.class, int.class, int.class, int.class};
                        int x = cellIndex * (Constans.otherWidth + 1);
                        int y = rowIndex * (Constans.otherHeight + 1);
                        if(maxX < x){
                            maxX = x+(Constans.otherWidth + 1);
                        }
                        if(maxY < y){
                            maxY = y+(Constans.otherHeight + 1);
                        }
                        Element loadElementClass = LoadClassUtils.loadElementClass(elementType, classes, x+Constans.otherWidth/2, y+Constans.otherHeight/2, Constans.otherWidth, Constans.otherHeight);
                        loadElementClass.setPlayId(elementType.getNumber());
                        loadElementClass.setXCoordinate(loadElementClass.getMapXCoordinate());
                        loadElementClass.setYCoordinate(loadElementClass.getMapYCoordinate());
                        loadElementClass.setGameRole(GameRole.SCENE);
                        //

                        elementList.add(loadElementClass);
                        element1D.add(loadElementClass);

                    } catch (NumberFormatException e) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cellIndex++;
                }
                element2D.add(element1D);
                System.out.println(elementList.size());
            }
            rowIndex++;
        }

        GameMapContext gameMapContext = new GameMapContext();
        gameMapContext.addElements(elementList);
        gameMapContext.setEleMap(element2D);
        gameMapContext.setWide(maxX);
        gameMapContext.setHeight(maxY);
        return gameMapContext;
    }

}
