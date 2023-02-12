package com.example.service.user.model.resp;

import lombok.Data;

import java.util.List;

/**
 * @author yzhou
 * @date 2023/2/13
 */
@Data
public class ImportUserResp {
    private List<String> successId;
    private List<String> errorId;
}
