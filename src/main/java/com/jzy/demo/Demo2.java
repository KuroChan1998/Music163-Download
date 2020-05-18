package com.jzy.demo;

import com.jzy.model.Music163;
import com.jzy.model.Music163PageInfo;
import com.jzy.model.MyPage;
import com.jzy.util.Music163Utils;

/**
 * @ClassName Demo2
 * @Author JinZhiyun
 * @Description 搜索与分页
 * @Date 2020/5/18 11:52
 * @Version 1.0
 **/
public class Demo2 {
    public static void main(String[] args) {
        //搜索与分页
        //搜索关键字
        String keyword = "我好想你";
        //分页要求：每页10条，第2页
        MyPage page = new MyPage();
        page.setPageNum(2);
        page.setPageSize(10);
        //获取"我好想你"的搜索结果的第2页
        Music163PageInfo musicPageInfo = Music163Utils.getMusicPageInfo(page, keyword);
        System.out.println("\"" + keyword + "\"的搜索结果共：" + musicPageInfo.getTotal() + "条");
        System.out.println("第" + musicPageInfo.getPageNum() + "页的" + musicPageInfo.getPageSize() + "条数据为：");
        for (Music163 music163 : musicPageInfo.getMusicList()) {
            System.out.println(music163);
        }
    }
}
