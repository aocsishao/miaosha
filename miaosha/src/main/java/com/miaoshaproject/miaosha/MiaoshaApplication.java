package com.miaoshaproject.miaosha;

import com.miaoshaproject.miaosha.dao.UserDOMapper;
import com.miaoshaproject.miaosha.dataobject.UserDO;
import org.apache.catalina.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.miaoshaproject.miaosha"})
@MapperScan("com.miaoshaproject.miaosha.dao")
@RestController
public class MiaoshaApplication {
	@Autowired
	private UserDOMapper userDOMapper;


	@RequestMapping("/")
	public String home(){
		UserDO userDO1=userDOMapper.selectByPrimaryKey(1);
		if(userDO1==null){
			return "用户对象不存在";
		}
		else return userDO1.getName();
	}
	public static void main(String[] args) {
		SpringApplication.run(MiaoshaApplication.class, args);
	}

}
