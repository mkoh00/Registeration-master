package com.cookandroid.registeration.helper.network;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteUserRequest extends StringRequest {//탈퇴 요청을 보내는 클래스
    final static private String URL="https://jaohoo.cafe24.com/DeleteUser.php";
    private Map<String, String> parameters;

    public DeleteUserRequest(String userID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters =new HashMap<>();
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
