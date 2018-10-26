package valencha.com.registronuevo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText edt_user, edt_email, edt_contrasena;
    Button btn_enviar;
    TextView tv_cuenta;
    FirebaseDatabase db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        auth= FirebaseAuth.getInstance();

        edt_user= findViewById(R.id.edt_user);
        edt_email= findViewById(R.id.edt_email);
        edt_contrasena= findViewById(R.id.edt_contrasena);
        btn_enviar= findViewById(R.id.btn_enviar);
        tv_cuenta= findViewById(R.id.tv_cuenta);

        /*
        ///PARA AGREGAR A LA BASE DE DATOS
        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = new Usuario(edt_user.getText().toString(),edt_email.getText().toString(),edt_contrasena.getText().toString());
                db.getReference().child("usuarios").push().setValue(usuario);
            }
        });
*/

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            auth.createUserWithEmailAndPassword(edt_email.getText().toString(),edt_contrasena.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Usuario nuevo= new Usuario(edt_user.getText().toString(),edt_email.getText().toString(),edt_contrasena.getText().toString());
                        nuevo.setUid(auth.getCurrentUser().getUid());
                        //Child avisa a una rama hijo que se esta iniciando
                        db.getReference().child(nuevo.getUid()).push().setValue(nuevo);

                    }else{
                        Toast.makeText(MainActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });


            }
        });


        tv_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (MainActivity.this, Login.class);

                startActivity(i);
                finish();
            }
        });

    }
}
