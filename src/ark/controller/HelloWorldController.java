package ark.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {

    @RequestMapping("hello")
    @ResponseBody
    public String helloWorld(){
        return "hello world";
    }


    @RequestMapping("welcome")
    public String welcome(){
        // 自动定位到 WEB-CONTENT/pages/front/index.html 下面
        return "front/index.html";
    }


}
