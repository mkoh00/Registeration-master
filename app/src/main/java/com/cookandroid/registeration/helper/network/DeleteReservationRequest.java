package com.cookandroid.registeration.helper.network;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteReservationRequest extends StringRequest {// 예약 삭제
    final static private String URL="https://jaohoo.cafe24.com/DeleteReservation.php";
    private Map<String, String> parameters;

    public DeleteReservationRequest(String uid, String building, String room_number, String time, Response.Listener<String> listener){
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
