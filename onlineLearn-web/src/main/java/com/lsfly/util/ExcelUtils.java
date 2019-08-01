package com.lsfly.util;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/***
 * poi 导出excel/读取excel
 */
public class ExcelUtils {
	//03版
	private static final String OFFICE_EXCEL_XLS = ".xls";
	//07版
	private static final String OFFICE_EXCEL_XLSX = ".xlsx";	
	//HSSFWorkbook 能写入的最大行数 65535   最大列256
	private static final Integer HSSFWORKBOOK_MAX_NUM = 65535;
	//SXSSFWorkbook 最大写入行数 1048576 最大列 16384
	private static final Integer XSSFWORKBOOK_MAX_NUM = 1048576;
		
	/**单元格最小宽度*/
	private static final int MIN_COL_WIDTH = 2 * 256;

    /**单元格最大宽度*/
	private static final int MAX_COL_WIDTH = 255 * 256;

	/** 默认字体(字体：微软雅黑；大小：12)**/
	private static Font sheetFont(Workbook workbook,String fontStr) {
		//创建新字体
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);//设置字体大小
		if(null == fontStr || "".equals(fontStr)){
			font.setFontName("微软雅黑");//设置字体    			
		}else{
			font.setFontName(fontStr);
		}
		return font;
	}

	/**设置单元格的时间类型**/
	private static CellStyle dataCellStyle(Workbook workbook,Font f,String dateType) {

		CellStyle dateCellStyle = workbook.createCellStyle();

		if(null == f || "".equals(f)){
			f= sheetFont(workbook,null);
		}
		dateCellStyle.setFont(f);
		dateCellStyle.setAlignment(HorizontalAlignment.CENTER);//设置单元格对齐方式为水平居中
		dateCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//设置单元格对齐方式为垂直居中
		DataFormat format= workbook.createDataFormat();
		if(null == dateType || "".equals(dateType)){
			dateCellStyle.setDataFormat(format.getFormat("yyyy/MM/dd HH:mm:ss"));
		}else{
			dateCellStyle.setDataFormat(format.getFormat(dateType));
		}		
		return dateCellStyle;
	}

    /**
     * 计算列宽
     * @param colIndex 当前列的下标
     * @param content 当前列的内容(字符串形式)
     * @return 当前列的最佳列宽
     * @throws UnsupportedEncodingException
     *
     */
	private static int autoColWidth(Sheet sheet, int colIndex, String content) throws UnsupportedEncodingException{
        int width = content.getBytes("UTF-8").length * 256+184*2;//根据当前内容计算单元格宽度
        if(width < ExcelUtils.MIN_COL_WIDTH){
            width = ExcelUtils.MIN_COL_WIDTH;
        }
        //每列的最大宽度不能超过255 * 256，超过这个值会报java.lang.IllegalArgumentException: the maximum column width in Excel is 255 characters
        if(width > ExcelUtils.MAX_COL_WIDTH){
			width = ExcelUtils.MAX_COL_WIDTH;
        }
        if(width<sheet.getColumnWidth(colIndex)){
			width = sheet.getColumnWidth(colIndex);
        }
        return width;
    }

	/**
	 * 写入图片到一个单元格中
	 * @param workbook {@link Workbook}对象，要写入图片的工作薄
	 * @param sheet Excel的表单
	 * @param imgAddress 图片地址，可以是URL也可以是本地路径
	 * @param rowIndex 写入单元格行下标
	 * @param colIndex 写入单元格列下标
	 */
	private static void writeImgToOneCell(Workbook workbook,Sheet sheet,String imgAddress, int rowIndex, int colIndex){
		ByteArrayOutputStream byteArrayOut = null;
		try {
			byteArrayOut = new ByteArrayOutputStream();
			BufferedImage bufferImg = null;
			if(imgAddress.startsWith("http")){
				bufferImg = ImageIO.read(new URL(imgAddress));
			}else{
				bufferImg = ImageIO.read(new File(imgAddress));
			}
			String suffixName = imgAddress.substring(imgAddress.lastIndexOf(".") + 1);//获取文件后缀名
			ImageIO.write(bufferImg, suffixName, byteArrayOut);
			//表示图片写入一个单元格内且图片四周与单元格的边距为0
			CreationHelper helper = workbook.getCreationHelper();
			ClientAnchor anchor = helper.createClientAnchor();
			anchor.setDx1(0);
			anchor.setDx2(0);
			anchor.setDy1(0);
			anchor.setDy2(0);
			anchor.setCol1(colIndex);
			anchor.setCol2(colIndex + 1);
			anchor.setRow1(rowIndex);
			anchor.setRow2(rowIndex + 1);
			Drawing drawing = sheet.createDrawingPatriarch();
			writeImgToCell(workbook, drawing, anchor, byteArrayOut, suffixName);
		} catch (Exception e) {
			System.err.println("ExcelUtil.writeImgToOneCell方法Exception:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if(null != byteArrayOut){
				try {
					byteArrayOut.flush();
					byteArrayOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * 写入图片到一个单元格中
	 * @param workbook
	 * @param sheet
	 * @param source 图片资源(目前只支持{@link URL}和{@link File})
	 * @param cell {@link XSSFCell}对象，要写入图片的单元格
	 */
	private static void writeImgToOneCell(Workbook workbook,Sheet sheet,Object source, Cell cell){
		ByteArrayOutputStream byteArrayOut = null;
		try {
			BufferedImage bufferImg = null;
			String suffixName = "jpg";//给个默认的图片格式，避免获取文件后缀名失败
			if(source instanceof URL){
				byteArrayOut = new ByteArrayOutputStream();
				URL url = (URL)source;
				bufferImg = ImageIO.read(url);
				String query = url.getPath();
				suffixName = query.substring(query.lastIndexOf(".") + 1);//获取文件后缀名
			}else if(source instanceof File){
				byteArrayOut = new ByteArrayOutputStream();
				File file = (File)source;
				bufferImg = ImageIO.read(file);
				String addr = file.getAbsolutePath();
				suffixName = addr.substring(addr.lastIndexOf(".") + 1);//获取文件后缀名
			}else{//如果没匹配上，就跳过当前单元格
				return;
			}
			ImageIO.write(bufferImg, suffixName, byteArrayOut);
			//表示图片写入一个单元格内且图片四周与单元格的边距为0
			CreationHelper helper = workbook.getCreationHelper();
			ClientAnchor anchor = helper.createClientAnchor();
			anchor.setDx1(0);
			anchor.setDy1(0);//开始
			anchor.setDx2(0);
			anchor.setDy2(0);
			anchor.setCol1(cell.getColumnIndex());
			anchor.setCol2(cell.getColumnIndex() + 1);
			anchor.setRow1(cell.getRowIndex());
			anchor.setRow2(cell.getRowIndex() + 1);
			Drawing drawing = sheet.createDrawingPatriarch();
			writeImgToCell(workbook, drawing, anchor, byteArrayOut, suffixName);
		} catch (Exception e) {
			System.err.println("ExcelUtils.writeImgToOneCell方法Exception:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if(null != byteArrayOut){
				try {
					byteArrayOut.flush();
					byteArrayOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * 写入图片到单元格
	 * @param workbook 要写入图片的工作薄
	 * @param draw {@link Drawing}对象
	 * @param anchor {@link ClientAnchor}对象
	 * @param byteArrayOut {@link ByteArrayOutputStream}对象
	 * @param suffixName 图片后缀名
	 */
	private static void writeImgToCell(Workbook workbook, Drawing draw,ClientAnchor anchor, ByteArrayOutputStream byteArrayOut, String suffixName){
		try {
			//不同格式的图片采用不同的方法写入
			if("bmp".equalsIgnoreCase(suffixName)){//忽略大小写
				if(workbook instanceof XSSFWorkbook){
					draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_BMP));
				}
			}else if("eps".equalsIgnoreCase(suffixName)){
				if(workbook instanceof XSSFWorkbook){
					draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_EPS));
				}
			}else if("gif".equalsIgnoreCase(suffixName)){
				if(workbook instanceof XSSFWorkbook){
					draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_GIF));
				}
			}else if("tiff".equalsIgnoreCase(suffixName)){
				if(workbook instanceof XSSFWorkbook){
					draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_TIFF));
				}
			}else if("jpg".equalsIgnoreCase(suffixName) || "jpeg".equalsIgnoreCase(suffixName)){
				draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_JPEG));
			}else if("png".equalsIgnoreCase(suffixName)){
				draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_PNG));
			}else if("emf".equalsIgnoreCase(suffixName)){//扩展窗口元文件
				draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_EMF));
			}else if("wmf".equalsIgnoreCase(suffixName)){//Windows元文件
				draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_WMF));
			}else if("pict".equalsIgnoreCase(suffixName)){//Mac PICT格式
				draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_PICT));
			}else if("dib".equalsIgnoreCase(suffixName)){//设备独立位图
				draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_DIB));
			}
		} catch (Exception e) {
			System.err.println("ExcelUtil.writeImgToCell方法Exception:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 设置标题
	 * @param workbook Excel对象
	 * @param sheet 工作簿对象
	 * @param headers 表头名称
	 * @param headerStyle 表头样式
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static int writeTitleToSheet(Workbook workbook,Sheet sheet,String[] headers,CellStyle headerStyle) throws UnsupportedEncodingException{
		int createRowNums = 0;//创建行的次数
		if(workbook != null && sheet != null && headers.length >0){
			Row titleRow = sheet.createRow(0);
			titleRow.setHeightInPoints(20);//设置高度(20倍)
			++createRowNums;//每创建一行就累加一下行数
			//写入标题
			for (int i = 0; i < headers.length; i++) {
				String content = null != headers[i] ? headers[i] : "";//取出标题内容
				Cell cell = titleRow.createCell(i);//创建单元格
				cell.setCellValue(content);//设置单元格内容
				if(null != headerStyle){
					cell.setCellStyle(headerStyle);//设置单元格样式
				}else{
					Font font = workbook.createFont();//创建标题字体样式
					font.setFontName("標楷體");//设置字体
					font.setBold(true);//加粗字体
					font.setColor(new HSSFColor.WHITE().getIndex());//设置字体颜色为白色

					CellStyle titleCellStyle = workbook.createCellStyle();//创建标题单元格样式
					titleCellStyle.setBorderLeft(BorderStyle.THIN);//设置单元格左边框为厚厚的边框单元格样式
					titleCellStyle.setBorderTop(BorderStyle.THIN);//设置单元格上边框为厚厚的边框单元格样式
					titleCellStyle.setBorderRight(BorderStyle.THIN);//设置单元格右边框为厚厚的边框单元格样式
					titleCellStyle.setBorderBottom(BorderStyle.THIN);//设置单元格下边框
					titleCellStyle.setAlignment(HorizontalAlignment.CENTER);//设置单元格对齐方式为水平居中
					titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//设置单元格对齐方式为垂直居中
					//设置背景色
					titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//填充色
					titleCellStyle.setFillBackgroundColor(new HSSFColor.PALE_BLUE().getIndex());//设置背景色
					titleCellStyle.setFillForegroundColor(new HSSFColor.PALE_BLUE().getIndex());//前景色

					titleCellStyle.setFont(font);//设置字体样式

					cell.setCellStyle(titleCellStyle);//设置标题单元格样式
				}
				//sheet.autoSizeColumn(0);//设置自动调整宽度(只对英文，数字有效)
				sheet.setColumnWidth(i,content.getBytes("UTF-8").length*256*2);//设置宽度
			}
			//sheet.createFreezePane(0, 1, 0, 1);//冻结首行
		}
		return createRowNums;
	}

	/**
	 * 写数据到sheet
	 * @param workbook Excel对象
	 * @param sheet 工作簿对象
	 * @param cellStyle 单元格样式
	 * @param data 数据
	 * @param fields
	 * @param headers 表头名称
	 * @throws UnsupportedEncodingException
	 */
	private static void writeDataToSheet(Workbook workbook,Sheet sheet,String sheetName,CellStyle cellStyle,List<?> data,String[] fields,String[] headers) throws UnsupportedEncodingException {
		if(workbook != null && sheet != null && data.size() > 0 && headers.length > 0 && fields.length>0){
			int sheetNums = workbook.getNumberOfSheets();//获取excel内sheet的数量
			for (int i = 0; i < data.size(); i++) {//循环取出数据
				Map<String,Object> map = null;
				if(HashMap.class == data.get(i).getClass()){
					map = (Map<String,Object>)data.get(i);
				}
				else{
					map = convertBean(data.get(i));//取出每一行数据
				}

				int sheetTotalRows = sheet.getPhysicalNumberOfRows();//更新每个sheet内的行数
				//每当一个sheet内的行数达到65535行之后就创建一个新的sheet放数据
				if(0 == sheetTotalRows % ExcelUtils.HSSFWORKBOOK_MAX_NUM){
					sheet = workbook.createSheet((null != sheetName ? sheetName : "") + "第" + (sheetNums + 1) + "页");
					++sheetNums;//累加sheet数量
					sheetTotalRows = 0;//计数器清零
					//写入标题
					sheetTotalRows += writeTitleToSheet(workbook,sheet,headers,null);//写入标题到sheet中，并累加当前sheet中的行数
				}
				Row row = sheet.createRow(sheetTotalRows);//创建新的一行
				++sheetTotalRows;//每创建一行就累加一下总行数
				for(int j = 0;j<fields.length;j++){
					CellStyle style = null;
					Cell cell = row.createCell(j);//创建单元格
					Object obj = map.get(fields[j]);
					if(null != obj && !"".equals(obj)){//判断查询出来的数据是否为空
						if(obj instanceof String){
							String str = obj.toString();//转换为字符串
							String regExpEmail = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";//邮箱正则表达式
							String regExpURL = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";//URL正则表达式
							Hyperlink hyperLink = null;
							CreationHelper creationHelper = workbook.getCreationHelper();
							if(str.lastIndexOf(".")>0){
								String lastStr = str.substring(str.lastIndexOf(".")+1);//获取后缀
								if("png".equalsIgnoreCase(lastStr) || "jpg".equalsIgnoreCase(lastStr) || "jpeg".equalsIgnoreCase(lastStr) || "gif".equalsIgnoreCase(lastStr)){//为图片格式
									ExcelUtils.writeImgToOneCell(workbook,sheet,str,cell);//向当前单元格中写入数据
								}else if(str.matches(regExpEmail)){//邮箱
									hyperLink = creationHelper.createHyperlink(HyperlinkType.EMAIL);//创建URL类型的链接
									hyperLink.setAddress(str);//设置链接地址
									cell.setCellValue(str);
								}else if(str.matches(regExpURL)){//链接地址
									hyperLink = creationHelper.createHyperlink(HyperlinkType.URL);//创建URL类型的链接
									hyperLink.setAddress(str);//设置链接地址
									cell.setCellValue(str);
								}else{//其他格式以String格式写入
									cell.setCellValue(str);
									style = cellStyle;
								}
							}else{
								cell.setCellValue(str);
								style = cellStyle;
							}
						}else if(obj instanceof Integer){//整型
							cell.setCellValue((Integer)obj);
							style = cellStyle;
						}else if(obj instanceof Float){//浮点型
							cell.setCellValue((Float)obj);
							style = cellStyle;
						}else if(obj instanceof Double){//浮点型
							cell.setCellValue((Double)obj);
							style = cellStyle;
						}else if(obj instanceof Date){//时间类型
							cell.setCellValue((Date)obj);
							style = dataCellStyle(workbook,sheetFont(workbook,null),null);//设置单元格样式
						}else if(obj instanceof Long){//长整型
							cell.setCellValue((Long)obj);
							style = cellStyle;
						}else{//其他格式(以文本格式写入)
							if(null != obj && !"".equals(obj)){
								cell.setCellValue(obj.toString());
								style = cellStyle;
							}
						}
						cell.setCellStyle(style);//设置单元格样式
						sheet.setColumnWidth(j, autoColWidth(sheet,j,obj.toString()));//设置列宽
					}
				}
			}
		}
	}

	/**
	 * 写数据到sheet
	 * @param workbook Excel对象
	 * @param sheet 工作簿对象
	 * @param cellStyle 单元格样式
	 * @param data 数据
	 * @param headers 表头名称
	 * @throws UnsupportedEncodingException
	 */
	private static void writeDataToSheet(Workbook workbook,Sheet sheet,String sheetName,CellStyle cellStyle,List<List<Object>> data,String[] headers) throws UnsupportedEncodingException {
		if(workbook != null && sheet != null && data.size() > 0 && headers.length > 0){
			int sheetNums = workbook.getNumberOfSheets();//获取excel内sheet的数量
			for (int i = 0; i < data.size(); i++) {//循环取出数据
				List<Object> list = data.get(i);
				int sheetTotalRows = sheet.getPhysicalNumberOfRows();//更新每个sheet内的行数
				//每当一个sheet内的行数达到65535行之后就创建一个新的sheet放数据
				if(0 == sheetTotalRows % ExcelUtils.HSSFWORKBOOK_MAX_NUM){
					sheet = workbook.createSheet((null != sheetName ? sheetName : "") + "第" + (sheetNums + 1) + "页");
					++sheetNums;//累加sheet数量
					sheetTotalRows = 0;//计数器清零
					//写入标题
					sheetTotalRows += writeTitleToSheet(workbook,sheet,headers,null);//写入标题到sheet中，并累加当前sheet中的行数
				}
				Row row = sheet.createRow(sheetTotalRows);//创建新的一行
				++sheetTotalRows;//每创建一行就累加一下总行数
				for(int j = 0;j<list.size();j++){
					CellStyle style = null;
					Cell cell = row.createCell(j);//创建单元格
					Object obj = list.get(j);
					if(null != obj && !"".equals(obj)){//判断查询出来的数据是否为空
						if(obj instanceof String){
							String str = obj.toString();//转换为字符串
							String regExpEmail = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";//邮箱正则表达式
							String regExpURL = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";//URL正则表达式
							Hyperlink hyperLink = null;
							CreationHelper creationHelper = workbook.getCreationHelper();
							if(str.lastIndexOf(".")>0){
								String lastStr = str.substring(str.lastIndexOf(".")+1);//获取后缀
								if("png".equalsIgnoreCase(lastStr) || "jpg".equalsIgnoreCase(lastStr) || "jpeg".equalsIgnoreCase(lastStr) || "gif".equalsIgnoreCase(lastStr)){//为图片格式
									ExcelUtils.writeImgToOneCell(workbook,sheet,str,cell);//向当前单元格中写入数据
								}else if(str.matches(regExpEmail)){//邮箱
									hyperLink = creationHelper.createHyperlink(HyperlinkType.EMAIL);//创建URL类型的链接
									hyperLink.setAddress(str);//设置链接地址
									cell.setCellValue(str);
								}else if(str.matches(regExpURL)){//链接地址
									hyperLink = creationHelper.createHyperlink(HyperlinkType.URL);//创建URL类型的链接
									hyperLink.setAddress(str);//设置链接地址
									cell.setCellValue(str);
								}else{//其他格式以String格式写入
									cell.setCellValue(str);
									style = cellStyle;
								}
							}else{
								cell.setCellValue(str);
								style = cellStyle;
							}
						}else if(obj instanceof Integer){//整型
							cell.setCellValue((Integer)obj);
							style = cellStyle;
						}else if(obj instanceof Float){//浮点型
							cell.setCellValue((Float)obj);
							style = cellStyle;
						}else if(obj instanceof Double){//浮点型
							cell.setCellValue((Double)obj);
							style = cellStyle;
						}else if(obj instanceof Date){//时间类型
							cell.setCellValue((Date)obj);
							style = dataCellStyle(workbook,sheetFont(workbook,null),null);//设置单元格样式
						}else if(obj instanceof Long){//长整型
							cell.setCellValue((Long)obj);
							style = cellStyle;
						}else{//其他格式(以文本格式写入)
							if(null != obj && !"".equals(obj)){
								cell.setCellValue(obj.toString());
								style = cellStyle;
							}
						}
						cell.setCellStyle(style);//设置单元格样式
						sheet.setColumnWidth(j, autoColWidth(sheet,j,obj.toString()));//设置列宽
					}
				}
			}
		}
	}

	/**
	 * 导出excel(.xls)
	 * @param fileName 文件名称
	 * @param data 数据
	 * @param headers 标题
	 * @param fields 属性
	 * @param response
	 */
	public static void writeExcelToXLS(String fileName,String sheetName,List<?> data,String[] headers,String[] fields ,HttpServletResponse response){
		try {
			//创建Excel表格
			HSSFWorkbook workbook = new HSSFWorkbook();

			//创建新字体
			HSSFFont font = workbook.createFont();
			font.setFontName("微软雅黑");//设置字体
			font.setFontHeightInPoints((short) 12);//设置字体大小

			//单元格样式(3.16以下的写法)
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);//设置单元格对齐方式为水平居左
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//设置单元格对齐方式为垂直居中
			cellStyle.setFont(font);//设置字体样式

			//表头样式(3.16写法)
			HSSFCellStyle titleStyle = workbook.createCellStyle();
			titleStyle.setAlignment(HorizontalAlignment.CENTER);//设置单元格对齐方式为水平居中
			titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//设置单元格对齐方式为垂直居中
			titleStyle.setFillBackgroundColor(new HSSFColor.TURQUOISE().getIndex());//设置背景色
			titleStyle.setFont(font);//设置字体样式


			/**设置单元格格式为文本格式*/
			HSSFCellStyle textStyle = workbook.createCellStyle();
			HSSFDataFormat format = workbook.createDataFormat();
			textStyle.setDataFormat(format.getFormat("@"));

			//创建工作簿
			HSSFSheet sheet = workbook.createSheet((null == sheetName?"":sheetName)+"第1页");

			writeTitleToSheet(workbook,sheet,headers,null);//设置标题

			//写入数据
			if(null != data && data.size() > 0){
				if(null != fields && fields.length>0){
					writeDataToSheet(workbook,sheet,sheetName,cellStyle,data,fields,headers);
				}else{
					writeDataToSheet(workbook,sheet,sheetName,cellStyle,beanToList(data),headers);
				}

			}
			OutputStream out = null;
			//输出文件
			fileName = fileName+".xls";
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition","attachment; filename="+URLEncoder.encode(fileName, "UTF-8") );
			out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/**
	 * 导出excel(.xlsx)
	 * @param fileName
	 * @param data
	 * @param headers
	 * @param fields
	 * @param response
	 */
	public static void writeExcelToXLSX(String fileName,String sheetName,List<?> data,String[] headers,String[] fields ,HttpServletResponse response){
		try {
			//Excel的文档对象
			XSSFWorkbook workbook = new XSSFWorkbook();

			//创建新字体
			XSSFFont font = workbook.createFont();
			font.setFontName("微软雅黑");//设置字体
			font.setFontHeightInPoints((short) 12);//设置字体大小

			//单元格样式(3.16以下的写法)
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);//设置单元格对齐方式为水平居左
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//设置单元格对齐方式为垂直居中
			cellStyle.setFont(font);//设置字体样式

			//表头样式(3.16写法)
			XSSFCellStyle titleStyle = workbook.createCellStyle();
			titleStyle.setAlignment(HorizontalAlignment.CENTER);//设置单元格对齐方式为水平居中
			titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//设置单元格对齐方式为垂直居中
			titleStyle.setFillBackgroundColor(new HSSFColor.TURQUOISE().getIndex());//设置背景色
			titleStyle.setFont(font);//设置字体样式


			/**设置单元格格式为文本格式*/
			XSSFCellStyle textStyle = workbook.createCellStyle();
			XSSFDataFormat format = workbook.createDataFormat();
			textStyle.setDataFormat(format.getFormat("@"));

			//Excel的表单
			XSSFSheet sheet = workbook.createSheet((null == sheetName?"":sheetName)+"第1页");

			writeTitleToSheet(workbook,sheet,headers,null);//设置标题

			//写入数据
			if(null != data && data.size() > 0){
				if(null != fields && fields.length>0){
					writeDataToSheet(workbook,sheet,sheetName,cellStyle,data,fields,headers);
				}else{
					writeDataToSheet(workbook,sheet,sheetName,cellStyle,beanToList(data),headers);
				}
			}
			OutputStream out = null;
			//输出文件
			fileName = fileName+".xlsx";
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.addHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-disposition","attachment; filename="+URLEncoder.encode(fileName, "UTF-8") );
			out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/**
	* spring MVC 文件处理方法(.xls)
	* @param file
	* @return 电子工作簿
	*/
	private static Workbook loadXLS(MultipartFile file) {
		Workbook workbook = null;
		try {
			POIFSFileSystem fs = new POIFSFileSystem(file.getInputStream());
			workbook = new HSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("文件解析错误！");
		}
		return workbook;
	}

	/**
	 * spring MVC 文件处理方法(.xlsx)
	 * @param upfile
	 * @return 电子工作簿
	 */
	private static Workbook loadXLSX(MultipartFile upfile) {
		Workbook workbook = null;
		OPCPackage op;
		try {
			op = OPCPackage.open(upfile.getInputStream());
			workbook = new XSSFWorkbook(op);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			System.err.println("文件解析错误");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("文件提取错误！");
		}

		return workbook;
	}
	
	/**
     * 读取单元格数据
     * @param cell 要读取数据的单元格
     * @return 有内容则返回{@link Object}类型的数据，否则返回<code>null</code>。
     */
	private static Object readCellData(Cell cell){
        Object cellContent = null;
        try {
            if(null != cell){
                if(CellType.NUMERIC == cell.getCellTypeEnum()){//数字类型(去掉科学计算法)
                    DecimalFormat df = new DecimalFormat("0");  
                    cellContent = df.format(cell.getNumericCellValue());
                }else if(CellType.STRING == cell.getCellTypeEnum()){//字符串类型
                    cellContent = cell.getStringCellValue();
                }else if(CellType.FORMULA == cell.getCellTypeEnum()){//表达式类型
                    cellContent = cell.getCellFormula();
                }else if(CellType.BLANK == cell.getCellTypeEnum()){//链接类型
                    cellContent = cell.getHyperlink();
                }else if(CellType.BOOLEAN == cell.getCellTypeEnum()){//布尔值类型
                    cellContent = cell.getBooleanCellValue();
                }else{//Cell.CELL_TYPE_ERROR类型，一个尚且未知的类型，姑且用String类型去读取(有可能会异常)
                    cellContent = cell.getStringCellValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cellContent;
    }
	
    /**
     * 读取Excel数据写入数据库
     * @param file
     * @return 
     */
	public static List<List<Object>> readExcel(MultipartFile file) {
		List<List<Object>> data = null;//用来保存解析出来的数据
		Workbook workbook = null;
		if(file.getOriginalFilename().endsWith(OFFICE_EXCEL_XLS)){
			workbook = loadXLS(file);//加载Excel文件			
		}else if(file.getOriginalFilename().endsWith(OFFICE_EXCEL_XLSX)){			
			workbook = loadXLSX(file);//加载Excel文件
		}
		if(null != workbook){
            data = new ArrayList<List<Object>>();
            int sheets = workbook.getNumberOfSheets();//excel内不为空的sheet的总数量
            //遍历所有的sheet，取出每个sheet中的数据
            for(int sheetNum = 0; sheetNum < sheets; ++sheetNum){
                Sheet sheet = workbook.getSheetAt(sheetNum);//获取当前sheet
                int lastRowNum = sheet.getLastRowNum();//获取最后一行的下标
                //遍历当前sheet中所有的行，取出每行的数据(过滤标题行,从1开始)
                for(int rowNum = 1; rowNum <= lastRowNum; ++rowNum){
                    Row row = sheet.getRow(rowNum);//获取当前行
                    int lastCellNum = row.getLastCellNum();//获取最后一列的下标
                    List<Object> rowData = new ArrayList<Object>(lastCellNum);//用来保存一行的数据
                    //遍历所有的列，取出当前行所有的数据
                    for(int colNum = 0; colNum < lastCellNum; ++colNum){
                        Cell cell = row.getCell(colNum);//获取当前单元格
                        Object cellValue = ExcelUtils.readCellData(cell);//读取单元格数据
                        rowData.add(cellValue);
                    }
                    data.add(rowData);
                }
            }
        }
		return data;
	}

	/**
	 * 将一个 JavaBean 对象转化为一个  Map
	 * @param obj 要转化的JavaBean 对象
	 * @return
	 */
	private static Map convertBean(Object obj){
		 Map<String,Object> reMap = new HashMap<String,Object>();
	        if (obj == null)
	            return null;
	        Field[] fields = obj.getClass().getDeclaredFields();
	        try {
	            for(int i=0;i<fields.length;i++){
	                try {
	                    Field f = obj.getClass().getDeclaredField(fields[i].getName());
	                    f.setAccessible(true);
	                    Object o = f.get(obj);
	                    reMap.put(fields[i].getName(), o);
	                } catch (NoSuchFieldException e) {
	                    e.printStackTrace();
	                } catch (IllegalArgumentException e) {
	                    e.printStackTrace();
	                } catch (IllegalAccessException e) {
	                    e.printStackTrace();
	                }
	            }
	        } catch (SecurityException e) {
	            e.printStackTrace();
	        }
        return reMap;    
    }
	
	/**
	 * List<model>转换
	 * @param list 数据
	 * @return
	 */
	private static List<List<Object>> beanToList(List<?> list){
		List<List<Object>> data = null;
		List<Object> oList =new ArrayList<Object>();
		try {
			for (Object bean : list) {
				data = new ArrayList<List<Object>>();
				Class<?> type = bean.getClass();
				Field[] fields = type.getDeclaredFields();//获取所有私有方法对象
				for (Field field : fields) {
					boolean isAccessible = field.isAccessible();//读取访问权限
					if(!isAccessible){//不可访问
						field.setAccessible(true);
					}
						Object obj = field.get(bean);
						oList.add(obj);
						field.setAccessible(isAccessible);
				}
				data.add(oList);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static void main(String[] args) {

	}
}
