package org.mmclub.mmbaby.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 */
public class FileUtils {
    private String SDCardRoot;

    public FileUtils() {
        SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

    public File createFileInSDCard(String dir, String fileName)
            throws IOException {
        File file = new File(SDCardRoot + dir + File.separator + fileName);
        file.createNewFile();
        return file;
    }

    public File createSDDir(String dir) {
        File dirFile = new File(SDCardRoot + dir + File.separator);
        dirFile.mkdirs();
        return dirFile;
    }

    public boolean isFileExist(String path, String fileName) {
        File file = new File(path + fileName);
        return file.exists();
    }

    public ArrayList<String> readFileName(String path) {
        ArrayList<String> arrayList = new ArrayList<String>();
        File file = new File(SDCardRoot + path + File.separator);
        if (file.isDirectory()) {
            File[] fileArray = file.listFiles();
            if (null != fileArray && 0 != fileArray.length) {
                for (int i = 0; i < fileArray.length; i++) {
                    if (fileArray[i].toString().endsWith(".jpg")) {
                        arrayList.add(fileArray[i].toString());
                    }
                }
                return arrayList;
            }
        }
        return arrayList;
    }

    public Boolean write2SDFromBitmap(String path, String fileName, Bitmap bitmap) {
        if (bitmap != null) {
            if (!isFileExist(path, fileName)) {
                try {
                    File file = createFileInSDCard(path, fileName);
                    FileOutputStream out = new FileOutputStream(file);
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                        out.flush();
                        out.close();
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("TAG", "exception:"+e.toString());
                }
            }
        }
        return false;
    }
}

