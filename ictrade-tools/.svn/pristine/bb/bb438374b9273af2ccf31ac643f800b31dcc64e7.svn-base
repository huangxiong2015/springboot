package com.ictrade.tools.excel;
  
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;  
   
/** 
  
 */ 
public class Excel2007Reader {  
   
	 //Creates a new XLSX -> CSV converter  
    // @param pkg        The XLSX package to process 
    // @param output     The PrintStream to output the CSV to 
   // @param minColumns The minimum number of columns to output, or -1 for no minimum  
    public Excel2007Reader() { }  
	
    //The type of the data value is indicated by an attribute on the cell. 
    //The value is usually in a "v" element within the cell. 
    enum xssfDataType {  
        BOOL,  
        ERROR,  
        FORMULA,  
        INLINESTR,  
        SSTINDEX,  
        NUMBER,  
    }  
   
    int countrows = 0;   
    
    private int sheetIndex = -1; 
    
    private String sheetName = "";
    
    private IRowReader rowReader; 
    
    public void setRowReader(IRowReader rowReader){  
        this.rowReader = rowReader;  
    } 
    
    
   
    // Derived from http://poi.apache.org/spreadsheet/how-to.html#xssf_sax_api 
    // <p> 
    // Also see Standard ECMA-376, 1st edition, part 4, pages 1928ff, at 
    // http://www.ecma-international.org/publications/standards/Ecma-376.htm 
    // </p><p> 
   //A web-friendly version is http://openiso.org/Ecma/376/Part4 
    class MyXSSFSheetHandler extends DefaultHandler {  
        
        private List<String> rowlist = new ArrayList<String>(); 
   
        // Table with styles 
        private StylesTable stylesTable;  
   
        //Table with unique strings  
        private ReadOnlySharedStringsTable sharedStringsTable;  
   
       //Number of columns to read starting with leftmost 
        private final int minColumnCount;  
   
        // Set when V start element is seen  
        private boolean vIsOpen;  
   
        // Set when cell start element is seen;  
        // used when cell close element is seen.  
        private xssfDataType nextDataType;  
   
        // Used to format numeric cell values.  
        private short formatIndex;  
        private String formatString;  
        private final DataFormatter formatter;  
   
        private int thisColumn = -1;  
        // The last column printed to the output stream  
        private int lastColumnNumber = -1;  
   
        // Gathers characters as they are seen.  
        private StringBuilder value;  
        private StringBuilder rowValue;
        
        private int maxNum;
   
        //Accepts objects needed while parsing.  
        // @param styles  Table of styles 
        // @param strings Table of shared strings 
        // @param cols    Minimum number of columns to show 
        // @param target  Sink for output  
        public MyXSSFSheetHandler(  
                StylesTable styles,  
                ReadOnlySharedStringsTable strings,  
                int cols) {  
            this.stylesTable = styles;  
            this.sharedStringsTable = strings;  
            this.minColumnCount = cols;  
            this.value = new StringBuilder();  
            this.nextDataType = xssfDataType.NUMBER;  
            this.formatter = new DataFormatter();  
            this.rowValue = new StringBuilder();
        }  
        
        private void setMaxNum(String column) {
        	if(StringUtils.isEmpty(column) || this.maxNum != 0){
        		return;
        	}
        	String tempColumn = column.substring(column.indexOf(':')+1,column.length());
    		this.maxNum =  Integer.parseInt(tempColumn.toUpperCase().replaceAll("[A-Z]", ""));
    		rowReader.setMaxNum(sheetIndex, this.maxNum);
    	} 
        
        // @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
        public void startElement(String uri, String localName, String name,  
                                 Attributes attributes) throws SAXException {  
        	setMaxNum(attributes.getValue("ref"));//获取最多行数
            if ("inlineStr".equals(name) || "v".equals(name)) {  
                vIsOpen = true;  
                // Clear contents cache  
                value.setLength(0);  
            }  
            // c => cell  
            else if ("c".equals(name)) {  
                // Get the cell reference  
                String r = attributes.getValue("r");  
                int firstDigit = -1;  
                for (int c = 0; c < r.length(); ++c) {  
                    if (Character.isDigit(r.charAt(c))) {  
                        firstDigit = c;  
                        break;  
                    }  
                }  
                thisColumn = nameToColumn(r.substring(0, firstDigit));  
   
                // Set up defaults.  
                this.nextDataType = xssfDataType.NUMBER;  
                this.formatIndex = -1;  
                this.formatString = null;  
                String cellType = attributes.getValue("t");  
                String cellStyleStr = attributes.getValue("s");  
                if ("b".equals(cellType))  
                    nextDataType = xssfDataType.BOOL;  
                else if ("e".equals(cellType))  
                    nextDataType = xssfDataType.ERROR;  
                else if ("inlineStr".equals(cellType))  
                    nextDataType = xssfDataType.INLINESTR;  
                else if ("s".equals(cellType))  
                    nextDataType = xssfDataType.SSTINDEX;  
                else if ("str".equals(cellType))  
                    nextDataType = xssfDataType.FORMULA;  
                else if (cellStyleStr != null) {  
                    // It's a number, but almost certainly one  
                    //  with a special style or format  
                    int styleIndex = Integer.parseInt(cellStyleStr);  
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);  
                    this.formatIndex = style.getDataFormat();  
                    this.formatString = style.getDataFormatString();  
                    if (this.formatString == null)  
                        this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);  
                }  
            }  
   
        }  
   
  
       // @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)       
        public void endElement(String uri, String localName, String name)  
                throws SAXException {  
   
            String thisStr = null;  
   
            // v => contents of a cell  
            if ("v".equals(name)) {  
                // Process the value contents as required.  
                // Do now, as characters() may be called more than once  
                switch (nextDataType) {  
   
                    case BOOL:  
                        char first = value.charAt(0);  
                        thisStr = first == '0' ? "FALSE" : "TRUE";  
                        break;  
   
                    case ERROR:  
                        thisStr = "\"ERROR:" + value.toString();  
                        break;  
   
                    case FORMULA:  
                        // A formula could result in a string value,  
                        // so always add double-quote characters.  
                        thisStr = value.toString();  
                        break;  
   
                    case INLINESTR:  
                        // TODO: have seen an example of this, so it's untested.  
                        XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());  
                        thisStr = rtsi.toString();  
                        break;  
   
                    case SSTINDEX:  
                        String sstIndex = value.toString();  
                        int idx = Integer.parseInt(sstIndex);  
                        XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));  
                        thisStr = rtss.toString();  
                        break;  
   
                    case NUMBER:  
                        String n = value.toString();  
                        if (this.formatString != null)  {
                        	if("m/d/yy".equals(this.formatString)){
                        		thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, "yyyy/m/d"); 
                        	}else{
                        		 thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString); 
                        	}
                        }else {
                        	try {
								// 如果数字格式转化不成功   则以字符串方式返回
                        		thisStr = BigDecimal.valueOf(Double.parseDouble(n)).stripTrailingZeros().toPlainString();
							} catch (Exception e) {
								thisStr = n;
							}
                        }
                        break;  
   
                    default:  
                        thisStr = "(TODO: Unexpected type: " + nextDataType + ")";  
                        break;  
                }  
   
                // Output after we've seen the string contents  
                // Emit commas for any fields that were missing on this row  
                if (lastColumnNumber == -1) {  
                    lastColumnNumber = 0;  
                }  
                for (int i = lastColumnNumber; i < thisColumn; ++i) { 
//                    rowlist.add("");
                	rowValue.append("qqq;;;");
                }
                rowValue.append(thisStr);
   
                // Update column  
                if (thisColumn > -1){
                    lastColumnNumber = thisColumn;
                }
   
            } else if ("row".equals(name)) {  
   
                // Print out any missing commas if needed  
                if (minColumns > 0) {  
                    // Columns are 0 based  
                    if (lastColumnNumber == -1) {  
                        lastColumnNumber = 0;  
                    }   
                    //for (int i = lastColumnNumber; i < (this.minColumnCount); i++) {  
                    //	rowValue.append(',');  
                    //}  
                }  
                String[] values = rowValue.toString().split("qqq;;;");
                for(int i = 0;i < values.length;i++){
                	String v = values[i];
                	rowlist.add(v);
                }
                // We're onto a new row  
                rowReader.getRows(sheetIndex, sheetName,countrows++,rowlist);
                rowValue = new StringBuilder();
                rowlist.clear();
                lastColumnNumber = -1;   
            }  
   
        }  
   
        //Captures characters only if a suitable element is open. 
        //Originally was just "v"; extended for inlineStr also. 
        public void characters(char[] ch, int start, int length)  
                throws SAXException {  
            if (vIsOpen)  
                value.append(ch, start, length);  
        }  
   
        //Converts an Excel column name like "C" to a zero-based index. 
        // @param name 
        // @return Index corresponding to the specified name  
        private int nameToColumn(String name) {  
            int column = -1;  
            for (int i = 0; i < name.length(); ++i) {  
                int c = name.charAt(i);  
                column = (column + 1) * 26 + c - 'A';  
            }  
            return column;  
        }  
   
    }  
   
    ///////////////////////////////////////  
   
    private int minColumns = 0;  
   
    //Parses and shows the content of one sheet 
    // using the specified styles and shared-strings tables.  
    // @param styles 
    // @param strings 
    // @param sheetInputStream  
    public void processSheet(  
            StylesTable styles,  
            ReadOnlySharedStringsTable strings,  
            InputStream sheetInputStream)  
            throws IOException, ParserConfigurationException, SAXException {  
   
        InputSource sheetSource = new InputSource(sheetInputStream);  
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();  
        SAXParser saxParser = saxFactory.newSAXParser();  
        XMLReader sheetParser = saxParser.getXMLReader();  
        ContentHandler handler = new MyXSSFSheetHandler(styles, strings, this.minColumns);  
        sheetParser.setContentHandler(handler);  
        sheetParser.parse(sheetSource);  
    }  
   
    // Initiates the processing of the XLS workbook file to CSV. 
     //@throws IOException 
     //@throws OpenXML4JException 
     //@throws ParserConfigurationException 
     //@throws SAXException 
      
    public void process(String path)  
            throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {  
		try(OPCPackage xlsxPackage = OPCPackage.open(path, PackageAccess.READ);) {
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);  
	        XSSFReader xssfReader = new XSSFReader(xlsxPackage);  
	   
	        StylesTable styles = xssfReader.getStylesTable();  
	        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();  
	        sheetIndex = 0;  
	        while (iter.hasNext()) {  
	        	try(InputStream stream = iter.next()){
		            sheetName = iter.getSheetName();  
		            processSheet(styles, strings, stream);  
		            ++sheetIndex;  
	        	}
	        }  
		}  
    }  
   
}  