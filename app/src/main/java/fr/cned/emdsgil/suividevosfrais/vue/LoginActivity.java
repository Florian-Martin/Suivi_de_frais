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

        controleur = Controleur.getControleur();

        init();

        // Chargement des méthodes événementielles
        onCreateListenersLoading();
    }


    // -------- METHODES --------

    /**
     * Initialisation des objets graphiques et bind des views à des variables
     */
    private void init() {
        btnConnexion = findViewById(R.id.loginAct_btn_connexion);
        etLogin = findViewById(R.id.loginAct_editTxt_username);
        etPassword = findViewById(R.id.loginAct_editTxt_password);
    }

    // -------- EVENEMENTS --------

    /**
     * Au clic sur le bouton "Connexion" déclenchement du test de validité du login
     */
    private void onCreateListenersLoading() {
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin();
            }
        });
    }

    /**
     * Tentative d'authentification
     * <p>
     * Test si les champs ont été remplis, puis si le combo utilisateur / mot de passe saisi
     * correspond avec les données de la base distante
     */
    private void tryLogin() {
        String etLoginText = etLogin.getText().toString().trim();
        String etPasswordText = etPassword.getText().toString().trim();

        if (etLoginText.isEmpty()) {
            etLogin.setError("Veuillez renseigner votre identifiant");
        } else if (etPasswordText.isEmpty()) {
            etPassword.setError("Veuillez renseigner votre mot de passe");
        } else {
            etLogin.setError(null);
            etPassword.setError(null);
            controleur.setContext(this);
            controleur.logIn(etLoginText + "%" + etPasswordText);
        }
    }

    /**
     * Basculement sur le menu principal si l'authentification est réussie
     *
     * @param statutLogin Valeur de résultat de l'authentification
     */
    public void isLoginValid(Boolean statutLogin) {
        if (statutLogin) {
            Intent intent = new Intent(LoginActivity.this, AccueilActivity.class);
            startActivity(intent);
            // Empêche de revenir sur cette Activity depuis AccueilActivity, une fois connecté
            finish();
        } else {
            Toast.makeText(this, "Identifiants erronés, veuillez réessayer.", Toast.LENGTH_SHORT).show();
        }
    }
}