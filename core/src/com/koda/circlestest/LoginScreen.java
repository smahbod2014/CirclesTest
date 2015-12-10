package com.koda.circlestest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

/**
 * Created by Sean on 12/9/2015.
 */
public class LoginScreen extends GameState {

    Skin skin;
    Stage stage;
    LoginScreenListener myListener;
    TextField usernameField, passwordField;

    public LoginScreen() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();

        Table table = new Table();
        Label label = new Label("Welcome to <insert game name here>! Please log in.", skin);
        Label usernameLabel = new Label("Username: ", skin);
        Label passwordLabel = new Label("Password: ", skin);
        usernameField = new TextField("", skin);
        usernameField.getOnscreenKeyboard().show(true);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        TextButton loginButton = new TextButton("Login", skin);
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                    Dialog dialog = new Dialog("Invalid fields", skin, "dialog");
                    dialog.text("Username/password cannot be left blank.");
                    dialog.button("OK");
                    dialog.show(stage);
                    return;
                }

                try {
                    System.out.println("Login button clicked. Connecting...");
                    if (!CirclesTest.client.isConnected()) {
                        CirclesTest.client.connect(5000, CirclesTest.HOST, 3001, 3002);
                    }
                    Messages.LoginMessage m = new Messages.LoginMessage(
                            usernameField.getText(),
                            passwordField.getText());
                    CirclesTest.client.sendTCP(m);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        TextButton registerButton = new TextButton("Register", skin);
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                    Dialog dialog = new Dialog("Invalid fields", skin, "dialog");
                    dialog.text("Username/password cannot be left blank.");
                    dialog.button("OK");
                    dialog.show(stage);
                    return;
                }

                try {
                    if (!CirclesTest.client.isConnected()) {
                        CirclesTest.client.connect(5000, CirclesTest.HOST, 3001, 3002);
                    }
                    Messages.RegistrationMessage m = new Messages.RegistrationMessage(
                            usernameField.getText(),
                            passwordField.getText());
                    CirclesTest.client.sendTCP(m);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        table.setFillParent(true);
        table.center();
        table.add(label).colspan(10).padBottom(10f).row();
        table.add(usernameLabel).colspan(3).padRight(5f);
        table.add(usernameField).colspan(7).padBottom(5f).row();
        table.add(passwordLabel).colspan(3).padRight(5f);
        table.add(passwordField).colspan(7).padBottom(5f).row();
        table.add(loginButton).colspan(5).fill().padRight(5f);
        table.add(registerButton).colspan(5).fill();

        stage.addActor(table);

        myListener = new LoginScreenListener();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        CirclesTest.client.addListener(myListener);
        usernameField.setText("");
        passwordField.setText("");
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        stage.draw();
    }

    private void handleRegistrationResponse(int resultCode) {
        if (resultCode == Messages.RegistrationResponse.RESULT_ACCEPTED) {
            Dialog dialog = new Dialog("Registration", skin, "dialog");
            dialog.text("Registration accepted!");
            dialog.button("OK");
            dialog.show(stage);
        }
        else if (resultCode == Messages.RegistrationResponse.RESULT_USER_EXISTS) {
            Dialog dialog = new Dialog("Registration", skin, "dialog");
            dialog.text("User already exists, please choose another username.");
            dialog.button("OK");
            dialog.show(stage);
        }
    }

    private void handleLoginResponse(int resultCode) {
        if (resultCode == Messages.LoginResponse.RESULT_ACCEPTED) {
            CirclesTest.client.removeListener(myListener);
            StateManager.putParam("username", usernameField.getText());
            StateManager.setState("game");
        }
        else if (resultCode == Messages.LoginResponse.RESULT_BAD_USERNAME) {
            Dialog dialog = new Dialog("Login", skin, "dialog") {
                @Override
                protected void result(Object object) {
                    usernameField.setText("");
                    passwordField.setText("");
                }
            };
            dialog.text("Incorrect username.");
            dialog.button("OK");
            dialog.show(stage);
        }
        else if (resultCode == Messages.LoginResponse.RESULT_BAD_PASSWORD) {
            Dialog dialog = new Dialog("Login", skin, "dialog") {
                @Override
                protected void result(Object object) {
                    usernameField.setText("");
                    passwordField.setText("");
                }
            };
            dialog.text("Incorrect password.");
            dialog.button("OK");
            dialog.show(stage);
        }
    }

    private class LoginScreenListener extends Listener {
        @Override
        public void received(Connection connection, Object object) {
            if (object instanceof Messages.WelcomeMessage) {
                Messages.WelcomeMessage m = (Messages.WelcomeMessage) object;
                System.out.println("Server says welcome: " + m.text);
            }
            else if (object instanceof Messages.RegistrationResponse) {
                final Messages.RegistrationResponse m = (Messages.RegistrationResponse) object;
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        handleRegistrationResponse(m.resultCode);
                    }
                });
            }
            else if (object instanceof Messages.LoginResponse) {
                final Messages.LoginResponse m = (Messages.LoginResponse) object;
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        handleLoginResponse(m.resultCode);
                    }
                });
            }
        }
    }
}
