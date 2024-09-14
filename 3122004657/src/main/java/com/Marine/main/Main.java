package com.Marine.main;

import com.Marine.util.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String file1 = args[0];
        String file2 = args[1];
        Map<String, List<Integer>> stringListOriginMap = TokenizerUtil.CountWord(file1);
        Map<String, List<Integer>> stringListCopyMap = TokenizerUtil.CountWord(file2);
        Double similarity = TokenizerUtil.CosCount ( stringListOriginMap,stringListCopyMap );
        System.out.println ("文本相似度为"+similarity );
        FileUtil.writeFile ( args[2],String.valueOf ( similarity ) );
    }
}
