package org.example.Server.Service.Impl;

import org.example.Server.Service.LoginService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginServiceImpl implements LoginService {

    private Map<Integer, String> allUserMap = new ConcurrentHashMap<>();

    {
        allUserMap.put(1, "1");
        allUserMap.put(2, "2");
        allUserMap.put(11111, "123");
        allUserMap.put(11233, "123");
    }

    @Override
    public boolean login(Integer userId, String password) {
        String pass = allUserMap.get(userId);
        if (pass == null) {
            return false;
        }
        return pass.equals(password);
    }
}
