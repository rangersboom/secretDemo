package com.sunny.demo.api;

import com.sunny.demo.entity.User;
import com.sunny.demo.filter.requestBodyParam.TestParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author:Sunny
 * @Description:
 * @CreateDate: 21:54 2018/10/26
 * @Modified:
 * @Version:
 */
@Slf4j
@RestController
@RequestMapping({"/api","/body/*","/param/*"})
public class TestApi {

    @RequestMapping("/testParam")
    public List test(HttpServletRequest request){
        String id = request.getParameter("id");
        log.info("id===>"+id);
        String name = request.getParameter("name");
        log.info("name===>"+name);

        List list = new ArrayList();
        User user1 = new User(1,"张三",13,96.3,new Date(),true);
        User user2 = new User(2,"李四",14,96.4,new Date(),false);
        User user3 = new User(3,"王五",15,96.5,new Date(),true);
        list.add(user1);
        list.add(user2);
        list.add(user3);
        return list;
    }

    @RequestMapping("/testBody")
    public List testBody(@RequestBody TestParam testParam){
        log.info(testParam.toString());
        List list = new ArrayList();
        User user1 = new User(1,"张三",13,96.3,new Date(),true);
        User user2 = new User(2,"李四",14,96.4,new Date(),false);
        User user3 = new User(3,"王五",15,96.5,new Date(),true);
        list.add(user1);
        list.add(user2);
        list.add(user3);
        return list;
    }
}
