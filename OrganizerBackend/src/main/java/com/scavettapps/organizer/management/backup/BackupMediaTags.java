package com.scavettapps.organizer.management.backup;

import com.scavettapps.organizer.media.MediaFile;
import com.scavettapps.organizer.media.json.MediaTags;
import com.scavettapps.organizer.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackupMediaTags {
   private List<Tag> tags;
   private Map<String, Set<Tag>> hashesToTags;
}
