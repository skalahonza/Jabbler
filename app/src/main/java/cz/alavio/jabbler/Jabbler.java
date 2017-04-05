package cz.alavio.jabbler;

import android.app.Application;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import cz.alavio.jabbler.Models.AppContext;

/**
 * Global app launcher handler
 */

public class Jabbler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (AppContext.isExternalStorageWritable()) {
            //On app creation
            File appDirectory = new File(Environment.getExternalStorageDirectory() + "/Jabbler");
            File logDirectory = new File(appDirectory + "/Logs");
            File logFile = new File(logDirectory, "log-" + System.currentTimeMillis() + ".txt");

            //create app folder
            if (!appDirectory.exists()) {
                appDirectory.mkdir();
            }

            //create log folder
            if (!logDirectory.exists()) {
                logDirectory.mkdir();
            }

            //clear previous and write a new one
            try{
                //clearing -c
                Process process = Runtime.getRuntime().exec("logcat -c");

                process = Runtime.getRuntime().exec("logcat -f " + logFile + ":S MyActivity:D MyActivity2:D");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(AppContext.isExternalStorageReadable()){
            //TODO handle only readable
        }
        else{
            //TODO handle not authorized
        }
    }
}
