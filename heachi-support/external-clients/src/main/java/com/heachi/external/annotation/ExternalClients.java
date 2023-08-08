package com.heachi.external.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @HttpExchange 를 사용하는 인터페이스에 달아주는 어노테이션이다.
 * 사용자가 직접 ProxyFactory 로 빈을 등록해줘야하는 번거로움을 해소해준다.
 * baseUrl에는 application.yml 의 환경변수나, 자신이 하고싶은 String 타입의 주소를 넣어줄 수 있다.
 *
 * ExternalClientsPostProcessor 클래스에서는 먼저 application.yml 환경변수를
 * 가져오는 요청을 하고, 만약 존재하지 않는다면, baseUrl String 값을 그대로 baseUrl로 설정한다.
 *
 * ex)
 *      @ExternalClients(baseUrl = "server.url")
 *      public interface KakaoExternalClients {
 *
 *          @GetExchange("/auth")
 *          public getAuthorization(@RequestParam String token);
 *      }
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalClients {

    String baseUrl() default "http://localhost:8080";

}
