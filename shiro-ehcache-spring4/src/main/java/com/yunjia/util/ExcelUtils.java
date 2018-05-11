package com.yunjia.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("all")
public class ExcelUtils {

	public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {
		// 第一步，创建一个webbook，对应一个Excel文件
		if (wb == null) {
			wb = new HSSFWorkbook();
		}
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(sheetName);
		sheet.setAutobreaks(true);

		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow(0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		// HSSFExtendedColor color = new HSSFExtendedColor(new ExtendedColor());
		// color.setARGBHex("FF4C6B87");
		// HSSFPalette customPalette = wb.getCustomPalette();
		// customPalette.setColorAtIndex(HSSFColor.ORANGE.index, (byte) 76,
		// (byte) 107, (byte) 135);
		// style.setFillForegroundColor(HSSFColor.ORANGE.index);
		// style.setFillForegroundColor(color.getIndex());
		// style.setFillForegroundColor(HSSFColor.YELLOW.index2);
		// style.setFillBackgroundColor(color.getIndex());
		// style.setFillBackgroundColor(HSSFColor.YELLOW.index2);

		HSSFCell cell = null;
		// 创建标题
		for (int i = 0; i < title.length; i++) {
			// sheet.autoSizeColumn(i);
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(style);
		}
		// 创建内容
		for (int i = 0; i < values.length; i++) {
			sheet.autoSizeColumn(i);
			row = sheet.createRow(i + 1);
			for (int j = 0; j < values[i].length; j++) {
				row.createCell(j).setCellValue(values[i][j]);
			}
		}

		return wb;
	}

	public static void write2Excel2003(OutputStream os, String sheetName, List<String[]> titles,
			List<Map<String, Object>> data) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFSheet sheet = wb.createSheet(sheetName);
		sheet.autoSizeColumn(0);
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

		// 创建标题
		HSSFRow titleRow = sheet.createRow(0);
		for (int i = 0; i < titles.size(); i++) {
			sheet.autoSizeColumn(i);// 设置单元格自适应大小
			HSSFCell cell = titleRow.createCell(i);

			cell.setCellStyle(cellStyle);
			cell.setCellValue(titles.get(i)[1]);
		}
		// 创建数据行
		for (int i = 0; i < data.size(); i++) {
			HSSFRow r = sheet.createRow(i + 1);

			Map<String, Object> map = data.get(i);
			for (int j = 0; j < titles.size(); j++) {
				sheet.autoSizeColumn(j);// 设置单元格自适应大小
				HSSFCell cell = r.createCell(j);
				cell.setCellStyle(cellStyle);
				
				String key = titles.get(j)[0];
				Object obj = map.get(key);
				if(obj instanceof Number){
					cell.setCellValue(Double.valueOf(obj+""));
				}else {
					String value = (obj == null) ? "" : obj.toString();
					cell.setCellValue(value);
				}
			}

		}
		wb.write(os);
	}
	
	
	public static void write2Excel2007(OutputStream os, String sheetName, List<String[]> titles,
			List<Map<String, Object>> data) throws Exception {
		XSSFWorkbook wb = new XSSFWorkbook();
		
		XSSFSheet sheet = wb.createSheet(sheetName);
		sheet.autoSizeColumn(0);
		XSSFCellStyle HeadStyle = createHeadStyle(wb);
		XSSFCellStyle bodyStyle = createBodyStyle(wb);
		
		// 创建标题
		XSSFRow titleRow = sheet.createRow(0);
		for (int i = 0; i < titles.size(); i++) {
//			sheet.autoSizeColumn(i);// 设置单元格自适应大小
			XSSFCell cell = titleRow.createCell(i);
			
			cell.setCellStyle(HeadStyle);
			cell.setCellValue(titles.get(i)[1]);
		}
		// 创建数据行
		for (int i = 0; i < data.size(); i++) {
			XSSFRow r = sheet.createRow(i + 1);
			
			Map<String, Object> map = data.get(i);
			for (int j = 0; j < titles.size(); j++) {
				XSSFCell cell = r.createCell(j);
				cell.setCellStyle(bodyStyle);
				
				String key = titles.get(j)[0];
				Object obj = map.get(key);
				if(obj instanceof Number) {
					cell.setCellValue(Double.valueOf(obj+""));
				}else {
					String value = obj==null ? "" : obj.toString().trim();
					cell.setCellValue(value);
				}
			}
		}
		
		wb.write(os);
	}

	private static XSSFCellStyle createBodyStyle(XSSFWorkbook wb) {
		XSSFCellStyle bodyStyle = wb.createCellStyle();// 创建样式对象 
		//style2.setFillForegroundColor(XSSFColor.YELLOW.index);
		//style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		bodyStyle.setBorderBottom(BorderStyle.THIN);
		bodyStyle.setBorderLeft(BorderStyle.THIN);
		bodyStyle.setBorderRight(BorderStyle.THIN);
		bodyStyle.setBorderTop(BorderStyle.THIN);
		bodyStyle.setAlignment(HorizontalAlignment.CENTER);
		bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		// 生成另一个字体
		// XSSFFont font2 = wb.createFont();
		// font2.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		// // 把字体应用到当前的样式
		// bodyStyle.setFont(font2);
		return bodyStyle;
	}

	private static XSSFCellStyle createHeadStyle(XSSFWorkbook wb) {
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);// 设置样式居中
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		// 生成一个字体
		XSSFFont font = wb.createFont();
		//font.setColor(XSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);
		// 把字体应用到当前的样式
		cellStyle.setFont(font);
		return cellStyle;
	}

	public static List<Map<String, Object>> readFromExcel(InputStream is, String[] titles, int startRow, int startCol)
			throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		POIFSFileSystem fs = new POIFSFileSystem(is);
		// 得到Excel工作簿对象
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		// 得到Excel工作表对象
		HSSFSheet sheet = wb.getSheetAt(0);
		// 获取有效行
		int rowNum = sheet.getLastRowNum();
		for (int i = startRow; i < rowNum; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			HSSFRow row = sheet.getRow(i);
			// 获取有效列
			int cellNum = row.getLastCellNum();
			for (int j = startCol; j < cellNum; j++) {
				HSSFCell cell = row.getCell(j);
				String cellValue = cell.getStringCellValue();
				map.put(titles[j - startCol], cellValue);
			}

			list.add(map);
		}
		// 得到Excel工作表的行
		HSSFRow row = sheet.getRow(0);
		// 得到Excel工作表指定行的单元格
		HSSFCell cell = row.getCell(0);
		HSSFCellStyle cellStyle = cell.getCellStyle();// 得到单元格样式
		System.out.println(cell.getStringCellValue());

		return list;
	}

}