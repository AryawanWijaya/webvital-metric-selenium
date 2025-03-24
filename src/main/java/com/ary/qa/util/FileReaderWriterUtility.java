package com.ary.qa.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileReaderWriterUtility {

  public void writeToHtml(String nameReport,String html){
    String dir =
        System.getProperty("user.dir") + "/target/"+nameReport+".html";
    try {
      BufferedWriter output = new BufferedWriter(new FileWriter(dir));
      output.write(html);
      output.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String readHtml(String location) throws FileNotFoundException {
    StringBuilder sb = new StringBuilder();
    BufferedReader input = new BufferedReader(new FileReader(location));
    String str;
    try {
      while ((str = input.readLine()) != null) {
        sb.append(str);
      }
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }
}
