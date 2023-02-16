package com.example.service.group.model.callback;

import com.example.service.group.model.resp.AddMemberResp;
import lombok.Data;

import java.util.List;

@Data
public class AddMemberAfterCallback {
    private String groupId;
    private Integer groupType;
    private String operater;
    private List<AddMemberResp> memberId;
}
