package fr.cned.emdsgil.suividevosfrais.vue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.cned.emdsgil.suividevosfrais.R;
import fr.cned.emdsgil.suividevosfrais.controleur.Controleur;

/**
 * Classe de présentation gérant la connexion de l'utilisateur
 *
 * <p>
 * Date : 2021
 *
 * @author fmart
 */
public class LoginActivity extends AppCompatActivity {

    // -------- VARIABLES --------
    private Button btnConnexion;
    private EditText etLogin, etPassword;
    private Controleur controleur;

    // -------- CYCLE DE VIE --------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnConnexion = findViewById(R.id.loginAct_btn_connexion);
        etLogin = findViewById(R.id.loginAct_editTxt_username);
        etPassword = findViewById(R.id.loginAct_editTxt_password);
        controleur = Controleur.getControleur();

        // Chargement des méthodes événementielles
        onCreateListenersLoading();
    }


    // -------- EVENEMENTS --------

    /**
     * Au clic sur le bouton "Connexion" déclenchement du test de validité du login
     */
    private void onCreateListenersLoading() {
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginValid()) {
                    Intent intent = new Intent(LoginActivity.this, AccueilActivity.class);
                    startActivity(intent);
                    // Empêche de revenir sur cette Activity depuis AccueilActivity, une fois connecté
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Identifiants erronés, veuillez réessayer.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Test de validité du login
     * <p>
     * Test si les champs ont été remplis, puis si le combo
     * utilisateur / mot de passe saisi correspond avec les données de la base distante
     *
     * @return True si le combo correspond.
     */
    private boolean isLoginValid() {
        String etLoginText = etLogin.getText().toString().trim();
        String etPasswordText = etPassword.getText().toString().trim();

        if (etLoginText.isEmpty()) {
            etLogin.setError("Veuillez renseigner le nom d'utilisateur");
        } else if (etPasswordText.isEmpty()) {
            etPassword.setError("Veuillez renseigner un mot de passe");
        } else {
            etLogin.setError(null);
            etPassword.setError(null);
            controleur.login(etLoginText + "%" + etPasswordText);
        }

        return Controleur.getStatutLogin();
    }
}