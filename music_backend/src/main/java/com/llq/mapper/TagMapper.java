package com.llq.mapper;

import com.llq.dto.TagDTO;
import com.llq.entity.MusicTagEntry;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface TagMapper {

    @Select("select `id`,`tag_name` from tag_info where tag_category=#{category} order by createtime")
    List<TagDTO> getTagsByCategory(int category);

    @Select("select `tag_id` from user_likedtag where user_id=#{userId}")
    List<Integer> getLikedTagIDList(int userId);

    @Delete("delete from user_likedtag where user_id=#{userId}")
    void deleteAllLikedTagOfUser(int userId);

    @Insert("insert into user_likedtag(user_id, tag_id) values(#{userID}, #{tagID})")
    void insertLikedTagOfUser(@Param("userID") int userId, @Param("tagID") int tagId);

    @Select("select `music_id`, GROUP_CONCAT(`tag_id`) tag_ids from music_type group by music_id")
    @Results({
            @Result(property = "musicId", column = "music_id"),
            @Result(property = "tagIds", column = "tag_ids")
    })
    List<MusicTagEntry> getMusicTagIDMap();
}
