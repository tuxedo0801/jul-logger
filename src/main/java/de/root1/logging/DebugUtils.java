/*
 * To change this license header, choose License Headers in Project Properties.
/*
 * Copyright (C) 2018 Alexander Christian <alex(at)root1.de>. All rights reserved.
 * 
 *   This is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.root1.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.logging.LogManager;

public class DebugUtils {

  public static void checkEnableDebug() {
    String property = System.getProperty("de.root1.debug", "false");
    boolean debugEnabled = Boolean.parseBoolean(property);
    if (debugEnabled) {
      enableDebug(null);
    }
  }

  public static void enableDebug(Map<String, String> loggerAndLevel) {

    System.out.println("ENABLING DEBUG LOG");
    try {
      File f = new File("de.root1.debuglogging.properties");
      if (!f.exists()) {
        System.out.println("debug logging properties does not exist. Creating default '" + f.getAbsolutePath() + "' ...");
        FileWriter fw = new FileWriter(f);
        fw.write("handlers= java.util.logging.FileHandler, java.util.logging.ConsoleHandler" + "\n");
        fw.write(".level= INFO" + "\n");
        fw.write("java.util.logging.FileHandler.pattern = de.root1.debug.log" + "\n");
        fw.write("java.util.logging.FileHandler.limit = 500000" + "\n");
        fw.write("java.util.logging.FileHandler.count = 1" + "\n");
        fw.write("java.util.logging.FileHandler.formatter = de.root1.logging.JulFormatter" + "\n");
        fw.write("java.util.logging.ConsoleHandler.level = ALL " + "\n");
        fw.write("java.util.logging.ConsoleHandler.formatter = de.root1.logging.JulFormatter" + "\n");
        
        if (loggerAndLevel!=null && !loggerAndLevel.isEmpty()) {
          Set<String> keySet = loggerAndLevel.keySet();
          for (String logger : keySet) {
            String level = loggerAndLevel.get(logger);
            fw.write(logger+".level = "+level+"\n");
          }
        } else {
          fw.write("de.root1.level = ALL" + "\n");
        }
        
        fw.close();
      } else {
        System.out.println("Using existing debug logging properties: " + f.getAbsolutePath());
      }
      FileInputStream is = new FileInputStream(f);
      LogManager.getLogManager().readConfiguration(is);

    } catch (IOException ex) {
      ex.printStackTrace();
    }

    System.out.println("ENABLING DEBUG LOG *DONE*");

  }

}
