package com.wdq.shardingjdbcmasterslave.sequence;

import com.wdq.shardingjdbcmasterslave.constant.DbAndTableEnum;
import com.wdq.shardingjdbcmasterslave.constant.ShardingConstant;
import com.wdq.shardingjdbcmasterslave.utils.DateUtil;
import com.wdq.shardingjdbcmasterslave.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 对业务开放的分布式主键生成器
 * @author: wdq-bg405275
 * @data: 2019-10-16 10:40
 **/
@Component
@RequiredArgsConstructor
public class KeyGenerator {
    final RedisSequenceGenerator sequenceGenerator;

    /**
     * 根据路由id生成内部系统主键id
     * 路由id可以是内部其他系统主键id，也可以是外部第三方用户id
     *
     * @param targetEnum     待生成主键的目标表规则配置
     * @param relatedRouteId 路由id或外部第三方用户id
     * @return
     */
    public String generateKey(DbAndTableEnum targetEnum, String relatedRouteId) {
        if (StringUtils.isBlank(relatedRouteId)) {
            throw new IllegalArgumentException("路由id为空");
        }
        StringBuilder key = new StringBuilder();
        //1. id业务前缀
        String idPrefix = targetEnum.getCharsPrefix();
        //2.id数据库索引位
        String dbIndex = getDbIndexAndTbIndexMap(targetEnum, relatedRouteId).get("dbIndex");
        //3.id表索引位
        String tbIndex = getDbIndexAndTbIndexMap(targetEnum, relatedRouteId).get("tbIndex");
        //4.id规则版本位
        String idVersion = targetEnum.getIdVersion();
        //5.时间戳位
        String timeString = DateUtil.formatDate(new Date());
        /** 6.id分布式机器位 2位*/
        String distributedIndex = getDistributedId(2, 1);
        /** 7.随机数位*/
        String sequenceId = sequenceGenerator.getNextVal(targetEnum, Integer.parseInt(dbIndex), Integer.parseInt(tbIndex));
        /** 库表索引靠前*/
        return key.append(idPrefix)
                .append(dbIndex)
                .append(tbIndex)
                .append(idVersion)
                .append(timeString)
                .append(distributedIndex)
                .append(sequenceId).toString();
    }

    private Map<String, String> getDbIndexAndTbIndexMap(DbAndTableEnum targetEnum, String relatedRouteId) {
        Map<String, String> map = new HashMap<>();
        //获取库索引
        String preDbIndex = String.valueOf(
                StringUtil.getDbIndexByMod(relatedRouteId, targetEnum.getDbCount(), targetEnum.getTbCount())
        );
        //获取表索引
        String preTbIndex = String.valueOf(
                StringUtil.getTbIndexByMod(relatedRouteId, targetEnum.getDbCount(), targetEnum.getTbCount())
        );
        String dbIndex = StringUtil.fillZero(preDbIndex, ShardingConstant.DB_SUFFIX_LENGTH);
        String tbIndex = StringUtil.fillZero(preTbIndex, ShardingConstant.TABLE_SUFFIX_LENGTH);
        map.put("dbIndex", dbIndex);
        map.put("tbIndex", tbIndex);

        return map;
    }

    /**
     * 生成id分布式机器位
     * @return 分布式机器id
     * length与hostCount位数相同
     */
    private String getDistributedId(int length, int hostCount) {
        return StringUtil
                .fillZero(String.valueOf(getIdFromHostName() % hostCount), length);
    }


    /**
     * 适配分布式环境，根据主机名生成id
     * 分布式环境下，如：Kubernates云环境下，集群内docker容器名是唯一的
     * 通过 @See org.apache.commons.lang3.SystemUtils.getHostName()获取主机名
     * @return
     */
    private Long getIdFromHostName(){
        //unicode code point
        int[] ints = StringUtils.toCodePoints(SystemUtils.getHostName());
        int sums = 0;
        for (int i: ints) {
            sums += i;
        }
        return (long)(sums);
    }
}
