package org.example.Util.Factory;

import lombok.Getter;
import org.example.Server.Service.Impl.LoginServiceImpl;
import org.example.Server.Service.LoginService;

public abstract class LoginServiceFactory {

    @Getter
    private static LoginService userService = new LoginServiceImpl();

}
