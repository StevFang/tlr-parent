package com.ddmh.controller.api;

import com.ddmh.common.Pagination;
import com.ddmh.condition.ColumnAuditRecordCondition;
import com.ddmh.controller.AbstractApiController;
import com.ddmh.service.ColumnAuditRecordLoadService;
import com.ddmh.utils.JsonUtils;
import com.ddmh.vo.ColumnAuditRecordVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 列变更记录
 *
 * @author FBin
 * @version 2019/4/10 9:25
 */
@Slf4j
@RestController
@RequestMapping("/column/audit/record")
public class ColumnAuditRecordLoadController extends AbstractApiController {

    @Autowired
    private ColumnAuditRecordLoadService columnAuditRecordLoadService;

    @GetMapping("/list")
    public Object loadColumnAuditRecordList(ColumnAuditRecordCondition columnAuditRecordCondition){
        String checkResult = checkParams(columnAuditRecordCondition);
        if(StringUtils.isNotBlank(checkResult)){
            return JsonUtils.error(checkResult);
        }
        Pagination<ColumnAuditRecordVo> pageData = columnAuditRecordLoadService.loadPageData(columnAuditRecordCondition);
        return JsonUtils.success(pageData);
    }

    private String checkParams(ColumnAuditRecordCondition columnAuditRecordCondition) {
        String columnId = columnAuditRecordCondition.getColumnId();
        if(StringUtils.isBlank(columnId)){
            return "columnId字段的值不能为空！";
        }

        return super.checkPageParams(columnAuditRecordCondition);
    }
}
