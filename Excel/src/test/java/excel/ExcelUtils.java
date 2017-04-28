package excel;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * implement the following examples:
 * 1) month outcome new month;
 * 2) fill in gym sheet by formula;
 * 3) word
 */

public class ExcelUtils {
    private static final Logger log = Logger.getLogger(ExcelUtils.class.getName());
    public static final String pathToBuild = "C:\\ProgramData\\JetBrains\\TeamCity\\system\\artifacts\\CdsAutomation\\Check what spot are framed our offer (Chrome_ 20 invocation)";
    //    public static final String pathToBuild = "C:\\Users\\kgr\\Desktop\\test\\builds";
//    public static final String pathToWorkbook = "C:\\ProgramData\\JetBrains\\TeamCity\\system\\artifacts\\CdsAutomation\\report\\fluent_automation_report.xlsx";
    public static final String pathToWorkbook = "C:\\Users\\alex\\Dropbox\\Fluent Automation Reports\\fluent_automation_report.xlsx";
    //        public static final String pathToWorkbook = "C:\\Users\\kgr\\Desktop\\test\\online\\fluent_automation_report.xlsx";
    private static List<String> headerList = Arrays.asList("Date", "Version", "Failed/Passed", "Framed at position", "Details/Screenshot");

    public static void createWorkbook(String path) {
        try {
            if (new File(path).exists()) return;
            Workbook workbook = new XSSFWorkbook();
            FileOutputStream fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            log.info("Create workbook by path\t" + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createSheetByName(Workbook workbook, String sheetName) {
        if (workbook.getSheet(sheetName) == null) {
            Sheet sheet = workbook.createSheet(sheetName);
            sheet.setDefaultColumnWidth(25);
            CellStyle style = getCellStyle(workbook);
            Row row = sheet.createRow(0);
            row.setHeightInPoints(5 * sheet.getDefaultRowHeightInPoints());
            for (int i = 0; i < headerList.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(headerList.get(i));
                cell.setCellStyle(style);
            }
            writeToWorkbook(workbook, pathToWorkbook);
            log.info(String.format("Create workbook sheet '%s'", sheetName));
        }
    }

    // add method to save screen path
    public static void addImage(Workbook workbook, Sheet sheet, String pathToScreen, int rowNum) {
        try {
            //add picture data to this workbook
            InputStream is = new FileInputStream(pathToScreen);
            byte[] bytes = IOUtils.toByteArray(is);
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            is.close();
            CreationHelper helper = workbook.getCreationHelper();
            // Create the drawing patriarch.  This is the top level container for all shapes.
            Drawing drawing = sheet.createDrawingPatriarch();
            //add a picture shape
            ClientAnchor anchor = helper.createClientAnchor();
            //set top-left corner of the picture,
            //subsequent call of Picture#resize() will operate relative to it
            anchor.setCol1(2);
            anchor.setRow1(rowNum);
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            //auto-size picture relative to its top-left corner
            // add method to replace dynamically
            pict.resize(0.4);
            pict.resize();
            writeToWorkbook(workbook, pathToWorkbook);
        } catch (IOException e) {
            log.info("Unable to set image to cell cause\n" + e.getMessage());
        }
    }

    public static void readWorkbook() {

    }

    public static void readCellsData() {

    }

    public static void updateSheet() {
        Workbook workbook = getWorkbook(pathToWorkbook);
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        // update cell
        if (row.getCell(0) != null) {
            row.getCell(0).setCellValue("updated value");
            sheet.autoSizeColumn(0);
        }
        writeToWorkbook(workbook, pathToWorkbook);
    }

    public static Workbook getWorkbook(String path) {
        createWorkbook(path);
        try {
            return WorkbookFactory.create(new FileInputStream(path));
        } catch (IOException | InvalidFormatException e) {
            throw new Error("Unable to get workbook cause of\n" + e.getMessage());
        }
    }

    public static CellStyle getCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }

    public static Font getFont(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Calibri");
        font.setBold(true);
        font.setFontHeight((short) 12);
        return font;
    }

    public static void adjustColumnWidth(Sheet sheet) {
        Iterator<Cell> cellIterator = sheet.getRow(0).cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            sheet.autoSizeColumn(cell.getColumnIndex());
        }
    }

    // Write the output to a file
    public static void writeToWorkbook(Workbook workbook, String pathToWorkbook) {
        try {
            FileOutputStream fileOut = new FileOutputStream(pathToWorkbook);
            workbook.write(fileOut);
            fileOut.close();
            log.info("Append data to workbook\t" + pathToWorkbook);
        } catch (IOException e) {
            throw new Error("Unable to save changes to workbook\n" + e.getMessage());
        }
    }

    // framed specific
    public static List<String> getHeadersList(Sheet sheet) {
        Row row = sheet.getRow(0);
        List<String> headerList = new ArrayList<>(row.getPhysicalNumberOfCells());
        for (Iterator<Cell> cellIterator = (row).cellIterator(); cellIterator.hasNext(); ) {
            headerList.add(cellIterator.next().getStringCellValue());
        }
        return headerList;
    }

    public static void main(String[] args) throws IOException {
//        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(new File("C:\\Users\\kgr\\Downloads\\Gym\\xlsx")));
        try {
            Workbook workbook = new XSSFWorkbook(new File("C:\\Users\\kgr\\Downloads\\Gym.xlsx"));
            Sheet sheet = workbook.getSheet("volume_nikolay");

            CellReference ref = new CellReference("B12");
            Row r = sheet.getRow(ref.getRow());
            if (r != null) {
                Cell c = r.getCell(ref.getCol());
            }

            List<Hyperlink> hyperLinks = (List<Hyperlink>) sheet.getHyperlinkList();
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Comment comment = cell.getCellComment();
                    Hyperlink hyperlink = cell.getHyperlink();
                    if (comment != null)
                        System.out.println("Comment = " + comment.getString().getString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
