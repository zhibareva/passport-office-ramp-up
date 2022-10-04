package com.passportoffice;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationEventHandler implements ApplicationListener<ContextRefreshedEvent> {
  @Autowired PassportApiControllerTest passportApiControllerTest;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    RestAssured.baseURI = "http://localhost/v1/";
    RestAssured.port = passportApiControllerTest.port;
  }
}
