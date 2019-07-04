package com.scavettapps.organizer.controller.response;

public class ErrorResponse extends Response {

    private Object error;

    /**
     * @param error
     */
    public ErrorResponse(Object error) {
	super();
	this.error = error;
    }

    /**
     * @return the error
     */
    public Object getError() {
	return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(Object error) {
	this.error = error;
    }

}
