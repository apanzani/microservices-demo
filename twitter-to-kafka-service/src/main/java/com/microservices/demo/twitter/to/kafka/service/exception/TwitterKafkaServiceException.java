package com.microservices.demo.twitter.to.kafka.service.exception;

public class TwitterKafkaServiceException extends RuntimeException {

    public TwitterKafkaServiceException(){
        super();
    }

    public TwitterKafkaServiceException(String message) {
        super(message);
    }

    protected TwitterKafkaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
