
import java.io.BufferedReader;

import java.io.File;

import java.io.FileOutputStream;

import java.io.FileReader;

import java.util.LinkedList;

import java.util.List;

import java.util.regex.Matcher;

import java.util.regex.Pattern;


import org.apache.poi.xssf.usermodel.XSSFCell;

import org.apache.poi.xssf.usermodel.XSSFRow;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**

 * Program to get the comments.

 * "Print this"

 */

public class App

{

	static String folderName = "/Users/807157/Downloads/student-service/Sourcefile/src";
	static String outPutFileName = "/Users/807157/Documents/CommentInfo.xlsx";
	static String outputSheetName = "COMMENT INFORMATION";

	public static void main( String[] args ){
		try {
			//"see this and check"
			File folder = new File(folderName);
			String[] files = folder.list();
			String regex1="";
			String regex2="";
			XSSFWorkbook workbook = new XSSFWorkbook();
			//Create a blank sheet
			XSSFSheet spreadsheet = workbook.createSheet( outputSheetName);
			//Create row object
			XSSFRow row;
			int rowid=0;
			row = spreadsheet.createRow(rowid++);
			XSSFCell cell;
			int cellid=0;
			cell = row.createCell(cellid++);
			cell.setCellValue("FILENAME");
			cell = row.createCell(cellid++);
			cell.setCellValue("COMMENTS");
			for (String filename : files)
			{
				String ext = filename.substring(filename.indexOf(".")+1,filename.length());
				if(null!=ext && ext.equalsIgnoreCase("java")){
					regex1 = "(?:.*/\\*(?:[^*]|(?:\\*+[^*/]))*"
							+ "\\*+/)|(?:.*//.*)";
					regex2 = "(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)";
				}else if(null!=ext && (ext.equalsIgnoreCase("c")||ext.equalsIgnoreCase("cpp"))){
					regex1 = "(?:.*/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?:.*//.*)";
					regex2 = "(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)";
				}else if(null!=ext && (ext.equalsIgnoreCase("py"))){
					regex1 = "(?:.*#.*)";
					regex2 = "(?:#.*)";

				}
				File file = new
						File(folderName+"/"+filename);
				List<String> sbList = new LinkedList<String>();
				BufferedReader br = new BufferedReader(new FileReader(file));
				List<String> commentList = new LinkedList<String>();
				StringBuilder wholeFileString = new StringBuilder();
				String st;
				while ((st = br.readLine()) != null) {
					sbList.add(st.trim());
					wholeFileString.append("\n").append(st.trim());
				}
				Pattern pattern = Pattern.compile(regex1);
				Matcher matcher = pattern.matcher(wholeFileString);
				while (matcher.find()) {
					commentList.add(matcher.group()+"\n");
				}
				int k=0;
				for(String comment:commentList) {
					if(!comment.matches("\".*\"")) {
						Pattern pattern2 = Pattern.compile(regex2);
						Matcher matcher2 = pattern2.matcher(comment);
						while (matcher2.find()){
							int indexStart =matcher2.start();
							int indexEnd = matcher2.end();
							int codeIndex = -1;
							int nextCodeIndex = -1;
							if(comment.indexOf('"')!=-1) {
								codeIndex = comment.indexOf('"');
								nextCodeIndex =comment.indexOf('"', codeIndex+1);
							}
							if(codeIndex!=-1 && nextCodeIndex!=-1 && indexStart>codeIndex && nextCodeIndex<indexEnd) {
								continue;
							}else {
								String cell2Val = matcher2.group();
								row = spreadsheet.createRow(rowid++);
								cellid=0;
								cell = row.createCell(cellid++);
								cell.setCellValue(filename);
								cell = row.createCell(cellid++);
								cell.setCellValue(cell2Val);
								System.out.println(filename+":"+cell2Val);
							}
						}
					}
				}
			}
			FileOutputStream out  = new FileOutputStream(
					new File(outPutFileName));
			workbook.write(out);
			out.close();
		}catch(Exception e){
				System.out.println("Exception occured:"+ e.getMessage());
		}

	}
}

