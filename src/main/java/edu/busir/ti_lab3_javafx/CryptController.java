package edu.busir.ti_lab3_javafx;

import edu.busir.ti_lab3_javafx.logic.ArithmeticOperations;
import edu.busir.ti_lab3_javafx.logic.Cryptor;
import edu.busir.ti_lab3_javafx.logic.Decryptor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class CryptController {
    @FXML
    private RadioButton encryptButton;
    @FXML
    private TextField pValueTextField;
    @FXML
    private TextField xValueTextField;
    @FXML
    private TextField kValueTextField;
    @FXML
    private ComboBox<Long> rootsComboBox;
    @FXML
    private Button continueButton;
    @FXML
    private TextArea outputFileTextArea;

    private String kValue = "";

    @FXML
    private void initialize() {
        pValueTextField.textProperty().addListener(new ChangeListener<>() {
            @Override
            public synchronized void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String newValue = pValueTextField.getText();
                if (!newValue.matches("[0-9]+")) {
                    pValueTextField.setText(newValue.replaceAll("[^0-9]", ""));
                }
                if (pValueTextField.getText().length() > 4) {
                    pValueTextField.setText(pValueTextField.getText().substring(0, 4));
                }
                new Thread(() -> checkValues()).start();
            }
        });
        xValueTextField.textProperty().addListener(new ChangeListener<>() {
            @Override
            public synchronized void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String newValue = xValueTextField.getText();
                if (!newValue.matches("[0-9]+")) {
                    xValueTextField.setText(newValue.replaceAll("[^0-9]", ""));
                }
                if (xValueTextField.getText().length() > 4) {
                    xValueTextField.setText(pValueTextField.getText().substring(0, 4));
                }
                new Thread(() -> checkValues()).start();
            }
        });
        kValueTextField.textProperty().addListener(new ChangeListener<>() {
            @Override
            public synchronized void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String newValue = kValueTextField.getText();
                if (!newValue.matches("[0-9]+")) {
                    kValueTextField.setText(newValue.replaceAll("[^0-9]", ""));
                }
                if (kValueTextField.getText().length() > 4) {
                    kValueTextField.setText(pValueTextField.getText().substring(0, 4));
                }
                new Thread(() -> checkValues()).start();
            }
        });
        rootsComboBox.setOnHidden(actionEvent -> {
            if (encryptButton.isSelected()) {
                new Thread(this::checkValues).start();
            }
        });
        continueButton.setDisable(true);
        outputFileTextArea.setEditable(false);
        pValueTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        xValueTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        kValueTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
    }

    @FXML
    private void buttonClicked() {
        FileChooser loadFileChooser = new FileChooser();
        loadFileChooser.setTitle("Open file");
        FileChooser saveFileChooser = new FileChooser();
        saveFileChooser.setTitle("Save file");
        File loadFile = loadFileChooser.showOpenDialog(continueButton.getScene().getWindow());
        if (loadFile != null) {
            File saveFile = saveFileChooser.showSaveDialog(continueButton.getScene().getWindow());
            if (saveFile != null) {
                outputFileTextArea.setText("");
                if (encryptButton.isSelected()) {
                    Cryptor cryptor = new Cryptor(Long.parseLong(pValueTextField.getText()), Long.parseLong(xValueTextField.getText()),
                            Long.parseLong(kValueTextField.getText()), rootsComboBox.getValue());
                    try {
                        cryptor.cryptFile(loadFile, saveFile);
                        makeNotification(true, "Encryption", "Encrypted successful", 2000);
                        var fis = new BufferedInputStream(new FileInputStream(saveFile));
                        byte[] buff = new byte[1000];
                        int shortsAmount = fis.read(buff) / 2;
                        short[] shorts = new short[shortsAmount];
                        ByteBuffer.wrap(buff).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < shortsAmount; i += 2) {
                            stringBuilder.append(shorts[i]).append(",").append(shorts[i + 1]).append(" ");
                        }
                        outputFileTextArea.setText(stringBuilder.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                        makeNotification(false, "Encryption", "Encrypting error", 2000);
                    }
                } else {
                    Decryptor decryptor = new Decryptor(Long.parseLong(pValueTextField.getText()), Long.parseLong(xValueTextField.getText()));
                    try {
                        decryptor.decryptFile(loadFile, saveFile);
                        makeNotification(true, "Decryption", "Decrypted successful", 2000);
                        var fis = new BufferedInputStream(new FileInputStream(saveFile));
                        byte[] buff = new byte[1000];
                        int bytesAmount = fis.read(buff);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < bytesAmount; i++) {
                            stringBuilder.append(buff[i]).append(" ");
                        }
                        outputFileTextArea.setText(stringBuilder.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                        makeNotification(false, "Decryption", "Decrypting error", 2000);
                    }
                }
            }
        }
    }

    private void makeNotification(boolean isSuccess, String title, String text, double delayTime) {
        Notifications newNotification = Notifications.create()
                .title(title)
                .text(text)
                .graphic(null)
                .hideAfter(Duration.millis(delayTime))
                .position(Pos.BOTTOM_RIGHT);
        if (isSuccess) {
            newNotification.showConfirm();
        } else {
            newNotification.showError();
        }
    }

    @FXML
    private void encryptChoose() {
        kValueTextField.setDisable(false);
        kValueTextField.setText(kValue);
        kValueTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        new Thread(() -> {
            if (!pValueTextField.getText().isEmpty()) {
                ArrayList<Long> roots = ArithmeticOperations.findPrimitiveRoots(Long.parseLong(pValueTextField.getText()));
                Platform.runLater(() -> rootsComboBox.setItems(FXCollections.observableArrayList(roots)));
            }
        }).start();
        new Thread(this::checkValues).start();
    }

    @FXML
    private void decryptChoose() {
        kValueTextField.setDisable(true);
        kValueTextField.setStyle("-fx-background-color: lightgray; -fx-background-radius: 10;");
        kValue = kValueTextField.getText();
        kValueTextField.setText("");
        Platform.runLater(() -> rootsComboBox.setItems(FXCollections.observableArrayList(new ArrayList<>())));
        new Thread(this::checkValues).start();
    }

    private synchronized void checkValues() {
        boolean incorrectData = false;
        long p = 0;
        if (pValueTextField.getText().isEmpty() ||
                !ArithmeticOperations.isPrime(Long.parseLong(pValueTextField.getText()))) {
            pValueTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            incorrectData = true;
            Platform.runLater(() -> {
                synchronized (rootsComboBox) {
                    rootsComboBox.setItems(FXCollections.observableArrayList(new ArrayList<>()));
                }
            });
        } else {
            pValueTextField.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 10;");
            p = Long.parseLong(pValueTextField.getText());
            final long pKey = p;
            if (encryptButton.isSelected()) {
                new Thread(() -> {
                    ArrayList<Long> roots = ArithmeticOperations.findPrimitiveRoots(pKey);
                    Platform.runLater(() -> {
                        synchronized (rootsComboBox) {
                            rootsComboBox.setItems(FXCollections.observableArrayList(roots));
                        }
                    });
                }).start();
            }
        }
        if (xValueTextField.getText().isEmpty() || Long.parseLong(xValueTextField.getText()) <= 1
                || (!pValueTextField.getText().isEmpty() && Long.parseLong(xValueTextField.getText()) >= p)) {
            xValueTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            incorrectData = true;
        } else {
            xValueTextField.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 10;");
        }
        if (encryptButton.isSelected() && (kValueTextField.getText().isEmpty() || pValueTextField.getText().isEmpty() ||
                Long.parseLong(kValueTextField.getText()) <= 1 || Long.parseLong(kValueTextField.getText()) >= p
                || !ArithmeticOperations.isMutuallyPrime(p, Long.parseLong(kValueTextField.getText())))) {
            kValueTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            incorrectData = true;
        } else {
            if (encryptButton.isSelected()) {
                kValueTextField.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 10;");
            }
        }
        if (encryptButton.isSelected() && rootsComboBox.getValue() == null) {
            incorrectData = true;
        }
        continueButton.setDisable(incorrectData);
    }


}