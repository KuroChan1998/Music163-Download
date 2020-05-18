package com.jzy.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Music163PageInfo
 * @Author JinZhiyun
 * @Description 查询网易云音乐返回分页信息
 * @Date 2020/5/18 9:14
 * @Version 1.0
 **/
@Data
public class Music163PageInfo implements Serializable {
    private static final long serialVersionUID = 6959031199355602397L;

    /**
     * 查询到的总数
     */
    private int total;

    /**
     * 一页限制个数
     */
    private int pageSize;

    /**
     * 当前页号，从1开始
     */
    private int pageNum;

    /**
     * 当前页的所有音乐信息
     */
    private List<Music163> musicList = new ArrayList<>();
}

