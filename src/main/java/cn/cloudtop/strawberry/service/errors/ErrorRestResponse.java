package cn.cloudtop.strawberry.service.errors;

import cn.cloudtop.strawberry.rest.RestResponse;

/**
 * Created by jackie on 16-6-4
 */
public class ErrorRestResponse extends RestResponse {

    public ErrorRestResponse(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
