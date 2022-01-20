package com.cookandroid.registeration.helper.network;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReserveRequest extends StringRequest {//회원가입 요청을 보내는 클래스
    final static private String URL="https://jaohoo.cafe24.com/ReserveAdd.php";
    private Map<String, String> parameters;

    public ReserveRequest(String uid, String building, String room_number, String time, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters =new HashMap<>();
        parameters.put("uid", uid);
        parameters.put("building", building);
        parameters.put("room_number",room_number);
        parameters.put("time",time);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
