package ark.controller;

import ark.service.UserCF;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 推荐算法 Controller 类
 */
@Controller
@RequestMapping("rec")
public class RecommendController {

    @RequestMapping("userCF")
    @ResponseBody
    public String UserCF(){

        UserCF userCf = new UserCF();
        return userCf.getUserRecommed("2").toString();

    }



}
