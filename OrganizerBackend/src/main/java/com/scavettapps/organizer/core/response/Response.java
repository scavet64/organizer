/**
 * Copyright 2019 Vincent Scavetta
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scavettapps.organizer.core.response;

import static com.scavettapps.organizer.core.OrganizerConstants.VERSION;

/**
 * @author Vincent Scavetta
 */
public abstract class Response {

    private String version = VERSION;

    /**
     * @return the version
     */
    public String getVersion() {
	return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
	this.version = version;
    }
}
