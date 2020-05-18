package com.jzy.demo;

import com.jzy.util.Music163Utils;

/**
 * @ClassName Demo1
 * @Author JinZhiyun
 * @Description //TODO
 * @Date 2020/5/18 11:50
 * @Version 1.0
 **/
public class Demo1 {
    public static void main(String[] args) {
        try {
            //根据歌曲id获取歌曲信息
            //https://music.163.com/#/song?id=26524510
            System.out.println(Music163Utils.getMusic(26524510));
        } catch (Exception e) {
            e.printStackTrace();
        }


        String saveDir = "C:\\Users\\92970\\Desktop\\";
        try {
            //将该音乐另存为...
            Music163Utils.downloadMusic(26524510, saveDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
