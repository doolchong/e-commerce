package com.example.userservice.util;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodName, Response response) {
        switch (response.status()) {
            case 400:
                break;
            case 404:
                if (methodName.contains("getOrderList")) {
                    return new ResponseStatusException(HttpStatus.valueOf(
                            response.status()),
                            "User's order is empty"
                    );
                }
                break;
            default:
                return new Exception(response.reason());
        }

        return null;
    }
}
