package com.example.service.group.model.resp;

import com.example.service.group.dao.ImGroupEntity;
import lombok.Data;

import java.util.List;

@Data
public class GetJoinedGroupResp {

    private Integer totalCount;

    private List<ImGroupEntity> groupList;

}
