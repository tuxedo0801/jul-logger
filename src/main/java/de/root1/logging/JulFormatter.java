/*
 * Copyright (C) 2015 Alexander Christian <alex(at)root1.de>. All rights reserved.
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


/**
 * This class formats the loggin-output for the console
 *
 * @version 20150804 1504
 */
public class JulFormatter extends Formatter {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String CRLF = "\r\n";

    /* (non-Javadoc)
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    @Override
    public String format(LogRecord record) {

        StringBuilder output = new StringBuilder();

        output.append(simpleDateFormat.format(new Date(record.getMillis())));
//        output.append(" [");
//        output.append(record.getLevel().getName());

        StringBuilder sb = new StringBuilder();
        // Send all output to the Appendable object sb
        java.util.Formatter formatter = new java.util.Formatter(sb, Locale.getDefault());
        formatter.format(" %s ", record.getLevel().getName());
        output.append(sb);

//        output.append("] ");
        output.append("[");
        output.append(String.format("%s", getThreadName(record.getThreadID())));
        output.append("]");
        output.append(" ");
        output.append(record.getSourceClassName());
        output.append(".");
        output.append(record.getSourceMethodName());
        output.append(": ");
        output.append(record.getMessage());
        Throwable thrown = record.getThrown();
        if (thrown != null) {
            output.append(CRLF);
            output.append(getStackTraceAsString(thrown));
        }
        output.append(CRLF);

        return output.toString();
    }

    /**
     * Returns the stacktrace of the given throwable as a string. String will be
     * the same as "e.printStackTrace();" woulld print to console
     *
     * @param e throwable to get stacktrace from
     * @return the exceptions stacktrace as a string
     */
    public static String getStackTraceAsString(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    public String getThreadName(int id) {
        Thread[] tarray = new Thread[Thread.activeCount()];
        int i = Thread.enumerate(tarray);

        for (Thread t : tarray) {
            if (t.getId() == id) {
                return t.getName();
            }
        }
        return "Thread[id=" + i + "]";
    }
    
    public static void set() {
        JulFormatter formatter = new JulFormatter();
        Logger logger = java.util.logging.Logger.getLogger("");
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            handler.setFormatter(formatter);
        }
        logger.log(Level.INFO, "Set formatter to "+JulFormatter.class.getCanonicalName());
    }
}
