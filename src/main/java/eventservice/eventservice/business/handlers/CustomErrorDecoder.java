package eventservice.eventservice.business.handlers;

import eventservice.eventservice.business.handlers.exceptions.CountryNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 404:
                return new CountryNotFoundException();
            default:
                return new Exception();
        }
    }
}
