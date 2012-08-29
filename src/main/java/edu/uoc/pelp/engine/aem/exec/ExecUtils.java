/*
 * Copyright 2001-2007 Geert Bevin (gbevin[remove] at uwyn dot com)
 * Licensed under the Apache License, Version 2.0 (the "License")
 * $Id: ExecUtils.java 3884 2007-08-22 08:52:24Z gbevin $
 */
package edu.uoc.pelp.engine.aem.exec;
import java.io.IOException;
import java.util.Map;

public abstract class ExecUtils {
    public static Process exec(String command) throws IOException, InterruptedException {
        return exec(command, null);
    }

    public static Process exec(String command, String[] envp) throws IOException, InterruptedException {
        Process process = null;
        if (null == envp) {
            process = Runtime.getRuntime().exec(command);
        } else {
            process = Runtime.getRuntime().exec(command, envp);
	}

	try {
            process.waitFor();
	} catch (InterruptedException e){
            throw e;
	}

	return process;
    }

    public static Process exec(String commands[], String[] envp) throws IOException, InterruptedException {
        Process process = null;
        if (null == envp) {
                process = Runtime.getRuntime().exec(commands);
        } else {
                process = Runtime.getRuntime().exec(commands, envp);
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw e;
        }

        return process;
    }

    public static Process exec(String command, long interval, long timeout) throws IOException, InterruptedException {
        return exec(command, null, interval, timeout);
    }

    public static Process exec(String[] commands, long interval, long timeout) throws IOException, InterruptedException
    {
        return exec(commands, null, interval, timeout);
    }

    public static synchronized Process exec(String command, String[] envp, long interval, long timeout)throws IOException, InterruptedException {
        Process	process = null;
        if (null == envp) {
                process = Runtime.getRuntime().exec(command);
        } else {
                process = Runtime.getRuntime().exec(command, envp);
        }

        return processTimeout(process, interval,timeout);
    }

    public static synchronized Process exec(String[] commands, String[] envp, long interval, long timeout)throws IOException, InterruptedException {
        Process	process = null;
        if (null == envp) {
                process = Runtime.getRuntime().exec(commands);
        } else {
                process = Runtime.getRuntime().exec(commands, envp);
        }

        return processTimeout(process, interval,timeout);
    }

    protected static synchronized Process processTimeout(Process process, long interval, long timeout) throws InterruptedException {
        long	time_waiting = 0;
        boolean	process_finished = false;

        while (time_waiting < timeout && !process_finished) {
                process_finished = true;
                try {
                    //Thread.sleep(interval);
                    Thread.sleep(interval);
                } catch (InterruptedException e){
                    e.fillInStackTrace();
                    throw e;
                }

                try {
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
    }

    public static void outputSystemProperties() {
        Map.Entry property = null;
        for (Object element : System.getProperties().entrySet()) {
            property = (Map.Entry)element;
            System.out.println(property.getKey()+"="+property.getValue());
        }
    }
}
