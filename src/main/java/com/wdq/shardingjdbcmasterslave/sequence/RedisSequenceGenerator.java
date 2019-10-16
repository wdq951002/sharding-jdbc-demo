package com.wdq.shardingjdbcmasterslave.sequence;

import com.wdq.shardingjdbcmasterslave.constant.DbAndTableEnum;
import com.wdq.shardingjdbcmasterslave.constant.ShardingConstant;
import com.wdq.shardingjdbcmasterslave.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @description: redis实现序列生成器
 * @author: wdq-bg405275
 * @data: 2019-10-15 13:54
 **/
@Component(value = "redisSequenceGenerator")
@RequiredArgsConstructor
public class RedisSequenceGenerator implements SequenceGenerator {
    /**序列生成器前缀**/
    public static String LOGIC_TABLE_NAME = "sequence:redis:";
    /**序列长度为5,不足5的用0补齐**/
    public static int SEQUENCE_LENGTH = 5;
    /**序列最大值**/
    public static int SEQUENCE_MAX = 9000;

    final StringRedisTemplate stringRedisTemplate;


    /**
     * redis序列获取实现方法
     * @param targetEnum
     * @param dbIndex
     * @param tbIndex
     * @return
     */
    @Override
    public String getNextVal(DbAndTableEnum targetEnum, int dbIndex, int tbIndex) {
        String redisKeySuffix = new StringBuilder(targetEnum.getTableName())
                .append("_")
                .append("dbIndex")
                .append(StringUtil.fillZero(String.valueOf(dbIndex), ShardingConstant.DB_SUFFIX_LENGTH))
                .append("_tbIndex")
                .append(StringUtil.fillZero(String.valueOf(tbIndex), ShardingConstant.TABLE_SUFFIX_LENGTH))
                .append("_")
                .append(targetEnum.getShardingKey()).toString();

        String increaseKey = new StringBuilder(LOGIC_TABLE_NAME).append(redisKeySuffix).toString();
        long sequenceId = stringRedisTemplate.opsForValue().increment(increaseKey);
        //达到指定值重置序列号，预留后10000个id以便并发时缓冲
        if (sequenceId == SEQUENCE_MAX) {
            stringRedisTemplate.delete(increaseKey);
        }
        // 返回序列值，位数不够前补零
        return StringUtil.fillZero(String.valueOf(sequenceId), SEQUENCE_LENGTH);
    }
}
