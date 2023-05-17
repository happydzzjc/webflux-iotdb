package org.abandon.iot.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * IotRecord
 *
 * @author abandon
 * @version 1.0
 * @since 2023/5/12 15:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IotRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 8981687777862317484L;

    private String deviceId;
    private long time;
    private List<String> measurements;
    private List<String> values;
}
