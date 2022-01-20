package com.cookandroid.registeration.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cookandroid.registeration.R;
import com.cookandroid.registeration.User;
import com.cookandroid.registeration.data.EmptyRoomItem;
import com.cookandroid.registeration.helper.network.DeleteReservationRequest;
import com.cookandroid.registeration.helper.network.ReserveRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EmptyRoomAdapter extends RecyclerView.Adapter<EmptyRoomAdapter.EmptyRoomViewHolder> {
    private ArrayList<EmptyRoomItem> list = new ArrayList<>();

    @NonNull
    @Override
    public EmptyRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_room, parent, false);
        return new EmptyRoomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmptyRoomViewHolder holder, final int position) {
        final EmptyRoomItem item = list.get(position);
        final Context context = holder.itemView.getContext();
        holder.tvName.setText(item.getBuilding() + " " + item.getRoom_number() + "호실");
        String mTime = "";

        switch (item.getTime()) {
            case "1":
                mTime = "09:00 ~ 09:50";
                break;
            case "2":
                mTime = "10:00 ~ 10:50";
                break;
            case "3":
                mTime = "11:00 ~ 11:50";
                break;
            case "4":
                mTime = "12:00 ~ 12:50";
                break;
            case "5":
                mTime = "13:00 ~ 13:50";
                break;
            case "6":
                mTime = "14:00 ~ 14:50";
                break;
            case "7":
                mTime = "15:00 ~ 15:50";
                break;
            case "8":
                mTime = "16:00 ~ 16:50";
                break;
            case "9":
                mTime = "17:00 ~ 17:50";
                break;
            case "10":
                mTime = "18:00 ~ 18:50";
                break;
            case "11":
                mTime = "19:00 ~ 19:50";
                break;
            case "12":
                mTime = "20:00 ~ 20:50";
                break;
            case "13":
                mTime = "21:00 ~ 21:50";
                break;
        }
        holder.tvTime.setText(mTime);
        final String finalMTime = mTime;

        final boolean isAvailable = item.getAvailable();

        if (isAvailable) {
            holder.btnReserve.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            if (item.getUid() != null && item.getUid().equals(User.getInstance().getId())) {
                holder.btnReserve.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
            } else {
                holder.btnReserve.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
            }
        }

        holder.btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAvailable) {
                    Toast.makeText(context, finalMTime + "으로 예약합니다", Toast.LENGTH_SHORT).show();

                    // userID, building, room_number, time, listener){
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(new ReserveRequest(User.getInstance().getId(), item.getBuilding(), item.getRoom_number(), item.getTime(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean result = jsonResponse.getBoolean("result");

                                Log.d("test", "result: " + result);
                                if (result) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    AlertDialog dialog = builder.setMessage(item.getBuilding() + " " + item.getRoom_number() + "호실 " + finalMTime + "시에 예약되었습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();
                                    list.get(position).setUid(User.getInstance().getId());
                                    list.get(position).setAvailable(false);
                                    notifyItemChanged(position);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    AlertDialog dialog = builder.setMessage("예약 실패했습니다.")
                                            .setNegativeButton("확인", null)
                                            .create();
                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }));
//                new ReserveTask(null,item.getFloor(), null, Integer.parseInt(item.getTime()), User.getInstance().getId());
                } else {
                    if (item.getUid() != null && item.getUid().equals(User.getInstance().getId())) {
                        Toast.makeText(context, finalMTime + "예약을 취소합니다", Toast.LENGTH_SHORT).show();

                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(new DeleteReservationRequest(User.getInstance().getId(), item.getBuilding(), item.getRoom_number(), item.getTime(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean result = jsonResponse.getBoolean("result");

                                    Log.d("test", "result: " + result);
                                    if (result) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        AlertDialog dialog = builder.setMessage(item.getBuilding() + " " + item.getRoom_number() + "호실 " + finalMTime + "시 예약이 취소되었습니다.")
                                                .setPositiveButton("확인", null)
                                                .create();
                                        dialog.show();
                                        list.get(position).setUid(null);
                                        list.get(position).setAvailable(true);
                                        notifyItemChanged(position);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        AlertDialog dialog = builder.setMessage("예약 취소 실패했습니다.")
                                                .setNegativeButton("확인", null)
                                                .create();
                                        dialog.show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }));
                    } else {
                        Toast.makeText(context, "이미 예약된 강의실입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(ArrayList<EmptyRoomItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class EmptyRoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvTime;
        Button btnReserve;

        public EmptyRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            btnReserve = itemView.findViewById(R.id.btn_reserve);
        }
    }

    class ReserveTask extends AsyncTask<Void, Void, String> {
        String target;
        String building;
        String today;
        String id;
        int floor;
        int time;

        ReserveTask(String building, int floor, String today, int time, String id) {
            this.building = building;
            this.floor = floor;
            this.today = today;
            this.time = time;
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            target = "https://jaohoo.cafe24.com/ReserveRoom.php?name=" + building + "&floor=" + floor + "&day=" + today + "&time=" + time + "&id=" + id;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result) {
            try {
                String rst = new Gson().fromJson(result, new TypeToken<String>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
