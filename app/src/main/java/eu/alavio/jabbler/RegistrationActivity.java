package eu.alavio.jabbler;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.alavio.jabbler.API.ApiHandler;
import eu.alavio.jabbler.Models.Helper;

import static eu.alavio.jabbler.R.id.email;

public class RegistrationActivity extends AppCompatActivity {

    //UI components
    @BindView(R.id.username)
    EditText vUsername;
    @BindView(R.id.fullName)
    EditText vFullName;
    @BindView(email)
    EditText vEmail;
    @BindView(R.id.password)
    EditText vPassword;
    @BindView(R.id.confirm_password)
    EditText vConfirm_password;

    RegisterTask registerTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_button)
    void onRegisterClick() {
        //get data from UI
        String username = String.valueOf(vUsername.getText());
        String fullName = String.valueOf(vFullName.getText());
        String email = String.valueOf(vEmail.getText());
        String password = String.valueOf(vPassword.getText());
        String confirm_password = String.valueOf(vConfirm_password.getText());


        //invalid username
        if (!Helper.validateUsername(username)) {
            vUsername.setError(getString(R.string.invalid_username));
            return;
        }

        //fullname empty
        if (fullName.isEmpty()) {
            vFullName.setError(getString(R.string.error_field_required));
        }

        //invalid email
        if (!Helper.validateEmail(email)) {
            vEmail.setError(getString(R.string.error_invalid_email));
            vEmail.requestFocus();
            return;
        }

        //password empty
        if (password.isEmpty()) {
            vPassword.setError(getString(R.string.password_empty));
            return;
        }

        //compare password and 2nd password
        if (!Objects.equals(password, confirm_password)) {
            vPassword.setError(getString(R.string.password_mismatch));
            vConfirm_password.setError(getString(R.string.password_mismatch));
            return;
        }

        //Remove error marks
        vUsername.setError(null);
        vFullName.setError(null);
        vEmail.setError(null);
        vPassword.setError(null);
        vConfirm_password.setError(null);

        registerTask = new RegisterTask(username, password, email, fullName,this);
        registerTask.execute();
    }

    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {

        private String username;
        private String password;
        private String email;
        private String fullName;
        private Context context;

        //Empty required
        public RegisterTask() {
        }

        public RegisterTask(String username, String password, String email, String fullName, Context context) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.fullName = fullName;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return ApiHandler.register(username, password, email, fullName);
            } catch (XMPPException e) {
                //Sever error or XMPP error
                Log.e("XMPP", "Error occured during registration.", e);
            } catch (IOException e) {
                //Connection error
            } catch (SmackException e) {
                //Server rejected
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            registerTask = null;
            if (success) {
                //Registered
                Toast.makeText(context,"Registered",Toast.LENGTH_LONG).show();
            } else {
                //Not register
                Toast.makeText(context,"NOT Registered",Toast.LENGTH_LONG).show();
            }
        }
    }
}


