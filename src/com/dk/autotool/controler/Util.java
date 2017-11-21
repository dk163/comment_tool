package com.dk.autotool.controler;

import java.util.ArrayList;

public class Util
{
  public static ArrayList<String> getFormatList(String comment, int part4)
  {
    ArrayList<String> commtsList = new ArrayList();
    int sum = comment.length();
    int current = 0;
    String tmp1 = "";
    String tmp2 = "";
    String tmp0 = "";
    boolean isOffset = false;
    int s = 0;
    int e = 0;
    
    boolean isEnd = false;
    while (current < sum)
    {
      s = e;
      e = s + part4;
      try
      {
        tmp1 = comment.substring(s, e);
      }
      catch (Exception e1)
      {
        tmp1 = comment.substring(s, comment.length());
      }
      if (e >= comment.length()) {
        isEnd = true;
      }
      if (!isEnd) {
        try
        {
          tmp2 = comment.substring(s + part4, e + part4);
        }
        catch (Exception e1)
        {
          tmp2 = comment.substring(s + part4, comment.length());
        }
      } else {
        tmp2 = "";
      }
      if (("".equals(tmp2)) || (tmp1.endsWith(" ")) || (tmp2.startsWith(" "))) {
        isOffset = false;
      } else {
        isOffset = true;
      }
      if (isOffset) {
        e = s + part4 - 1;
      }
      try
      {
        tmp0 = comment.substring(s, e);
      }
      catch (Exception e1)
      {
        tmp0 = comment.substring(s, comment.length());
      }
      current += tmp0.length();
      if ((isOffset) && (!tmp0.endsWith(" "))) {
        tmp0 = tmp0 + "-";
      }
      commtsList.add(tmp0);
    }
    return commtsList;
  }
}
