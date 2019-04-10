package com.ddmh.mapper;

import com.ddmh.condition.ColumnCondition;
import com.ddmh.dto.ColumnDto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table load mapper
 *
 * @author Fbin
 * @version 2019/4/9 22:47
 */
@Repository
public interface ColumnMapper {

    /**
     * 获取 column 总记录数
     *
     * @param columnCondition
     * @return
     */
    int loadColumnCountBy(ColumnCondition columnCondition);

    /**
     * 获取 column 分页数据
     *
     * @param columnCondition
     * @return
     */
    List<ColumnDto> loadPageColumnListBy(ColumnCondition columnCondition);

    /**
     * 根据唯一标识删除记录
     *
     * @param id
     */
    void deleteById(String id);
}
