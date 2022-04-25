/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hippo4j.config.service.biz.impl;

import cn.hippo4j.config.mapper.LogRecordMapper;
import cn.hippo4j.config.model.biz.log.LogRecordQueryReqDTO;
import cn.hippo4j.config.model.biz.log.LogRecordRespDTO;
import cn.hippo4j.config.service.biz.LogRecordBizService;
import cn.hippo4j.config.toolkit.BeanUtil;
import cn.hippo4j.tools.logrecord.model.LogRecordInfo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 操作日志.
 *
 * @author chen.ma
 * @date 2021/11/17 21:50
 */
@Service
@AllArgsConstructor
public class LogRecordBizServiceImpl implements LogRecordBizService {

    private final LogRecordMapper logRecordMapper;

    @Override
    public IPage<LogRecordRespDTO> queryPage(LogRecordQueryReqDTO pageQuery) {
        LambdaQueryWrapper<LogRecordInfo> queryWrapper = Wrappers.lambdaQuery(LogRecordInfo.class)
                .eq(StrUtil.isNotBlank(pageQuery.getBizNo()), LogRecordInfo::getBizNo, pageQuery.getBizNo())
                .eq(StrUtil.isNotBlank(pageQuery.getCategory()), LogRecordInfo::getCategory, pageQuery.getCategory())
                .eq(StrUtil.isNotBlank(pageQuery.getOperator()), LogRecordInfo::getOperator, pageQuery.getOperator())
                .orderByDesc(LogRecordInfo::getCreateTime);
        IPage<LogRecordInfo> selectPage = logRecordMapper.selectPage(pageQuery, queryWrapper);
        return selectPage.convert(each -> BeanUtil.convert(each, LogRecordRespDTO.class));
    }

}
