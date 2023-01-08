package com.likelion.mutsasns.controller.api;

import com.likelion.mutsasns.domain.dto.request.user.UserChangeRoleRequest;
import com.likelion.mutsasns.domain.dto.request.user.UserJoinRequest;
import com.likelion.mutsasns.domain.dto.request.user.UserLoginRequest;
import com.likelion.mutsasns.domain.dto.response.Response;
import com.likelion.mutsasns.domain.dto.response.user.UserChangeRoleResponse;
import com.likelion.mutsasns.domain.dto.response.user.UserJoinResponse;
import com.likelion.mutsasns.domain.dto.response.user.UserLoginResponse;
import com.likelion.mutsasns.domain.entity.User;
import com.likelion.mutsasns.enumerate.UserRole;
import com.likelion.mutsasns.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @ApiOperation(value = "회원가입", notes = "사용자가 입력한 이름과 비밀번호를 통해 회원가입을 한다.")
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request){
        User savedUser = userService.join(request.getUserName(), request.getPassword());
        return Response.success(new UserJoinResponse(savedUser.getId(), savedUser.getUserName()));
    }

    @ApiOperation(value = "로그인", notes = "사용자가 입력한 이름과 비밀번호로 로그인을 한다.")
    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        String token = userService.login(request.getUserName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @ApiOperation(value = "등급 변경 기능", notes = "관리자의 경우, 모든 회원의 권한을 변경할 수 있다.")
    @PostMapping("/{id}/role/change")
    public Response<UserChangeRoleResponse> changeRole(@PathVariable Integer id, @RequestBody UserChangeRoleRequest request){
        UserRole modifiedUserRole = userService.changeRole(id, request);
        return Response.success(new UserChangeRoleResponse("변경 되었습니다.", modifiedUserRole));
    }
}
