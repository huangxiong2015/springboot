/*
 * Created: 2017年1月19日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2017 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
package com.ictrade.tools.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 复制excel的内容
 * @author tongkun@yikuyi.com
 * @version 1.0.0
 */
public class ExcelCopy {

	
    /**
     * 克隆一个新的样式对象
     * @param fromWb
     * @param toWb
     * @param fromStyle
     * @since 2017年1月19日
     * @author tongkun@yikuyi.com
     */
    public  static CellStyle cloneCellStyle(Workbook fromWb,Workbook toWb,CellStyle fromStyle) {
    	CellStyle toStyle = toWb.createCellStyle();
        toStyle.setAlignment(fromStyle.getAlignment());  
        //边框和边框颜色  
        toStyle.setBorderBottom(fromStyle.getBorderBottom());  
        toStyle.setBorderLeft(fromStyle.getBorderLeft());  
        toStyle.setBorderRight(fromStyle.getBorderRight());  
        toStyle.setBorderTop(fromStyle.getBorderTop());  
        toStyle.setTopBorderColor(fromStyle.getTopBorderColor());  
        toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());  
        toStyle.setRightBorderColor(fromStyle.getRightBorderColor());  
        toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());  
          
        //背景和前景  
        toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());  
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());  
          
        toStyle.setDataFormat(fromStyle.getDataFormat());  
        toStyle.setFillPattern(fromStyle.getFillPattern());  
        Font fromFont = fromWb.getFontAt(fromStyle.getFontIndex());
        toStyle.setFont(copyFont(toWb,fromFont));
        toStyle.setHidden(fromStyle.getHidden());  
        toStyle.setIndention(fromStyle.getIndention());//首行缩进  
        toStyle.setLocked(fromStyle.getLocked());  
        toStyle.setRotation(fromStyle.getRotation());//旋转  
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());  
        toStyle.setWrapText(fromStyle.getWrapText());  
        return toStyle;
    }  
    
    /**
     * 克隆一个新的字体对象
     * @param toWb
     * @param fromFont
     * @return
     * @since 2017年1月19日
     * @author tongkun@yikuyi.com
     */
    public static Font copyFont(Workbook toWb,Font fromFont){
    	Font toFont = toWb.createFont();
        toFont.setBold(fromFont.getBold());
        toFont.setBoldweight(fromFont.getBoldweight());
        toFont.setCharSet(fromFont.getCharSet());
        toFont.setColor(fromFont.getColor());
        toFont.setFontHeight(fromFont.getFontHeight());
        toFont.setFontHeightInPoints(fromFont.getFontHeightInPoints());
        toFont.setFontName(fromFont.getFontName());
        toFont.setItalic(fromFont.getItalic());
        toFont.setStrikeout(fromFont.getStrikeout());
        toFont.setTypeOffset(fromFont.getTypeOffset());
        toFont.setUnderline(fromFont.getUnderline());
        return toFont;
    }
}
