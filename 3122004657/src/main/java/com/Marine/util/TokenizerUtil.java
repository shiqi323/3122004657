package com.Marine.util;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.Word;
import cn.hutool.extra.tokenizer.engine.hanlp.HanLPEngine;
import java.util.*;

/**
 * @Description 分词工具
 * @Author Marine
 **/

public class TokenizerUtil {
    public static Map<String, List<Integer>> CountWord(String filepath){
        //new 一个结果树
        Map<String, List<Integer>> resultMap = new TreeMap<String, List<Integer>> (  );
        //初始化分词引擎
        TokenizerEngine engine = new HanLPEngine (  );
        try{
            String content = FileUtil.readFile ( filepath );
        } catch (Exception e) {
            System.out.println (e.getMessage () );
            return null;
        }
        //默认用UTF-8编码
        FileReader fileReader = new FileReader ( filepath,"UTF-8" );
        String txt = fileReader.readString ();
        Result res = engine.parse ( txt );
        //解析文本
        Iterator<Word> iter = res.iterator ();
        int pos = 0;
        //遍历
        while(iter.hasNext ()){
            String temp = iter.next ().toString ();
            String after = "";
            for(int i = 0; i < temp.length ();i++){
                char c = temp.charAt ( i );
                //only 判断中文
                if(String.valueOf ( c ).matches ( "[\u4e00-\u9fa5]" )){
                    after += c;
                }
            }
            if(resultMap.get(after)==null){
                ArrayList<Integer> newList = new ArrayList<Integer> ( 100 );
                newList.add ( pos );
                resultMap.put ( after,newList );
            }
            List<Integer> tempList = resultMap.get(after);
            //记录新位置
            tempList.add(pos);
            pos++;
        }
        return resultMap;
    }

    /*
     * @param originMap 已经计算好词频的原文
     * @param copyMap 已经计算好词频的抄袭文
     */

    //向量余弦计算模型
    public static Double CosCount(Map<String, List<Integer>> originMap,Map<String, List<Integer>> copyMap){
        if(originMap == null || copyMap == null){
            return null;
        }
        int count = 0;//统计计算词量
        int result = 0;
        double fractionUp = 0;//向量余弦模型的分子
        double fractionDown1 = 0,fractionDown2 = 0;//向量余弦模型的分母
        double sum = 0;//相似度之和
        for(String key:originMap.keySet ()){
            List<Integer> wordListFromOrigin = originMap.get ( key );
            List<Integer> wordListFromCopy = copyMap.get ( key );
            //抄袭文本也存在该词则计算向量
            if(wordListFromCopy!= null){
                fractionDown1 = fractionDown2 = fractionUp = 0;
                //开始遍历每一个词的位置，准备构建余弦公式
                for(int i = 0;i<wordListFromOrigin.size ()&&i<wordListFromCopy.size ();i++){
                    Integer posFromOrigin = wordListFromOrigin.get ( i );
                    Integer posFromCopy = wordListFromCopy.get(i);
                    //计算分子、分母
                    fractionUp += posFromCopy*posFromOrigin;
                    fractionDown1 += posFromOrigin * posFromOrigin;
                    fractionDown2 += posFromCopy * posFromCopy;
                }
                fractionDown1 = Math.sqrt ( fractionDown1 );
                fractionDown2 = Math.sqrt ( fractionDown2 );
                double fractionDown = fractionDown1 * fractionDown2;
                if(fractionDown != 0){
                    sum += fractionUp/fractionDown;
                }
            }
            count ++;
        }
        //精确小数点后两位

        return sum/count;
    }
}
