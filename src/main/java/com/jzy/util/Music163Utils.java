package com.jzy.util;


import com.jzy.model.Music163;
import com.jzy.model.Music163PageInfo;
import com.jzy.model.MyPage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName Music163Utils
 * @Author JinZhiyun
 * @Description 网易云音乐工具类
 * @Date 2020/5/15 20:17
 * @Version 1.0
 **/
public class Music163Utils {
    /**
     * mp3外链
     */
    public static final String OUTER_MP3_URL = "http://music.163.com/song/media/outer/url";

    /**
     * 歌曲详情api
     */
    public static final String SONGS_DETAIL_API = "http://music.163.com/api/song/detail/";

    /**
     * 歌曲搜索api，首选
     */
    public static final String MUSIC_SEARCH_API_0 = "https://v1.alapi.cn/api/music/search";

    /**
     * 歌曲搜索api，备选
     */
    public static final String MUSIC_SEARCH_API_1 = "https://music.jeeas.cn/v1/search";


    /**
     * 根据歌曲id获得对应mp3下载外链
     *
     * @param id 歌曲id
     * @return mp3下载外链
     */
    public static String getOuterMp3Url(Integer id) {
        if (id == null) {
            return null;
        }
        return OUTER_MP3_URL + "?id=" + id;
    }

    /**
     * 获取多首歌曲信息的url链接
     *
     * @param ids 多首歌曲的id
     * @return url链接
     */
    public static String getSongDetailApi(List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return null;
        }
        String url = SONGS_DETAIL_API + "?ids=[";
        for (int i = 0; i < ids.size(); i++) {
            if (i != 0) {
                url += ",";
            }
            url += ids.get(i).toString();
        }
        url += "]";
        return url;
    }

    /**
     * 获得搜索音乐的api，，首选
     *
     * @param myPage  分页{页号，每页数量}
     * @param keyword 查询关键字
     * @return
     */
    public static String getMusicSearchApi0(MyPage myPage, String keyword) {
        //keyword:关键字，limit：每页数量，offset：偏移，(页号-1)*每页数量
        return MUSIC_SEARCH_API_0 + "?keyword=" + keyword + "&limit=" + myPage.getPageSize() + "&offset=" + (myPage.getPageNum() - 1) * myPage.getPageSize();
    }

    /**
     * 获得搜索音乐的api，，首选
     *
     * @param myPage  分页{页号，每页数量}
     * @param keyword 查询关键字
     * @return
     */
    public static String getMusicSearchApi1(MyPage myPage, String keyword) throws UnsupportedEncodingException {
        //keyword:关键字，limit：每页数量，offset：偏移，(页号-1)*每页数量
        return MUSIC_SEARCH_API_1 + "?s=" + URLEncoder.encode(keyword ,"UTF-8") + "&limit=" + myPage.getPageSize() + "&offset=" + (myPage.getPageNum() - 1) * myPage.getPageSize();
    }

    /**
     * 根据id获取网易云歌曲的详细信息
     *
     * @param id 歌曲id
     * @return 歌曲的详细信息
     * @throws Exception
     */
    public static Music163 getMusic(Integer id) throws Exception {
        if (id == null) {
            return null;
        }
        Music163 music163 = new Music163();

        HttpURLConnection connection;
        URL url;
        BufferedReader bf;
        StringBuffer sb = new StringBuffer();

        url = new URL(getSongDetailApi(Collections.singletonList(id)));
        connection = (HttpURLConnection) url.openConnection();
        bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        String line = "";
        while (null != (line = bf.readLine())) {
            sb.append(line);
        }

        JSONObject jsonObject = JSONObject.fromObject(sb.toString());
        JSONArray songs = jsonObject.getJSONArray("songs");
        JSONObject song1 = songs.getJSONObject(0);
        music163.setId((Integer) song1.get("id"));
        music163.setName((String) song1.get("name"));
        JSONArray artists = song1.getJSONArray("artists");
        for (int i = 0; i < artists.size(); i++) {
            JSONObject artist = artists.getJSONObject(i);
            music163.addArtist((String) artist.get("name"));
        }
        JSONObject album = song1.getJSONObject("album");
        music163.setAlbum((String) album.get("name"));

        music163.setMp3Url(getOuterMp3Url(id));

        return music163;
    }

    /**
     * 获得网易云信息查询的分页结果
     *
     * @param myPage  分页{页号，每页数量}
     * @param keyword 查询关键字
     * @return 分页结果
     */
    public static Music163PageInfo getMusicPageInfo(MyPage myPage, String keyword) {
        //首选方案
        Music163PageInfo pageInfo0 = getMusicPageInfoSolution0(myPage, keyword);
        if (pageInfo0 != null) {
            return pageInfo0;
        }

        //备选方案
        return getMusicPageInfoSolution1(myPage, keyword);
    }

    public static Music163PageInfo getMusicPageInfoSolution0(MyPage myPage, String keyword) {
        Music163PageInfo music163PageInfo = new Music163PageInfo();
        music163PageInfo.setPageNum(myPage.getPageNum());
        music163PageInfo.setPageSize(myPage.getPageSize());
        List<Music163> music163List = new ArrayList<>();
        int count = 0;


        URLConnection connection;
        URL url;
        BufferedReader bf;
        StringBuffer sb = new StringBuffer();

        try {
            url = new URL(getMusicSearchApi0(myPage, keyword));
            connection = url.openConnection();
            bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = "";
            while (null != (line = bf.readLine())) {
                sb.append(line);
            }

            JSONObject jsonObject = JSONObject.fromObject(sb.toString());
            JSONObject data = jsonObject.getJSONObject("data");
            count = (int) data.get("songCount");
            JSONArray songs = data.getJSONArray("songs");
            for (int i = 0; i < songs.size(); i++) {
                JSONObject song = songs.getJSONObject(i);
                Music163 music163 = new Music163();
                music163.setId((Integer) song.get("id"));
                music163.setName((String) song.get("name"));
                JSONArray artists = song.getJSONArray("artists");
                for (int j = 0; j < artists.size(); j++) {
                    JSONObject artist = artists.getJSONObject(j);
                    music163.addArtist((String) artist.get("name"));
                }
                JSONObject album = song.getJSONObject("album");
                music163.setAlbum((String) album.get("name"));
                music163.setMp3Url(getOuterMp3Url(music163.getId()));

                music163List.add(music163);
            }
        } catch (Exception e) {
            return null;
        }
        music163PageInfo.setTotal(count);
        music163PageInfo.setMusicList(music163List);

        return music163PageInfo;
    }

    public static Music163PageInfo getMusicPageInfoSolution1(MyPage myPage, String keyword) {
        Music163PageInfo music163PageInfo = new Music163PageInfo();
        music163PageInfo.setPageNum(myPage.getPageNum());
        music163PageInfo.setPageSize(myPage.getPageSize());
        List<Music163> music163List = new ArrayList<>();
        int count = 0;


        URLConnection connection;
        URL url;
        BufferedReader bf;
        StringBuffer sb = new StringBuffer();

        try {
            url = new URL(getMusicSearchApi1(myPage, keyword));
            connection = url.openConnection();
            bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = "";
            while (null != (line = bf.readLine())) {
                sb.append(line);
            }

            JSONObject jsonObject = JSONObject.fromObject(sb.toString());
            JSONObject data = jsonObject.getJSONObject("result");
            count = (int) data.get("songCount");
            JSONArray songs = data.getJSONArray("songs");
            for (int i = 0; i < songs.size(); i++) {
                JSONObject song = songs.getJSONObject(i);
                Music163 music163 = new Music163();
                music163.setId((Integer) song.get("id"));
                music163.setName((String) song.get("name"));
                JSONArray artists = song.getJSONArray("ar");
                for (int j = 0; j < artists.size(); j++) {
                    JSONObject artist = artists.getJSONObject(j);
                    music163.addArtist((String) artist.get("name"));
                }
                JSONObject album = song.getJSONObject("al");
                music163.setAlbum((String) album.get("name"));
                music163.setMp3Url(getOuterMp3Url(music163.getId()));

                music163List.add(music163);
            }
        } catch (Exception e) {
            return null;
        }
        music163PageInfo.setTotal(count);
        music163PageInfo.setMusicList(music163List);

        return music163PageInfo;
    }

    /**
     * 下载网易云音乐到mp3，另存为到相应目录下，文件名用id命名
     *
     * @param id            音乐id
     * @param saveDirectory 保存路径
     * @throws Exception
     */
    public static void downloadMusic(Integer id, String saveDirectory) throws Exception {
        String fileName = id + ".mp3";
        downloadMusic(id, saveDirectory, fileName);
    }

    public static void downloadMusic(Integer id, String saveDirectory, String fileName) throws Exception {
        String url = getOuterMp3Url(id);
        FileUtils.downloadFile(url, saveDirectory, fileName);
    }


    public static void main(String[] args) throws Exception {
        Music163Utils.downloadMusic(1308436740, "C:\\Users\\92970\\Desktop\\");
    }
}