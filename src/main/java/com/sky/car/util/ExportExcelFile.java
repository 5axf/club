package com.sky.car.util;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class ExportExcelFile {

	protected static Log LOGGER = LogFactory.getLog(ExportExcelFile.class);

	/**
	 * @param heads
	 * @param fields
	 * @param list
	 * @param fileName
	 * @param sheetName
	 */
	public static void exportExcel(String[] heads, String[] fields, List<?> list,
			String fileName, String sheetName) {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getResponse();
		try {
			response.reset();
			response.setContentType("application/msexcel;charset=UTF-8"); //设置导出文件格式
			response.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
			ServletOutputStream os = response.getOutputStream();
			if (list.size() <= 1000100) {
				ExportExcelFile.outExcel(heads, fields, list, fileName,sheetName,os);
			} else {
				ExportExcelFile.outExcelPage(heads, fields, list, fileName,sheetName,os);
			}
			os.flush();
			os.close();
		} catch (Exception e) {
			LOGGER.error("exportExcel报错", e);
		}
	}

	/**
	 * @param heads
	 *            列名
	 * @param fields
	 *            数据字段名
	 * @param list
	 *            数据
	 * @param fileName
	 *            文件名
	 * @param sheetName
	 *            Excel的sheet名称
	 * @throws Exception
	 */
	public static void outExcel(String[] heads, String[] fields, List<?> list,
			String fileName, String sheetName, ServletOutputStream os)
			throws Exception {
		long startTime = System.currentTimeMillis();
		Workbook workbook = new SXSSFWorkbook(100);
		Sheet sheet = workbook.createSheet(sheetName);
		try {
			Row row1 = sheet.createRow(0);	 //表格第一行
			sheet.setColumnWidth(1, 20 * 256);
			 //初始化单元格
			for (int k = 0; k < heads.length; k++) {
				sheet.setColumnWidth(k, heads[k].length() * 8 * 256); //设置列宽
				Cell cell1 = row1.createCell(k); //创建一个单元格
				cell1.setCellValue(heads[k]);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			long t1 = System.currentTimeMillis();
			for (int i = 0; i < list.size(); i++, map.clear()) {
				Object obj = list.get(i);	 //初始化数据
				ExportExcelFile.objectToMap(obj, map);	 //把对象转化成map
				Row row = sheet.createRow(i + 1); 	 //初始化excel行
				for (int j = 0; j < fields.length; j++) {
					 //创建一个单元格对象 并赋值
					setCellValue(row.createCell(j), map, fields[j]);
				}
			}
			long t2 = System.currentTimeMillis();
			LOGGER.info(fileName + "数据处理时间：" + (t2 - t1));
			workbook.write(os);
			workbook.close();
		} catch (Exception e) {
			LOGGER.error(fileName + "导出export报错", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info(fileName + "导出总时间" + (endTime - startTime));
	}

	/**
	 * 导出Excel文件
	 * @param heads
	 * @param fields
	 * @param list 结果集
	 * @param fileName 文件名
	 * @param sheetName
	 * @param os
	 * @throws Exception
	 */
	public static void outExcelPage(String[] heads, String[] fields,
			List<?> list, String fileName, String sheetName,
			ServletOutputStream os) throws Exception {
		long startTime = System.currentTimeMillis();
		Workbook workbook = new SXSSFWorkbook(100); //创建一个工作簿
		try {
			int pageNum;
			int pageSize = 1000100;
			int totCount;
			if (list.size() < pageSize || list.size() == pageSize) {
				totCount = 1;
			} else if (list.size() > pageSize && list.size() % pageSize == 0) {
				totCount = list.size() / pageSize;
			} else {
				totCount = list.size() / pageSize + 1;
			}
			long t1 = System.currentTimeMillis();
			for (pageNum = 1; pageNum < totCount + 1; pageNum++) {
				int end = pageSize * pageNum;
				if (pageNum == totCount) {
					end = list.size();
				}
				List<?> listPage = list.subList(pageSize * (pageNum - 1), end);
				Sheet sheet = workbook.createSheet("第" + (pageNum) + "页");  //创建分页工作簿
				sheet.setColumnWidth(1, 20 * 256);
				Row row1 = sheet.createRow(0); //表格第一行
				for (int k = 0; k < heads.length; k++) {
					sheet.setColumnWidth(k, heads[k].length() * 5 * 256); //设置列宽
					Cell cell1 = row1.createCell(k); //创建一个单元格
					cell1.setCellValue(heads[k]);
				}

				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < listPage.size(); i++, map.clear()) {
					Object obj = listPage.get(i);		 //初始化数据
					ExportExcelFile.objectToMap(obj, map);	 //把对象转化成map
					Row row = sheet.createRow(i + 1);
					for (int j = 0; j < fields.length; j++) {
						 //创建一个单元格对象 并赋值
						setCellValue(row.createCell(j), map, fields[j]);
					}
				}
			}

			long t2 = System.currentTimeMillis();
			LOGGER.info(fileName + "数据处理时间：" + (t2 - t1));
			workbook.write(os);
			workbook.close();
		} catch (Exception e) {
			LOGGER.error(fileName + "导出export报错", e);
		}
		long endTime = System.currentTimeMillis();
		LOGGER.info(fileName + "导出总时间" + (endTime - startTime));
	}

	/**
	 * 创建行添加数据
	 * 
	 * @param cell
	 * @param map
	 * @param fieldName
	 */
	private static void setCellValue(Cell cell, Map<String, Object> map,
			String fieldName) throws Exception {
		try {
			Object obj = map.get(fieldName);
			if (null != obj) {
				if (obj instanceof String) {
					cell.setCellValue(obj.toString());
				} else if (obj instanceof Integer) {
					cell.setCellValue(Integer.parseInt(obj.toString()));
				} else if (obj instanceof Date) {
					cell.setCellValue(DateUtil.formatDate((Date) obj,
							DateUtil.DATE_SMALL_STR));
				} else if (obj instanceof Long) {
					cell.setCellValue(Long.parseLong(obj.toString()));
				} else if (obj instanceof Double) {
					cell.setCellValue(Double.parseDouble(obj.toString()));
				} else {
					cell.setCellValue(obj.toString());
				}
			} else {
				cell.setCellValue("");
			}
		} catch (Exception e) {
			LOGGER.error("创建数据行出错!", e);
			e.printStackTrace();
		}
	}

	/**
	 * Object转换成Map类
	 * 
	 * @param obj
	 * @return
	 * @throws SecurityException
	 */
	public static void objectToMap(Object obj, Map<String, Object> reMap)
			throws SecurityException {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			Field f = null;
			Object o = null;
			for (int i = 0; i < fields.length; i++, o = null, f = null) {
				f = obj.getClass().getDeclaredField(fields[i].getName());
				f.setAccessible(true);
				o = f.get(obj);
				if(o!=null && !o.equals("") && !o.equals("null")) {
					reMap.put(fields[i].getName(), o);
				}
			}
		} catch (Exception e) {
			LOGGER.error("创建数据行出错!", e);
		}
	}

}
