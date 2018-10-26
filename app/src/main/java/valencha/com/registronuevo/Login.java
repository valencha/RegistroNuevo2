package valencha.com.registronuevo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    EditText edt_name, edt_pass;
    TextView tv_inicia, tv_nocuenta;
    Button btn_ingresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edt_name= findViewById(R.id.edt_name);
        edt_pass= findViewById(R.id.edt_pass);
        btn_ingresa= findViewById(R.id.btn_ingresa);
        tv_inicia= findViewById(R.id.tv_inicia);
        tv_nocuenta= findViewById(R.id.tv_nocuenta);
        tv_nocuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent (Login.this,MainActivity.class);

                    startActivity(i);
                    finish();
            }
        });


    }
}
