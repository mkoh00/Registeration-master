package com.cookandroid.registeration.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cookandroid.registeration.R;
import com.cookandroid.registeration.helper.network.RegisterRequest;
import com.cookandroid.registeration.helper.network.ValidateRequest;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private ArrayAdapter adapter;
    private Spinner spinner;
    private String userID, Password, userName, userMajor;
    private AlertDialog dialog;
    private boolean validate=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        spinner=(Spinner)findViewById(R.id.majorSpinner);
        adapter=ArrayAdapter.createFromResource(this, R.array.major, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText idText=(EditText)findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final EditText nameText = (EditText) findViewById(R.id.nameText);



        //회원가입시 아이디 사용가능여부 검증
        final Button validateButton=(Button) findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID=idText.getText().toString();
                if(validate){//검증 완료
                    return;
                }
                //ID입력 안했다면
                if(userID.equals(""))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다!!")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                //검증
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);
                                validate=true;
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다!!")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };

                ValidateRequest validateRequest=new ValidateRequest(userID,responseListener);
                RequestQueue queue=Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        Button registerButton =(Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = idText.getText().toString();
                String Password = passwordText.getText().toString();
                String userName = nameText.getText().toString();
                String userMajor = spinner.getSelectedItem().toString();

                if(!validate){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("먼저 중복 체크를 해주세요")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(userID.equals("")  || Password.equals("") ||userName.equals((""))  || userMajor.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈칸없이 입력해주세요!!")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success) {
                                Toast.makeText(RegisterActivity.this,"회원가입에 성공하였습니다", Toast.LENGTH_SHORT).show();
                                idText.setEnabled(false);
                                validate=true;
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                finish();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 등록에 실패했습니다..")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(userID,Password,userName,userMajor,responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }

    @Override
    protected  void onStop(){//회원 등록 완료 후 회원가입창 꺼지게 되면 실행되는 부분
        super.onStop();
        if(dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }
}
