package com.heachi.auth.api.controller.hello;

import com.heachi.auth.api.service.hello.HelloQueryRepository;
import com.heachi.mysql.define.hello.Hello;
import com.heachi.mysql.define.hello.repository.HelloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {


    private final HelloRepository helloRepository;
    private final HelloQueryRepository helloQueryRepository;

    @GetMapping("/auth/")
    public Hello hello() {
        Hello hi = Hello.builder()
                .hello("hi")
                .build();
        Hello save = helloRepository.save(hi);

        Hello hello = helloQueryRepository.find();

        System.out.println(hello.getHello());

        return save;
    }
}

