package com.llq.mapper;

import com.llq.dto.MusicDTO;
import com.llq.entity.AccountRating;
import com.llq.entity.tempIDStore;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

@Mapper
public interface MusicMapper {

    @Select("select `id`,`music_name`,`author`,`play_duration` from music_info order by play_num desc limit 18")
    List<MusicDTO> getHot18Musics();

    @Insert("insert into user_collect(user_id, music_id) values(#{userID}, #{musicID})")
    void addMusicCollection(@Param("userID") int userId, @Param("musicID") int musicId);

    @ResultType(Set.class)
    @Select("select `music_id` from user_collect where user_id = #{userId} order by createtime")
    Set<Integer> getAllMusicIDCollections(int userId);

    @Delete("delete from user_collect where user_id = #{userID} and music_id = #{musicID}")
    void removeMusicCollection(@Param("userID") int userId, @Param("musicID") int musicId);

    @Select("select `id`, `music_name`, `author`, `play_duration` from music_info where id=#{musicId}")
    MusicDTO getMusicByID(int musicId);

    @Update("update music_info set `play_num`=`play_num`+1 where `id`=#{musicId}")
    void addMusicPlayNum(int musicId);

    @Select("SELECT music_info.id,`music_name`,`author`,`play_duration` from music_info LEFT JOIN music_type " +
            "ON music_info.id=music_type.music_id where tag_id=#{tagId}")
    List<MusicDTO> getHotMusicsByTagId(int tagId);

    @Insert("INSERT INTO user_play (user_id, music_id, play_num) " +
            "VALUES(#{userID}, #{musicID}, 1) " +
            "ON DUPLICATE KEY UPDATE play_num=play_num+1")
    void updatePlayingRecordElseInsert(@Param("userID") int userId, @Param("musicID") int musicId);

    @Insert("insert into music_info(music_name, author, play_duration, play_num) " +
            "values(#{musicName}, #{author}, #{duration}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "store.id")
    void addMusic(@Param("musicName") String name, @Param("author") String artist, @Param("duration") String duration, @Param("store")tempIDStore store);

    @Insert("insert into music_type(music_id, tag_id) values(#{musicID}, #{tagID})")
    void addMusicType(@Param("musicID") int musicId, @Param("tagID") int tagId);

    @Select("select uc.user_id,uc.music_id,IFNULL(up.play_num, 0) as play_num, 1 as collected\n" +
            "from user_collect uc\n" +
            "left join user_play up on uc.user_id=up.user_id and uc.music_id=up.music_id\n" +
            "UNION ALL\n" +
            "select up.user_id,up.music_id,up.play_num as play_num, !isNULL(uc.user_id) as collected\n" +
            "from user_collect uc\n" +
            "right join user_play up on uc.user_id=up.user_id and uc.music_id=up.music_id")
    @Results(value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "musicId", column = "music_id"),
            @Result(property = "playNum", column = "play_num"),
    })
    List<AccountRating> getAllAccountRating();
}
