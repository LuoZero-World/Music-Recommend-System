package com.llq;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.llq.algorithm.CollaborativeUtil;
import com.llq.algorithm.ContentUtil;
import com.llq.algorithm.UserCFUtil;
import com.llq.entity.tempIDStore;
import com.llq.mapper.TagMapper;
import com.llq.service.AuthorizeService;
import com.llq.service.MusicService;
import com.llq.service.TagService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MusicBackendApplicationTests {

    @Resource
    MusicService musicService;

    @Resource
    CollaborativeUtil collaborativeUtil;

    @Test
    void contextLoads() throws FileNotFoundException {
        String path = this.getClass().getResource("/music").getPath() + "/心墙.mp3";
        org.springframework.core.io.Resource resource = new InputStreamResource(new FileInputStream(path));
        System.out.println(resource);
    }

    @Test
    void MusicDownLoad(){
        //歌曲名称
        String str = "Butterfly";
        String str2 = str.replaceAll(" ", "");

        //调API获取歌曲信息
        String json1 = HttpUtil.get("https://api.cenguigui.cn/api/kuwo/?search="+str2);
        JSONArray entries = JSONUtil.parseArray(json1);
        MusicInfo musicInfo = JSONUtil.toList(entries, MusicInfo.class).get(0);

        String path1 = "D:\\Java\\Write-IDEA\\music_recommend_separation\\music_frontend\\src\\resources\\image\\";
        String path2 = "D:\\Java\\Write-IDEA\\music_recommend_separation\\music_frontend\\src\\resources\\music\\";
        //下载图片
        HttpUtil.downloadFile(musicInfo.getPic(), FileUtil.file(path1+str+".jpg"));

        //下载歌曲
        //参数 rid br
        String url = "http://mobi.kuwo.cn/mobi.s?f=web&source=oppo&type=convert_url_with_sign&rid="+musicInfo.getRid()+"&br=128kmp3";
        String json2 = HttpUtil.get(url);
        JSONObject obj1 = JSONUtil.parseObj(json2);
        String finnal_url = (String) ((JSONObject) obj1.get("data")).get("url");
//        System.out.println(finnal_url);

        HttpUtil.downloadFile(finnal_url, FileUtil.file(path2+str+".mp3"));

        String duration = "0"+(int)Math.floor(Math.random()*2+3)+":"+String.format("%02d", (int)Math.floor(Math.random()*60));
        //向数据库存入信息
        //注意名称 因为下载的歌曲可能名称不一样，会多点啥
        musicService.addMusic(musicInfo.getName(), musicInfo.artist, duration, new int[]{3, 5}, new tempIDStore());
    }

}
