package fr.cned.emdsgil.suividevosfrais.vue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.cned.emdsgil.suividevosfrais.R;

public class LoginActivity extends AppCompatActivity {

    // -------- VARIABLES --------
    private Button btnConnexion;


    // -------- CYCLE DE VIE --------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnConnexion = findViewById(R.id.loginAct_btn_connexion);

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
            }
        });
    }

    /**
     * Test de validité du login
     *
     * Test si les champs ont été remplis, puis si le combo
     * utilisateur / mot de passe saisi correspond avec les données de la base distante
     *
     * @return True si le combo correspond.
     */
    private boolean isLoginValid() {
        // TODO: Tester si les champs sont remplis avant d)envoyer la requête au serveur
        return true;
    }
}