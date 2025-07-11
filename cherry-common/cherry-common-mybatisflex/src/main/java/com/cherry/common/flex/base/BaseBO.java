package com.cherry.common.flex.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * BaseBO
 *
 * @author keer2345
 * @date 2025-07-01
 */
@Data
public class BaseBO {

    private String id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String creator;
    private String createDept;
    private String updater;
    private String deleteId;
    private LocalDateTime deleteTime;
    /**
     * false: normal, true: deleted
     */
    private Boolean deleted;

    private String tenantId;
    private String searchValue;
    private Map<String, Object> params = new HashMap<>();
}
