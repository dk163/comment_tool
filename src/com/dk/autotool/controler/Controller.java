package com.dk.autotool.controler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.dk.autotool.model.HeaderFileModel;
import com.dk.autotool.model.NoteModel;

public class Controller
{
  private static final int LINE_LENGTH = 80;
  private StringBuffer mResult;
  private String mRecord;
  private boolean hasRecord;
  private int max;
  
  public Controller()
  {
    this.mResult = new StringBuffer();
  }
  
  public String getHeader(int category, String type, HeaderFileModel headerFileModel)
  {
    this.mResult = new StringBuffer();
    String begin = "|";
    String end = "|";
    if (type.equals("xml"))
    {
      createXmlHeader(category, headerFileModel, begin, end, "*");
    }
    else if (type.equals("java"))
    {
      begin = "/*";
      end = "*/";
      createHeader(category, headerFileModel, begin, end, "-");
    }
    else
    {
      begin = "#";
      end = "";
      createHeader(category, headerFileModel, begin, end, "-");
    }
    return trim_trailing(this.mResult.toString());
  }
  
  private String trim_trailing(String string)
  {
    String result = "";
    String[] arrayOfString;
    int j = (arrayOfString = string.split("\n")).length;
    for (int i = 0; i < j; i++)
    {
      String line = arrayOfString[i];
      result = result + line.trim() + "\n";
    }
    return result;
  }
  
  private void createModify(int category, HeaderFileModel headerFileModel, String begin, String end, String sig)
  {
    center("Modifications on Features list / Changes Request / Problems Report", 
      begin, end);
    
    cLine(sig, begin, end);
    
    createTable(headerFileModel.getMModifDate(), headerFileModel.getMAuthor(), 
      headerFileModel.getMKey(), headerFileModel.getMComment(), 
      begin, end, sig);
  }
  
  private void createXmlHeader(int category, HeaderFileModel headerFileModel, String begin, String end, String sig)
  {
    switch (category)
    {
    case 0: 
    case 2: 
      this.mResult.append("<!--\n");
      
      cLine("=", 80);
      this.mResult.append("\n");
      createFileHeader(category, headerFileModel, begin, end, sig);
      if (category != 2) {
        this.mResult.append("-->");
      }
      break;
    case 1: 
      if (category != 2)
      {
        this.mResult.append("<!--\n");
        cLine("=", begin, end);
      }
      createModify(category, headerFileModel, begin, end, sig);
      
      cLine("=", 80);
      this.mResult.append("\n-->");
      break;
    }
  }
  
  private void createFileHeader(int category, HeaderFileModel headerFileModel, String begin, String end, String sig)
  {
    this.mResult.append(begin);
    for (int i = 0; i < 80 - begin.length() - end.length() - "Date:".length() - 
          headerFileModel.getMDate().length() - 1; i++) {
      this.mResult.append(" ");
    }
    this.mResult.append("Date:" + headerFileModel.getMDate());
    this.mResult.append(" " + end + "\n");
    
    center("PRESENTATION", begin, end);
    cLine(" ", begin, end);
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
    
    center("Copyright " + sdf.format(new Date()) + 
      " TCL Communication Technology Holdings Limited.", 
      begin, end);
    cLine(" ", begin, end);
    cStrLine("This material is company confidential, cannot be reproduced in any form", 
      begin, end);
    cStrLine("without the written permission of TCL Communication Technology Holdings", 
      begin, end);
    cStrLine("Limited.", begin, end);
    cLine(" ", begin, end);
    
    cLine(sig, begin, end);
    
    appendUnknowLength(" Author :  ", headerFileModel.getMAuthor(), begin, end);
    
    appendUnknowLength(" Email  :  ", headerFileModel.getMEmail(), begin, end);
    
    appendUnknowLength(" Role   :  ", headerFileModel.getMRole(), begin, end);
    
    appendUnknowLength(" Reference documents : ", 
      headerFileModel.getMReferenceDoc(), begin, end);
    cLine(sig, begin, end);
    
    appendUnknowLength(" Comments : ", headerFileModel.getMComments(), 
      begin, end);
    
    appendUnknowLength(" File     : ", headerFileModel.getMFile(), begin, end);
    
    appendUnknowLength(" Labels   : ", headerFileModel.getMLabels(), 
      begin, end);
    
    cLine(sig, begin, end);
    cLine("=", begin, end);
  }
  
  private void createHeader(int category, HeaderFileModel headerFileModel, String begin, String end, String sig)
  {
    switch (category)
    {
    case 0: 
    case 2: 
      if (!begin.equals("|")) {
        cLineNot(begin.substring(begin.length() - 1, 
          begin.length()), begin, end);
      }
      createFileHeader(category, 
        headerFileModel, begin, end, sig);
      if (category != 2) {
        break;
      }
    case 1: 
      if (category != 2) {
        cLine("=", begin, end);
      }
      createModify(category, headerFileModel, begin, end, sig);
      
      cLineNot(begin.substring(begin.length() - 1, 
        begin.length()), begin, end);
      break;
    }
  }
  
  private void cStrLine(String content, String begin, String end)
  {
    begin = begin + " ";
    end = " " + end;
    
    this.mResult.append(begin);
    int length = content.length();
    int shouldLength = 80 - begin.length() - end.length();
    if (shouldLength - length >= 0)
    {
      this.mResult.append(content);
      for (int i = 0; i < shouldLength - length; i++) {
        this.mResult.append(" ");
      }
      this.mResult.append(end + "\n");
      return;
    }
    int hight = (int)Math.ceil(length / shouldLength);
    hight++;
    for (int i = 0; i < hight; i++)
    {
      if (i != 0) {
        this.mResult.append(begin);
      }
      if (i != hight - 1)
      {
        this.mResult.append(content.substring(i * shouldLength, 
          shouldLength + i * shouldLength));
      }
      else
      {
        String last = content.substring(i * shouldLength, 
          content.length());
        this.mResult.append(last);
        for (int j = 0; j < shouldLength - last.length(); j++) {
          this.mResult.append(" ");
        }
      }
      this.mResult.append(end + "\n");
    }
  }
  
  private void createTable(String modifDate, String author, String key, String comment, String begin, String end, String sig)
  {
    begin = begin + " ";
    end = " " + end;
    int dateLengh = 11;
    int partLen = 23;
    int part4 = 80 - partLen * 2 - 11 - 
      end.length() - begin.length();
    
    this.mResult.append(begin);
    centerBlank("date", 10);
    this.mResult.append("|");
    centerBlank("author", partLen - 1);
    this.mResult.append("|");
    centerBlank("Key", partLen - 1);
    this.mResult.append("|");
    centerBlank("comment", part4);
    this.mResult.append(end + "\n");
    
    this.mResult.append(begin);
    cLine(sig, 10);
    this.mResult.append("|");
    cLine(sig, partLen - 1);
    this.mResult.append("|");
    cLine(sig, partLen - 1);
    this.mResult.append("|");
    cLine(sig, part4);
    this.mResult.append(end + "\n");
    
    ArrayList<String> list1 = Util.getFormatList(modifDate, 10);
    ArrayList<String> list2 = Util.getFormatList(author, partLen - 1);
    ArrayList<String> list3 = Util.getFormatList(key, partLen - 1);
    ArrayList<String> list4 = Util.getFormatList(comment, part4);
    this.max = list4.size();
    for (int i = 0; i < this.max; i++)
    {
      this.mResult.append(begin);
      try
      {
        centerBlank((String)list1.get(i), 10);
      }
      catch (Exception e)
      {
        centerBlank(" ", 10);
      }
      this.mResult.append("|");
      try
      {
        centerBlank((String)list2.get(i), partLen - 1);
      }
      catch (Exception e)
      {
        centerBlank(" ", partLen - 1);
      }
      this.mResult.append("|");
      try
      {
        centerBlank((String)list3.get(i), partLen - 1);
      }
      catch (Exception e)
      {
        centerBlank(" ", partLen - 1);
      }
      this.mResult.append("|");
      if (i < list4.size() - 1) {
        centerBlank((String)list4.get(i), part4);
      } else {
        cLineBlank((String)list4.get(i), part4);
      }
      this.mResult.append(end + "\n");
    }
    this.mResult.append(begin);
    cLine(sig, 10);
    this.mResult.append("|");
    cLine(sig, partLen - 1);
    this.mResult.append("|");
    cLine(sig, partLen - 1);
    this.mResult.append("|");
    cLine(sig, 80 - partLen * 2 - 11 - 
      end.length() - begin.length());
    this.mResult.append(end + "\n");
  }
  
  private void cLine(String string, int length)
  {
    for (int i = 0; i < length; i++) {
      this.mResult.append(string);
    }
  }
  
  private void centerBlank(String string, int length)
  {
    int blank = length - string.length();
    for (int i = 0; i < blank / 2; i++) {
      this.mResult.append(" ");
    }
    this.mResult.append(string);
    for (int i = 0; i < blank - blank / 2; i++) {
      this.mResult.append(" ");
    }
  }
  
  private void cLine(String string, String begin, String end)
  {
    begin = begin + " ";
    end = " " + end;
    this.mResult.append(begin);
    for (int i = 0; i < 80 - begin.length() - end.length(); i++) {
      this.mResult.append(string);
    }
    this.mResult.append(end + "\n");
  }
  
  private void cLineNot(String string, String begin, String end)
  {
    this.mResult.append(begin);
    for (int i = 0; i < 80 - begin.length() - end.length(); i++) {
      this.mResult.append(string);
    }
    this.mResult.append(end + "\n");
  }
  
  private void center(String string, String begin, String end)
  {
    this.mResult.append(begin);
    int blank = 80 - begin.length() - 
      end.length() - string.length();
    for (int i = 0; i < blank / 2; i++) {
      this.mResult.append(" ");
    }
    this.mResult.append(string);
    for (int i = 0; i < blank - blank / 2; i++) {
      this.mResult.append(" ");
    }
    this.mResult.append(end + "\n");
  }
  
  private void appendUnknowLength(String title, String content, String begin, String end)
  {
    begin = begin + " ";
    end = " " + end;
    this.mResult.append(begin);
    this.mResult.append(title);
    int shouldLength = 80 - begin.length() - 
      end.length() - title.length();
    
    ArrayList<String> contentList = Util.getFormatList(content, shouldLength);
    for (int i = 0; i < contentList.size(); i++)
    {
      if (i > 0)
      {
        this.mResult.append(begin);
        centerBlank(" ", 80 - shouldLength - begin.length() - end.length());
      }
      cLineBlank((String)contentList.get(i), shouldLength);
      this.mResult.append(end + "\n");
    }
  }
  
  private void cLineBlank(String string, int shouldLength)
  {
    int blank = shouldLength - string.length();
    this.mResult.append(string);
    for (int i = 0; i < blank; i++) {
      this.mResult.append(" ");
    }
  }
  
  public String getNote(String cType, String operate, int lineNum, String type, NoteModel noteModel)
  {
    this.mResult = new StringBuffer();
    String end;
    String begin;
    if (type.equals("xml"))
    {
      begin = "<!--";
      end = "-->";
    }
    else
    {
      if (type.equals("java"))
      {
        begin = "//";
        end = "";
      }
      else
      {
        begin = "#";
        end = "";
      }
    }
    this.mResult.append(begin);
    if (lineNum == 1)
    {
      this.mResult.append("[" + cType + "]-" + 
        operate + " " /*+ "by SCDTABLET."*/ + noteModel.getMAuthor() + "," + 
        noteModel.getMDate());
      if (!noteModel.getMBugNum().trim().equals("")) {
        this.mResult.append("," + noteModel.getMBugNum());
      }
      this.mResult.append(",");
      addReason(noteModel.getMReason(), begin, end);
    }
    else if (lineNum == 2)
    {
      this.mResult.append("[" + cType + "]-" + 
        operate + "-BEGIN " /*+ "by SCDTABLET."*/ + noteModel.getMAuthor() + "," + 
        noteModel.getMDate());
      if (!noteModel.getMBugNum().trim().equals("")) {
        this.mResult.append("," + noteModel.getMBugNum());
      }
      this.mResult.append(",");
      addReason(noteModel.getMReason(), begin, end);
      this.mResult.append("\n\n");
      
      this.mResult.append(begin + "[" + cType + "]-" + 
        operate + "-END " /*+ "by SCDTABLET."*/ + 
        noteModel.getMAuthor() + end);
    }
    return trim_trailing(this.mResult.toString());
  }
  
  private void addReason(String mReason, String begin, String end)
  {
    if (mReason.length() + end.length() + this.mResult.length() <= 80)
    {
      this.mResult.append(mReason);
      cLineBlank(" ", 80 - this.mResult.length() - end.length());
      this.mResult.append(end);
      return;
    }
    cLineBlank(" ", 80 - this.mResult.length() - end.length());
    this.mResult.append(end + "\n");
    int shouldLength = 80 - begin.length() - end.length();
    ArrayList<String> list = Util.getFormatList(mReason, shouldLength);
    for (String str : list) {
      this.mResult.append(begin + str + end + "\n");
    }
  }
  
  public String getRecord()
  {
    return this.mRecord;
  }
  
  public void createRecord()
  {
    this.mRecord = "";
    boolean start = false;
    boolean flag = false;
    int count = 0;
    String[] arrayOfString;
    int j = (arrayOfString = this.mResult.toString().split("\n")).length;
    for (int i = 0; i < j; i++)
    {
      String line = arrayOfString[i];
      if ((line.contains("date")) && 
        (line.contains("author")) && 
        (line.contains("Key")) && 
        (line.contains("comment")))
      {
        start = true;
      }
      else if (start)
      {
        flag = true;
        start = false;
      }
      else if (flag)
      {
        this.mRecord += line.trim();
        count++;
        if (count >= this.max + 1)
        {
          flag = false;
          break;
        }
        this.mRecord += "\n";
      }
    }
    if ((this.mRecord != null) && (!"".equals(this.mRecord))) {
      this.hasRecord = true;
    } else {
      this.hasRecord = false;
    }
  }
  
  public boolean hasRecord()
  {
    return this.hasRecord;
  }
  
  public String getHtml(String string)
  {
    String page = "<html><head><Style Type=\"text/css\">body{    font-family: monospace;    white-space: nowrap;}</Style></head><body>";
    
    page = page + formatHtml(string).replaceAll("<!--", "&lt;!--");
    page = page + "</body></html>";
    return page;
  }
  
  private String formatHtml(String string)
  {
    string = string.replaceAll(" ", "&nbsp;");
    String body = "";
    boolean start = false;
    boolean flag = false;
    int count = 0;
    String[] arrayOfString;
    int j = (arrayOfString = string.split("\n")).length;
    for (int i = 0; i < j; i++)
    {
      String line = arrayOfString[i];
      if ((line.contains("date")) && 
        (line.contains("author")) && 
        (line.contains("Key")) && 
        (line.contains("comment")))
      {
        start = true;
        body = body + line;
        body = body + "<br/>";
      }
      else if (start)
      {
        start = false;
        flag = true;
        
        body = body + line;
        body = body + "<br/>";
      }
      else if (flag)
      {
        body = body + "<font color='red'>";
        body = body + line.trim();
        body = body + "</font>";
        count++;
        if (count >= this.max + 1) {
          flag = false;
        }
        body = body + "<br/>";
      }
      else
      {
        body = body + line;
        body = body + "<br/>";
      }
    }
    return body;
  }
}
