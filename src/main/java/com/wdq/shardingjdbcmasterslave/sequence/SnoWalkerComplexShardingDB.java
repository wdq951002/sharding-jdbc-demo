package com.wdq.shardingjdbcmasterslave.sequence;

import com.alibaba.fastjson.JSON;
import com.wdq.shardingjdbcmasterslave.constant.DbAndTableEnum;
import com.wdq.shardingjdbcmasterslave.constant.ShardingConstant;
import com.wdq.shardingjdbcmasterslave.utils.StringUtil;
import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @description: 自定义分库策略
 * @author: wdq-bg405275
 * @data: 2019-10-16 13:36
 **/
@Slf4j
public class SnoWalkerComplexShardingDB implements ComplexKeysShardingAlgorithm {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<ShardingValue> shardingValues) {
        //打印数据源集合以及分片键属性集合
        log.info("availableTargetNames:" + JSON.toJSONString(availableTargetNames) + ",shardingValues:" + JSON.toJSONString(shardingValues));
        List<String> shardingResults = new ArrayList<>();
        //遍历分片集合，匹配数据源
        for (ShardingValue value : shardingValues) {
            ListShardingValue<String> listShardingValue = (ListShardingValue<String>) value;
            List<String> shardingValue = (List<String>) listShardingValue.getValues();
            log.info("shardingValue:" + JSON.toJSONString(shardingResults));
            //获取数据库源索引值
            String index = getIndex(listShardingValue.getLogicTableName(), listShardingValue.getColumnName(), shardingValue.get(0));
            //循环匹配数据源，匹配到则退出循环
            for (String name : availableTargetNames) {
                //获取逻辑数据源索引后缀，即0、1、2、3、4
                String nameSuffix = name.substring(ShardingConstant.LOGIC_DB_PREFIX_LENGTH);
                //当且仅当availableTargetNames中的数据源索引与路由值对应的分片索引相同退出循环
                if (nameSuffix.equals(index)) {
                    shardingResults.add(name);
                    break;
                }
            }
            //匹配到一种路由规则就可以退出
            if (!shardingResults.isEmpty()) {
                break;
            }
        }

        return shardingResults;
    }

    /**
     * 根据分片键计算分片节点
     *
     * @param logicTableName
     * @param columnName
     * @param shardingValue
     * @return
     */
    private String getIndex(String logicTableName, String columnName, String shardingValue) {
        String index = "";
        if (StringUtils.isBlank(shardingValue)) {
            throw new IllegalArgumentException("分片键值为空");
        }
        //截取分片键值-下标循环主键规则枚举类，匹配主键列名得到规则
        for (DbAndTableEnum targetEnum : DbAndTableEnum.values()) {

            /*
              目标表路由
              如果逻辑表命中，判断路由键是否与列名相同
             */
            if (targetEnum.getTableName().equals(logicTableName)) {
                //目标表的目标主键路由-例如：根据订单id查询订单信息
                if (targetEnum.getShardingKey().equals(columnName)) {
                    index = getDbIndexBySubString(targetEnum, shardingValue);
                } else {
                    //目标表的非目标主键路由-例如：根据内部用户id查询订单信息- -固定取按照用户表库表数量
                    //兼容且仅限根据外部id路由 查询用户信息
                    index = getDbIndexByMod(targetEnum, shardingValue);
                }
                break;
            }
        }
        if (StringUtils.isBlank(index)) {
            String msg = "从分片键值中解析数据库索引异常：logicTableName=" + logicTableName + "|columnName=" + columnName + "|shardingValue=" + shardingValue;
            throw new IllegalArgumentException(msg);
        }
        return index;
    }

    /**
     * 该表主键使用下标方式截取数据库索引
     *
     * @param targetEnum
     * @param shardingValue
     * @return
     */
    private String getDbIndexBySubString(DbAndTableEnum targetEnum, String shardingValue) {
        int indexBegin = targetEnum.getDbIndexBegin();
        int indexEnd = targetEnum.getDbIndexBegin() + ShardingConstant.DB_SUFFIX_LENGTH;
        return StringUtil.deleteZero(shardingValue.substring(indexBegin, indexEnd));
    }

    /**
     * 内部用户id使用取模方式对目标表库表数量取模获取分片节点
     *
     * @param shardingValue
     * @return
     */
    private String getDbIndexByMod(DbAndTableEnum targetEnum, String shardingValue) {
        return String.valueOf(StringUtil.getDbIndexByMod(shardingValue, targetEnum.getDbCount(), targetEnum.getTbCount()));
    }
}
