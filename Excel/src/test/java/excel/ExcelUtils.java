package excel;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
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

    public static void addDataToSheet(PollData pollData) {
        if (pollData == null) return;
        String type = pollData.isMobile() ? "Mobile" : "Desktop";
        createSheetByName(getWorkbook(pathToWorkbook), type);
        Workbook workbook = getWorkbook(pathToWorkbook);
        Sheet sheet = workbook.getSheet(type);
        List<String> headerList = getHeadersList(sheet);
        Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        Row headerRow = sheet.getRow(0);
        CellStyle style = getCellStyle(workbook);
        // Date
        Cell cell = row.createCell(0);
        cell.setCellValue(DataHelper.getDateByFormatSimple(DataHelper.REPORT_PATTERN));
        cell.setCellStyle(style);
        // position
        cell = row.createCell(1);
        cell.setCellValue(pollData.getPosition());
        cell.setCellStyle(style);
        for (Map.Entry<String, String> question : pollData.getPollMap().entrySet()) {
            String questionValue = question.getKey().replaceAll("<br>", "");
            // question
            if (!headerList.contains(questionValue)) {
                cell = headerRow.createCell(headerList.size());
                cell.setCellValue(questionValue);
                cell.setCellStyle(style);
                headerList.add(questionValue);
            }
            // answer
            cell = row.createCell(headerList.indexOf(questionValue));
            cell.setCellValue(question.getValue());
            cell.setCellStyle(style);
        }
        // add image
//        addImage(workbook, sheet, pollData.getScreenPath(), row.getRowNum());
        // merge cells
//        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), sheet.getPhysicalNumberOfRows() - 1, 0, 0));
        // adjust width of the first column
//        adjustColumnWidth(sheet);
        writeToWorkbook(workbook, pathToWorkbook);
    }

    public static void addDataToSheet(PollData pollData, Sheet sheet, CellStyle style, Row row) {
        if (pollData == null || !pollData.isPollMapExists()) return;
        List<String> headerList = getHeadersList(sheet);
        Row headerRow = sheet.getRow(0);
        // Date
        Cell cell = row.createCell(0);
        cell.setCellValue(pollData.getPollDate());
        cell.setCellStyle(style);
        // version
        cell = row.createCell(1);
        cell.setCellValue(pollData.getVersion());
        cell.setCellStyle(style);
        // status possibly extend with column of unreliable automation results
        cell = row.createCell(2);
        cell.setCellValue(pollData.getStatus());
        cell.setCellStyle(style);
        // position
        if (pollData.isSuccess()) {
            cell = row.createCell(3);
            cell.setCellValue(pollData.getPosition());
            cell.setCellStyle(style);
        }
        for (Map.Entry<String, String> question : pollData.getPollMap().entrySet()) {
            String questionValue = question.getKey().replaceAll("<br>", "");
            // question
            if (!headerList.contains(questionValue)) {
                cell = headerRow.createCell(headerList.size());
                cell.setCellValue(questionValue);
                cell.setCellStyle(style);
                headerList.add(questionValue);
            }
            // answer
            cell = row.createCell(headerList.indexOf(questionValue));
            cell.setCellValue(question.getValue());
            cell.setCellStyle(style);
        }
    }

    public static void addDataToSheet(BuildData buildData) {
        if (buildData == null || !buildData.isPollDataPresent()) return;
        String sheetName = DataHelper.getFullMonthByDate(DataHelper.REPORT_PATTERN, buildData.getBuildDate());
//        String sheetName =  buildData.getFormType();
        createSheetByName(getWorkbook(pathToWorkbook), sheetName);
        Workbook workbook = getWorkbook(pathToWorkbook);
        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        CellStyle style = getCellStyle(workbook);
        // create hyperlink
        try {
            Cell hyperLinkCell = row.createCell(4);
            hyperLinkCell.setCellValue("ReportNG report");
            Hyperlink link = workbook.getCreationHelper().createHyperlink(Hyperlink.LINK_URL);
            link.setAddress(buildData.getBuildLink());
            hyperLinkCell.setHyperlink(link);
            hyperLinkCell.setCellStyle(style);
        } catch (Exception e) {
            log.severe("Unable to make hyperlink cell\n" + e.getMessage());
        }
        // polldata
 /*       String type = buildData.getFormType();
        int mergeFrom = row.getRowNum();*/
        for (int i = 0; i < buildData.getPollDataList().size(); i++) {
            Row row1 = i > 0 ? sheet.createRow(row.getRowNum() + i) : row;
            addDataToSheet(buildData.getPollDataList().get(i), sheet, style, row1);
          /*  if (!type.equals(buildData.getPollDataList().get(i).getVersion())) {
                // merge version while the same - improvement
                type = buildData.getPollDataList().get(i).getVersion();
                sheet.addMergedRegion(new CellRangeAddress(mergeFrom, sheet.getPhysicalNumberOfRows() - 1, 1, 1));
                mergeFrom = sheet.getPhysicalNumberOfRows();
            }*/
        }
        // merge cells
//        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), sheet.getPhysicalNumberOfRows() - 1, 0, 0));
        if (row.getRowNum() < sheet.getPhysicalNumberOfRows() - 1) {
            sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), sheet.getPhysicalNumberOfRows() - 1, 4, 4));
        }
        // adjust width of the first column
//        adjustColumnWidth(sheet);
        writeToWorkbook(workbook, pathToWorkbook);
    }

    public static void addDataToSheet() {
        File file = new File(pathToWorkbook);
        if (file.exists()) file.delete();
        // 20 invocation
        File builds = new File(pathToBuild);
        for (File build : builds.listFiles(File::isDirectory)) {
            addDataToSheet(HtmlUtils.getBuildDataFromHTML(build.getAbsolutePath() + "\\testng-report\\html\\suite1_test1_results.html"));
        }
        // 50 invocation
        builds = new File(pathToBuild.replace("20", "50"));
        for (File build : builds.listFiles(File::isDirectory)) {
            addDataToSheet(HtmlUtils.getBuildDataFromHTML(build.getAbsolutePath() + "\\testng-report\\html\\suite1_test1_results.html"));
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

    public static void main(String[] args) {
        addDataToSheet();
//        ExcelUtils.addDataToSheet(new PollData(ContactFactory.getContact()));
//        createWorkbook("C:\\Users\\kgr\\Desktop\\test\\workbook.xlsx");
//        addDataToSheet();
//        addSheetAfterMethod();
//        updateSheet();
    }
}
