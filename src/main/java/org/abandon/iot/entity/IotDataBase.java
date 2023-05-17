package org.abandon.iot.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * IotDataBase
 *
 * @author abandon
 * @version 1.0
 * @since 2023/5/12 20:21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "数据库信息", description = "数据库信息")
public class IotDataBase implements Serializable {

    @Schema(example = "root.jk.iot", description = "数据库名称", type = "string")
    private String database;

    private Long ttl;

    private Long schemaReplicationFactor;

    private Long dataReplicationFactor;

    private Long timePartitionInterval;

    @Serial
    private static final long serialVersionUID = -8553862772672002714L;
}
