package eu.alavio.jabbler;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import eu.alavio.jabbler.Models.AppContext;

/**
 * Global app launcher handler
 */

public class Jabbler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initXMPP();
        initLogger();
    }

    private void initLogger() {
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
                //clearing -cgit
                Process process = Runtime.getRuntime().exec("logcat -c");
                process = Runtime.getRuntime().exec("logcat -f " + logFile + ":S MyActivity:D MyActivity2:D");
                Log.d(this.getClass().getName(),"Logger is on.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(AppContext.isExternalStorageReadable()){
            //Handle only readable
            Log.e(this.getClass().getName(),"File system is read only,");
        }
        else{
            //Handle not authorized
            Log.e(this.getClass().getName(),"Cannot access file system for reading or writting.");
        }
    }

    private void initXMPP(){

    }
}
