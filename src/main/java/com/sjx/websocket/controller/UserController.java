package com.sjx.websocket.controller;

import com.sjx.websocket.entity.enums.GlobalConstants;
import com.sjx.websocket.entity.http.Response;
import com.sjx.websocket.entity.module.User;
import com.sjx.websocket.entity.vo.LoginVO;
import com.sjx.websocket.exception.GlobalException;
import com.sjx.websocket.exception.UserException;
import com.sjx.websocket.service.UserService;
import com.sjx.websocket.util.ip2region.IpAddressUtil;
import com.sjx.websocket.util.redis.RedisUtil;
import com.sjx.websocket.util.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @Resource
    private WebSocket webSocket;

    @Resource
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/getVerifyCode")
    public ResponseEntity<Response<String>> getVerifyCode() {
        try {
            log.info("获取验证码");
            return ResponseEntity.ok(Response.success(redisUtil.get(IpAddressUtil.getMacAddress()).toString()));
        } catch (GlobalException e) {
            log.error("获取验证码出错", e);
            return ResponseEntity.ok(Response.error(GlobalConstants.USER_GET_VERIFY_CODE.getCode(), GlobalConstants.USER_GET_VERIFY_CODE.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response<User>> login(@RequestBody LoginVO loginVO) {
        try {
            log.info("用户登录");
            return ResponseEntity.ok(Response.success("登陆成功", userService.login(loginVO)));
        } catch (UserException e) {
            log.error("用户登录数据出错", e);
            return ResponseEntity.ok(Response.error(e.getCode(), e.getMessage()));
        } catch (Exception e) {
            log.error("用户登录接口出错", e);
            return ResponseEntity.ok(Response.error());
        }
    }

    @GetMapping("/getAllUser")
    public Response<List<User>> getAllUser(@RequestParam(value = "userId")Long userId) {
        List<User> allUser = webSocket.getAllUser(userId);
        return new Response<>(200, "成功", allUser);
    }

    @GetMapping("/sendMessageToUser")
    public Response<String> sendMessageToUser(@RequestParam(value = "sendNickname")String sendNickname,
                                              @RequestParam(value = "nickname")String nickname,
                                              @RequestParam(value = "message")String message) {
        try {
            webSocket.onMessageToUser(sendNickname, nickname, message);
            return new Response<>(200, "成功", null);
        } catch (IOException e) {
            log.error("发送失败", e);
            return new Response<>(5001, "失败", null);
        } catch (Exception e) {
            log.error("服务错误", e);
            return new Response<>(5000, "失败", null);
        }
    }
}
