package com.example.service.user.service.impl;

import com.example.service.user.dao.mapper.ImUserDataMapper;
import com.example.service.user.model.req.ImportUserReq;
import com.example.service.user.model.resp.ImportUserResp;
import com.example.service.user.service.ImUserService;
import com.yzhou.im.common.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yzhou
 * @date 2023/2/12
 */
@Service
public class ImUserServiceImpl implements ImUserService {

    @Autowired
    ImUserDataMapper imUserDataMapper;

    @Override
    public ResponseVO importUser(ImportUserReq req) {

        if (req.getUserData().size() > 100) {
            // TODO 返回数量过多
        }
        List<String> successId = new ArrayList<>();
        List<String> errorId = new ArrayList<>();
        req.getUserData().forEach(e -> {
            try {
                e.setAppId(req.getAppId());
                int insert = imUserDataMapper.insert(e);
                if (insert == 1) {
                    successId.add(e.getUserId());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                errorId.add(e.getUserId());
            }
        });
        ImportUserResp resp = new ImportUserResp();
        resp.setSuccessId(successId);
        resp.setErrorId(errorId);
        return ResponseVO.successResponse(resp);
    }
}
