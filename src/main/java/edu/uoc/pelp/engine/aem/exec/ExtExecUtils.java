/*
	Copyright 2011-2012 Fundació per a la Universitat Oberta de Catalunya

	This file is part of PeLP (Programming eLearning Plaform).

    PeLP is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    PeLP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.uoc.pelp.engine.aem.exec;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Executes code in the system console, under restricted conditions.
 * @author Xavier Baró
 */
public class ExtExecUtils extends ExecUtils{

    private static long _MaxBufferSize=1000;
    
    private static synchronized void setProgrammInput(Process process,InputStream input) throws IOException {
        BufferedReader in= new BufferedReader(new InputStreamReader(input),8 * 1024);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()),8 * 1024);
        
        char[] cBuf = new char[1024];
        int bufRead;
        
        // TODO: If the System.in source is given, a programm can become idle.
        // Use in.ready();
        /*String strLine;           
        while((strLine = in.readLine()) != null) {
            out.write(strLine);
            out.write('\n');
        }*/
        if(in.ready()) {
            while ((bufRead = in.read(cBuf)) != -1) {
                out.write(cBuf, 0, bufRead);
            }
            out.close();
        }
    }

    private static synchronized BufferedReader getProgramOutputReader(Process process) throws IOException {
        return new BufferedReader(new InputStreamReader(process.getInputStream()),8 * 1024);
    }

    private static synchronized BufferedReader getProgramErrorsReader(Process process) throws IOException {
        return new BufferedReader(new InputStreamReader(process.getErrorStream()),8 * 1024);
    }

    public static Process exec(String command, File workDir, long interval, long timeout) throws IOException, InterruptedException {
        return exec(command, null, workDir,interval, timeout);
    }

    public static Process exec(String[] commands, File workDir,long interval, long timeout) throws IOException, InterruptedException {
        return exec(commands, null, workDir, interval, timeout);
    }

    public static synchronized Process exec(String command, String[] envp, File workDir, long interval, long timeout)throws IOException, InterruptedException {
        Process	process;
        process = Runtime.getRuntime().exec(command, envp,workDir);

        return processTimeout(process, interval,timeout);
    }

    public static synchronized Process exec(String[] commands, String[] envp, File workDir,long interval, long timeout)throws IOException, InterruptedException {
        Process	process;
        process = Runtime.getRuntime().exec(commands, envp,workDir);

        return processTimeout(process, interval,timeout);
    }

    public static Process exec(String command, File workDir, long interval, long timeout, InputStream input,StringBuffer output,StringBuffer errors) throws IOException, InterruptedException {
        return exec(command, null, workDir,interval, timeout, input,output,errors);
    }

    public static Process exec(String[] commands, File workDir,long interval, long timeout,InputStream input,StringBuffer output,StringBuffer errors) throws IOException, InterruptedException {
        return exec(commands, null, workDir, interval, timeout,input,output,errors);
    }

    public static synchronized Process exec(String command, String[] envp, File workDir, long interval, long timeout,InputStream input,StringBuffer output,StringBuffer errors)throws IOException, InterruptedException {
        Process	process;
        process = Runtime.getRuntime().exec(command, envp,workDir);

        if(input!=null) {
            setProgrammInput(process,input);
        }
        
        return processTimeout(process, interval,timeout,output,errors);
    }

    public static synchronized Process exec(String[] commands, String[] envp, File workDir,long interval, long timeout,InputStream input,StringBuffer output,StringBuffer errors)throws IOException, InterruptedException {
        Process	process;
        process = Runtime.getRuntime().exec(commands, envp,workDir);

        if(input!=null) {
            setProgrammInput(process,input);
        }

        return processTimeout(process, interval,timeout,output,errors);
    }

    protected static synchronized Process processTimeout(Process process, long interval, long timeout,StringBuffer output,StringBuffer errors) throws InterruptedException {
        long	time_waiting = 0;
        boolean	process_finished = false;
        BufferedReader out=null;
        BufferedReader err=null;
        String strLine;
        long writtenChars=0;
        char[] cBuf = new char[1024];
        int bufRead;

        try {
            if(output!=null) {
                try {
                    out = getProgramOutputReader(process);
                } catch (IOException ex) {
                    Logger.getLogger(ExtExecUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(errors!=null) {
                try {
                    err = getProgramErrorsReader(process);
                } catch (IOException ex) {
                    Logger.getLogger(ExtExecUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            while (time_waiting < timeout && !process_finished) {
                    process_finished = true;

                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e){
                        e.fillInStackTrace();
                        throw e;
                    }

                    try {
                        if(out!=null && output!=null) {
                            strLine=null;
                            try {
                                if(out.ready()) {
                                    /*while ((strLine = out.readLine()) != null) {
                                        writtenChars+=strLine.length();
                                        output.append(strLine);
                                        if(!out.ready()) {
                                            break;
                                        }
                                        // The first number of chars are written directly. Ones maximum is archieved, only one line per Interval is written
                                        if(writtenChars>_MaxBufferSize) {
                                            break;
                                        }
                                    }*/                                    
                                    while ((bufRead = out.read(cBuf)) != -1) {
                                        writtenChars+=bufRead;
                                        output.append(cBuf, 0, bufRead);
                                        if(!out.ready()) {
                                            break;
                                        }
                                        // The first number of chars are written directly. Ones maximum is archieved, only one line per Interval is written
                                        if(writtenChars>_MaxBufferSize) {
                                            break;
                                        }
                                    }
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(ExtExecUtils.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if(err!=null && errors!=null) {
                            strLine=null;
                            try {
                                 if(err.ready()) {
                                    /*while ((strLine = err.readLine()) != null) {
                                        writtenChars+=strLine.length();
                                        errors.append(strLine);
                                        if(!err.ready()) {
                                            break;
                                        }
                                        // The first number of chars are written directly. Ones maximum is archieved, only one line per Interval is written
                                        if(writtenChars>_MaxBufferSize) {
                                            break;
                                        }
                                    }*/
                                    while ((bufRead = err.read(cBuf)) != -1) {
                                        writtenChars+=bufRead;
                                        errors.append(cBuf, 0, bufRead);
                                        if(!err.ready()) {
                                            break;
                                        }
                                        // The first number of chars are written directly. Ones maximum is archieved, only one line per Interval is written
                                        if(writtenChars>_MaxBufferSize) {
                                            break;
                                        }
                                    }
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(ExtExecUtils.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        process.exitValue();
                    } catch (IllegalThreadStateException e) {
                        // process hasn't finished yet
                        process_finished = false;
                    }
                    time_waiting += interval;
            }

            if (process_finished){
                return process;
            } else {
                process.destroy();
                return null;
            }
        } catch (OutOfMemoryError notMem) {
            process.destroy();
            return null;
        }
    }
}
