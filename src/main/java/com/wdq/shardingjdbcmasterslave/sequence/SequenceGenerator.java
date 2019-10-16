package com.wdq.shardingjdbcmasterslave.sequence;

import com.wdq.shardingjdbcmasterslave.constant.DbAndTableEnum;

public interface SequenceGenerator {
    /**
     * @param targetEnum
     * @param dbIndex
     * @param tbIndex
     * @return
     */
    String getNextVal(DbAndTableEnum targetEnum, int dbIndex, int tbIndex);
}
