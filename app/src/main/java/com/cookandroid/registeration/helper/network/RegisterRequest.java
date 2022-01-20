package com.cookandroid.registeration.helper.network;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {//회원가입 요청을 보내는 클래스

    final static private String URL="https://jaohoo.cafe24.com/UserRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String Password, String userName,String userMajor, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("userID",userID);
        parameters.put("Password",Password);
        parameters.put("userName",userName);
        parameters.put("userMajor",userMajor);



    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
