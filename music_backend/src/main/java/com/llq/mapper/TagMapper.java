package com.llq.mapper;

import com.llq.dto.TagDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
}
