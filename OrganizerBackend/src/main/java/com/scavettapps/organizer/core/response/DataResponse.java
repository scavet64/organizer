package com.scavettapps.organizer.core.response;

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
