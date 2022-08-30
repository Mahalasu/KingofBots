package com.gsdai.backend.service.impl.gameHistory;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gsdai.backend.domain.Record;
import com.gsdai.backend.domain.User;
import com.gsdai.backend.mapper.RecordMapper;
import com.gsdai.backend.mapper.UserMapper;
import com.gsdai.backend.service.gameHistory.GetHistoryListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetHistoryListServiceImpl implements GetHistoryListService {

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<Record> recordIPage = new Page<>(page, 10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Record> records = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new LinkedList<>();
        for (Record record : records) {
            User userA = userMapper.selectById(record.getaId());
            User userB = userMapper.selectById(record.getbId());
            JSONObject item = new JSONObject();
            item.put("aAvatar", userA.getAvatar());
            item.put("aUsername", userA.getUsername());
            item.put("bAvatar", userB.getAvatar());
            item.put("bUsername", userB.getUsername());
            item.put("gameHistory", record);
            items.add(item);
        }
        resp.put("allHistory", items);
        resp.put("pageCount", recordMapper.selectCount(null));
        return resp;
    }
}
