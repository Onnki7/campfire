<<<<<<< HEAD
package com.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.Tag;
=======
package com.campusconnect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campusconnect.entity.Tag;
>>>>>>> ded559b9d55232478b633820b828376459ad79a4
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    List<Tag> selectTagsByUserId(@Param("userId") Long userId);
    List<Tag> selectTagsByPostId(@Param("postId") Long postId);
<<<<<<< HEAD
}
=======
}
>>>>>>> ded559b9d55232478b633820b828376459ad79a4
