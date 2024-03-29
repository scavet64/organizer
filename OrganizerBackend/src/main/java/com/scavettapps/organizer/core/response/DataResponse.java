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

/**
 * @author Vincent Scavetta
 */
public class DataResponse extends Response {

    private Object data;

    /**
     * @param data
     */
    public DataResponse(Object data) {
	super();
	this.data = data;
    }

    /**
     * @return the data
     */
    public Object getData() {
	return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Object data) {
	this.data = data;
    }

}
