package com.yzhou.im.service.message.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzhou.im.service.message.dao.ImMessageHistoryEntity;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ImMessageHistoryMapper extends BaseMapper<ImMessageHistoryEntity> {

    /**
     * 批量插入（mysql）
     * @param entityList
     * @return
     */
    Integer insertBatchSomeColumn(Collection<ImMessageHistoryEntity> entityList);
}
