package com.pranay.easybuy.products.responseBuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class APIResponse {
	private Instant timeStamp;
	private int statusCode;
	private String message;
	private String path;
	private Boolean status;
	private String error;
	private List<String> fieldErrors;

	public APIResponse(Instant timeStamp, String message, int statusCode, String path, Boolean status, String error,
			List<String> fieldErrors) {
		this.timeStamp = timeStamp;
		this.message = message;
		this.statusCode = statusCode;
		this.path = path;
		this.status = status;
		this.error = error;
		this.fieldErrors = fieldErrors;
	}

}
