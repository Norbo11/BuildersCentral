package com.github.norbo11.topbuilders.util.helpers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.stage.FileChooser;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PaperSize;
import org.apache.poi.ss.usermodel.ShapeTypes;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFChildAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShapeGroup;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSimpleShape;
import org.apache.poi.xssf.usermodel.XSSFTextBox;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.norbo11.topbuilders.models.Job;
import com.github.norbo11.topbuilders.models.JobGroup;
import com.github.norbo11.topbuilders.models.Project;
import com.github.norbo11.topbuilders.models.enums.QuoteSettingType;
import com.github.norbo11.topbuilders.util.Resources;

public class ExcelUtil {

    public static void exportToExcel(Project project) {
        try {            
            //Pick a file to save to
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(Resources.getResource("quotes.export.save"));
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel workbook", "*.xlsx")
            );
            //File file = fileChooser.showSaveDialog(Main.getMainStage());
            File file = new File("C:/Users/Norbo11/Desktop/quote.xlsx");
            
            //Create a new workbook and a new sheet
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Quote");
            
            //Set general properties
            sheet.getPrintSetup().setLandscape(true);
            sheet.getPrintSetup().setPaperSize(PaperSize.A4_PAPER);
            
            //Create fonts   
            XSSFFont boldFont = workbook.createFont();
            boldFont.setBold(true);
            
            XSSFFont workbookHeadingFont = workbook.createFont();
            workbookHeadingFont.setFontHeightInPoints((short) 14);
            workbookHeadingFont.setBold(true);
            
            XSSFFont headingFont = workbook.createFont();
            headingFont.setFontHeightInPoints((short) 12);
            headingFont.setBold(true);
            
            //Create styles  
            XSSFCellStyle baseStyle = workbook.createCellStyle();
            baseStyle.setWrapText(true);
            
            XSSFCellStyle darkBlue = workbook.createCellStyle();
            darkBlue.cloneStyleFrom(baseStyle);
            darkBlue.setFillForegroundColor(new XSSFColor(new byte[] { (byte) 72, (byte) 119, (byte) 212 }));
            darkBlue.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            
            XSSFCellStyle lightBlue = workbook.createCellStyle();
            lightBlue.cloneStyleFrom(baseStyle);
            lightBlue.setFillForegroundColor(new XSSFColor(new byte[] { (byte) 220, (byte) 230, (byte) 241 }));
            lightBlue.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            
            XSSFCellStyle jobGroupStyle = workbook.createCellStyle();
            jobGroupStyle.cloneStyleFrom(lightBlue);
            jobGroupStyle.setFont(boldFont);
            
            XSSFCellStyle workbookHeadingStyle = workbook.createCellStyle();
            workbookHeadingStyle.cloneStyleFrom(darkBlue);
            workbookHeadingStyle.setFont(workbookHeadingFont);
            workbookHeadingStyle.setAlignment(HorizontalAlignment.CENTER);
            
            XSSFCellStyle headingStyle = workbook.createCellStyle();
            headingStyle.cloneStyleFrom(darkBlue);
            headingStyle.setFont(headingFont);
            headingStyle.setAlignment(HorizontalAlignment.CENTER);
            
            XSSFCellStyle accountingStyle = workbook.createCellStyle();
            accountingStyle.cloneStyleFrom(baseStyle);
            accountingStyle.setDataFormat((short) 44);
            
            XSSFCellStyle blueAccountingStyle = workbook.createCellStyle();
            blueAccountingStyle.cloneStyleFrom(lightBlue);
            blueAccountingStyle.setDataFormat((short) 44);
            
            //Set defaults
            sheet.setDefaultRowHeightInPoints(22.5f);

            //Column widths
            sheet.setColumnWidth(0, 30 * 255);
            sheet.setColumnWidth(1, 34 * 255);
            sheet.setColumnWidth(2, 26 * 255);
            sheet.setColumnWidth(3, 16 * 255);
            sheet.setColumnWidth(4, 16 * 255);
            sheet.setColumnWidth(5, 16 * 255);
            
            /* Begin to lay out the actual content */
            
            //Workbook heading
            XSSFRow headingRow = sheet.createRow(0);
            XSSFCell headingCell = headingRow.createCell(1);
            headingCell.setCellStyle(workbookHeadingStyle);
            headingCell.setCellValue("Price estimate for:");
            
            //Address
            XSSFCell fullNameCell = headingRow.createCell(2);
            fullNameCell.setCellValue(project.getClientFullName());
            
            for (int i = 1; i < 4; i++) {
                XSSFCell cell = sheet.createRow(i).createCell(2);
                cell.setCellStyle(lightBlue);
                switch (i) {
                    case 1: cell.setCellValue(project.getFirstLineAddress()); break;
                    case 2: cell.setCellValue(project.getSecondLineAddress()); break;
                    case 3: cell.setCellValue((project.getCity().equals("") ? "" : project.getCity() + ", ") + project.getPostcode()); break;
                }
            }
            
            //Logo area
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFShapeGroup group = drawing.createGroup(drawing.createAnchor(0, 0, 1000, 0, 4, 0, 4, 5));
            
            int picture = workbook.addPicture(Resources.getAsBytes("src/img/logo.png"), Workbook.PICTURE_TYPE_PNG);
            group.createPicture(drawing.createAnchor(0, 0, 1000, 0, 4, 0, 4, 3), picture);

            XSSFSimpleShape line = group.createSimpleShape(new XSSFChildAnchor(0, 255, 250, 255));
            line.setShapeType(ShapeTypes.LINE);
            
            //TODO fonts
            XSSFTextBox textbox = group.createTextbox(new XSSFChildAnchor(0, 250, 250, 500));
            textbox.setText(new XSSFRichTextString("Top Builders\n07540734201\npitbuilder44@googlemail.com\nwww.top-builders.org"));

            boolean descriptionsEnabled = project.getSettings().getBoolean(QuoteSettingType.JOB_DESCRIPTIONS_ENABLED);
            boolean materialsEnabled = project.getSettings().getBoolean(QuoteSettingType.MATERIALS_ENABLED);
            boolean materialsPriceEnabled = project.getSettings().getBoolean(QuoteSettingType.MATERIALS_PRICE_ENABLED);
            boolean labourPriceEnabled = project.getSettings().getBoolean(QuoteSettingType.LABOUR_PRICE_ENABLED);
            boolean jobGroupsEnabled = project.getSettings().getBoolean(QuoteSettingType.GROUPS_ENABLED);
            
            //Gap
            sheet.createRow(4).setHeightInPoints(65f);
            
            //Headings
            XSSFRow tableHeadings = sheet.createRow(5);
            int lastColumn = 0;
            for (int i = 0; i < 6; i++) {                
                XSSFCell cell = tableHeadings.createCell(lastColumn);
                switch (i) {
                    case 0: {
                        cell.setCellStyle(headingStyle);
                        cell.setCellValue("Job Title");
                        lastColumn++;
                        break;
                    }
                    case 1: {
                        if (descriptionsEnabled) {
                            cell.setCellStyle(headingStyle);
                            cell.setCellValue("Job Description");
                            lastColumn++;
                        }
                        break;
                    }
                    case 2: {
                        if (materialsEnabled) {
                            cell.setCellStyle(headingStyle);
                            cell.setCellValue("Materials");
                            lastColumn++;
                        }
                        break;
                    }
                    case 3: {
                        if (materialsPriceEnabled && labourPriceEnabled) {
                            cell.setCellStyle(headingStyle);
                            cell.setCellValue("Material cost");
                            lastColumn++;
                        }
                        break;
                    }
                    case 4: {
                        if (labourPriceEnabled && materialsPriceEnabled) {
                            cell.setCellStyle(headingStyle);
                            cell.setCellValue("Labour cost");
                            lastColumn++;
                        }
                        break;
                    }
                    case 5: {
                        cell.setCellStyle(headingStyle);
                        cell.setCellValue("Total cost");
                        lastColumn++;
                        break;
                    }
                }
            }
            
            //Jobs
            int lastRow = 7;
            for (JobGroup jobGroup : project.getJobGroups()) {
                if (jobGroupsEnabled) {
                    XSSFRow row = sheet.createRow(lastRow);
                    for (int i = 0; i < lastColumn; i++) {
                        XSSFCell cell = row.createCell(i);
                        cell.setCellStyle(jobGroupStyle);
                        
                        if (i == 0) cell.setCellValue(jobGroup.getGroupName());
                    }
                    lastRow++;
                }
                
                for (Job job : jobGroup.getJobs()) {
                    XSSFRow row = sheet.createRow(lastRow);
                    int lastJobColumn = 0;
                    for (int i = 0; i < 6; i++) {                
                        XSSFCell cell = row.createCell(lastJobColumn);
                        switch (i) {
                            case 0: {
                                cell.setCellStyle(accountingStyle);
                                cell.setCellValue(job.getTitle());
                                lastJobColumn++;
                                break;
                            }
                            case 1: {
                                if (descriptionsEnabled) {
                                    cell.setCellStyle(accountingStyle);
                                    cell.setCellValue(job.getDescription());
                                    lastJobColumn++;
                                }
                                break;
                            }
                            case 2: {
                                if (materialsEnabled) {
                                    cell.setCellStyle(accountingStyle);
                                    cell.setCellValue(job.getRequiredMaterialsString(true));
                                    lastJobColumn++;
                                }
                                break;
                            }
                            case 3: {
                                if (materialsPriceEnabled && labourPriceEnabled) {
                                    cell.setCellStyle(accountingStyle);
                                    cell.setCellValue(job.getMaterialPrice());
                                    lastJobColumn++;
                                }
                                break;
                            }
                            case 4: {
                                if (labourPriceEnabled && materialsPriceEnabled) {
                                    cell.setCellStyle(accountingStyle);
                                    cell.setCellValue(job.getLabourPrice());
                                    lastJobColumn++;
                                }
                                break;
                            }
                            case 5: {
                                cell.setCellStyle(accountingStyle);
                                cell.setCellValue(job.getTotalCost());
                                lastJobColumn++;
                                break;
                            }
                        }
                    }
                    lastRow++;
                }
            }
            
            //Subtotals
            
            //Fill in the last 4 columns created
            XSSFRow subTotalRow = sheet.createRow(lastRow + 1);
            
            double totalCost = project.calculateTotalCost();
            
            int columnNumber = 0;
            int totalLabelColumn = 0;
            for (int i = lastColumn - 1; i > lastColumn - 5; i--) {
                XSSFCell cell = subTotalRow.createCell(i);
                cell.setCellStyle(lightBlue);
                
                switch (columnNumber) {
                    case 3: {
                        cell.setCellStyle(headingStyle);
                        cell.setCellValue("Sub total:");
                        break;
                    }
                    case 2: cell.setCellValue(project.calculateTotalMaterialCost()); break;
                    case 1: cell.setCellValue(project.calculateTotalLabourCost()); break;
                    case 0: cell.setCellValue(totalCost); break;
                }
                columnNumber++;
                totalLabelColumn = i; //This is done so that the Total cost row may be aligned properly
            }
            
            //Total
            XSSFRow totalRow = sheet.createRow(subTotalRow.getRowNum() + 2);
            XSSFCell totalLabel = totalRow.createCell(totalLabelColumn);
            totalLabel.setCellStyle(headingStyle);
            totalLabel.setCellValue("Total cost:");
            
            XSSFCell totalCell = totalRow.createCell(totalLabelColumn + 1);
            totalCell.setCellStyle(lightBlue);
            totalCell.setCellValue(totalCost);
                        
            //If the user picked a file
            if (file != null) {
                //Save the workbook to the file
                FileOutputStream fileOut = new FileOutputStream(file);
                workbook.write(fileOut);
                workbook.close();
                fileOut.close();

                //Open the workbook
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
