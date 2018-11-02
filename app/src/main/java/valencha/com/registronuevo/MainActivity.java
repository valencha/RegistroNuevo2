package valencha.com.registronuevo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    EditText edt_user, edt_email, edt_contrasena;
    Button btn_enviar;
    TextView tv_cuenta;
    FirebaseDatabase db;
    FirebaseAuth auth;
    CallbackManager  callbackManager;
    LoginButton loginButton;

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


        callbackManager = CallbackManager.Factory.create();
        loginButton=(LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigIn();
            }
        });
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

    private void sigIn() {

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e("Error",""+e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                String email = authResult.getUser().getEmail();
                Toast.makeText(MainActivity.this,"You are signed with email:"+email,Toast.LENGTH_SHORT).show();


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode,resultCode,data);

    }

    private void printKeyHash(){
        try{
            PackageInfo info   = getPackageManager().getPackageInfo("valencha.com.registronuevo",PackageManager.GET_SIGNATURES);

            for(Signature signature: info.signatures){
                MessageDigest messageDigest= MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.e("KEYHASH", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
            }


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
