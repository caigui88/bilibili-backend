package com.bilibili.common.mapper.video.audience_reactions;
import com.bilibili.common.domain.entity.user.IdCount;
import com.bilibili.common.domain.entity.video.audience_reactions.Comment;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface CommentMapper extends MPJBaseMapper<Comment> {
    @Select({
            "<script>",
            "SELECT comment_id AS id, COUNT(*) AS count",
            "FROM likes",
            "WHERE comment_id IN",
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "GROUP BY comment_id",
            "</script>"
    })
    List<IdCount> getCommentCount(Set<Integer> ids);
}
