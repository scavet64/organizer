package com.scavettapps.organizer.management.backup;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.scavettapps.organizer.tag.Tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackupMediaTags {
   private List<Tag> tags;
   private Map<String, Set<Tag>> hashesToTags;
}
