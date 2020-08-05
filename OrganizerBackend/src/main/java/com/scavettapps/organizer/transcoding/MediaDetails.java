/**
 * Copyright 2020 - Vincent Scavetta - All Rights Reserved
 */
package com.scavettapps.organizer.transcoding;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Vincent Scavetta.
 */
@Data
@Builder
public class MediaDetails {
   private String format;
   private String mimetype;
   private double duration;
}
