package com.ddmh.controller.api;

import com.ddmh.condition.TableQueryCondition;
import com.ddmh.dto.SelectItemDto;
import com.ddmh.service.TableLoadService;
import com.ddmh.utils.JsonUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Fbin
 * @version 2019/4/11 7:39
 */
@RestController
@RequestMapping("/select")
public class TableSelectController {

    @Autowired
    private TableLoadService tableLoadService;

    @GetMapping("/table")
    public Object loadTableData(TableQueryCondition tableQueryCondition){
        enrichTableQueryCondition(tableQueryCondition);

        List<SelectItemDto> selectItemDtoList = Lists.newArrayList();
        List<String> tableNameList = tableLoadService.loadTableListBy(tableQueryCondition);
        if(!CollectionUtils.isEmpty(tableNameList)){
            getTableList(tableNameList, selectItemDtoList);
        }
        return JsonUtils.success(selectItemDtoList);
    }

    private void enrichTableQueryCondition(TableQueryCondition tableQueryCondition) {
        tableQueryCondition.setCurrent(1);
        tableQueryCondition.setPageSize(20);
        tableQueryCondition.setSortName("table_name");
        tableQueryCondition.setOrder("asc");
    }

    private void getTableList(List<String> tableNameList,
                              List<SelectItemDto> selectItemDtoList) {

        for(String tableName : tableNameList){
            SelectItemDto selectItemDto =
                    SelectItemDto.builder()
                            .label(tableName)
                            .value(tableName)
                            .build();
            selectItemDtoList.add(selectItemDto);
        }
    }

}
