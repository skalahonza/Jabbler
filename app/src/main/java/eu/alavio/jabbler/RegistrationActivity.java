package eu.alavio.jabbler;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.alavio.jabbler.API.ApiHandler;
import eu.alavio.jabbler.Models.Dialogs;
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
    @BindView(R.id.register_progress)
    ProgressBar vProgress;

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

        registerTask = new RegisterTask(username, password, email, fullName, this);
        Dialogs.operateProgressView(true, vProgress);
        registerTask.execute();
    }

    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {

        private String username;
        private String password;
        private String email;
        private String fullName;
        private Context context;

        private String status = "";

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
            } catch (XMPPException | IOException | SmackException e) {
                Log.e("XMPP", "Error occured during registration.", e);
                status = e.getLocalizedMessage();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            registerTask = null;
            Dialogs.operateProgressView(false, vProgress);
            if (success) {
                //Registered
                Dialogs.userCreatedDialog(context, RegistrationActivity.this::onBackPressed);
            } else {
                Dialogs.userNotCreatedDialog(context, status);
            }
        }

        /**
         * Handles the task cancellation
         */
        @Override
        protected void onCancelled() {
            registerTask = null;
            Dialogs.operateProgressView(false, vPassword);
        }
    }
}


