package com.example.lddm;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    EditText nome, celular, email;
    Button contatosButton, whatsappButton, emailButton;
    Pessoa pessoa;
    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nome = (EditText) findViewById(R.id.nomeText);
        celular = (EditText) findViewById(R.id.celularText);
        email = (EditText) findViewById(R.id.emailText);
        contatosButton = (Button) findViewById(R.id.contatos);
        whatsappButton = (Button) findViewById(R.id.whatsapp);
        emailButton = (Button) findViewById(R.id.email);
        signInButton = findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        CallbackManager callbackManager = CallbackManager.Factory.create();


        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );

        contatosButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        botaoContato(view);
                    }
                }
        );
        whatsappButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        botaoWhatsapp(view);
                    }
                }
        );
        emailButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        botaoEmail(view);
                    }
                }
        );

        celular.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contatosButton.setEnabled(!celular.getText().toString().trim().isEmpty());
                whatsappButton.setEnabled(!celular.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailButton.setEnabled(!email.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            startActivity(new Intent(MainActivity.this, Main2Activity.class));
        } catch (ApiException e) {
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(MainActivity.this, Main2Activity.class));
        }
        super.onStart();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void botaoContato( View view){
        Intent intentContato = new Intent(ContactsContract.Intents.Insert.ACTION);
        intentContato.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        nome = (EditText) findViewById(R.id.nomeText);
        String name = nome.getText().toString();

        celular = (EditText) findViewById(R.id.celularText);
        String phone = celular.getText().toString();

        email = (EditText) findViewById(R.id.emailText);
        String mail = email.getText().toString();

        pessoa = new Pessoa(name, phone, mail);

        intentContato
                .putExtra(ContactsContract.Intents.Insert.EMAIL, pessoa.getEmail())
                .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.PHONE, pessoa.getTelefone())
                .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.NAME, pessoa.getNome())
        ;
        startActivity(intentContato);

    }
    public void botaoEmail( View view){
        nome = (EditText) findViewById(R.id.nomeText);
        String name = nome.getText().toString();

        celular = (EditText) findViewById(R.id.celularText);
        String phone = celular.getText().toString();

        email = (EditText) findViewById(R.id.emailText);
        String mail = email.getText().toString();

        pessoa = new Pessoa(name, phone, mail);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+ pessoa.getEmail()));
        intent
                .putExtra(Intent.EXTRA_EMAIL, pessoa.getEmail())
                .putExtra(Intent.EXTRA_SUBJECT, "Seu contato foi adicionado com sucesso." )
        ;
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }
    public void botaoWhatsapp( View view){
        nome = (EditText) findViewById(R.id.nomeText);
        String name = nome.getText().toString();

        celular = (EditText) findViewById(R.id.celularText);
        String phone = celular.getText().toString();

        email = (EditText) findViewById(R.id.emailText);
        String mail = email.getText().toString();

        pessoa = new Pessoa(name, phone, mail);

        try{
            String text = "Contato foi criado com sucesso.";
            String toNumber = "5531" + pessoa.getTelefone();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
