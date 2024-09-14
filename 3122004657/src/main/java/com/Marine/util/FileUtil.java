package com.Marine.util;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import com.Marine.Exception.FileException;

public class FileUtil {
    /**
     *
     * @param filepath 文件路径
     * @return
     */
    public static String readFile(String filepath) throws FileException {
        if(filepath == null || "".equals ( filepath )){
            throw new FileException ( "文件路径为空" );
        }
        FileReader fileReader = null;
        try{
            fileReader = new FileReader ( filepath );
        }catch (Exception e){
            throw new FileException ( "文件为空" );
        }
        return fileReader.readString ();
    }
    public static boolean writeFile(String filepath,String content) {
        FileWriter fileWriter = new FileWriter ( filepath );
        try {
            fileWriter.write ( content );
        } catch (Exception e) {
            System.out.println ( "答案输出异常，答案为：" + content );
        }
        return true;
    }
}
