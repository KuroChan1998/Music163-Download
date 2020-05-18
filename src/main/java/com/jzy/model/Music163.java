package com.jzy.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Music163
 * @Author JinZhiyun
 * @Description 网易云音乐对象封装
 * @Date 2020/5/15 21:14
 * @Version 1.0
 **/
@Data
public class Music163 implements Serializable {
    private static final long serialVersionUID = 7303031772431106758L;

    /**
     * 歌曲id
     */
    private Integer id;

    /**
     * 歌曲名称
     */
    private String name;

    /**
     * 歌手
     */
    private List<String> artists = new ArrayList<>(2);

    /**
     * 用"/"对多个歌手分割作为字符串描述，如"蔡徐坤/吴亦凡"
     */
    private String artistsToString = "";

    /**
     * 专辑
     */
    private String album;

    /**
     * MP3下载链接
     */
    private String mp3Url;

    /**
     * 为当前歌曲添加一个歌手
     *
     * @param artist 歌手名
     */
    public void addArtist(String artist){
        if (artists.size() != 0) {
            artistsToString += "/";
        }
        artistsToString += artist;
        artists.add(artist);
    }
}
