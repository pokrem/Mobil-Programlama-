package com.example.ders1;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private String currentInput = "";
    private String operator = "";
    private double firstOperand = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);

        // 1. Sayı Tuşları (0-9)
        int[] numberButtonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                currentInput += button.getText().toString();
                tvDisplay.setText(currentInput);
            }
        };

        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(numberClickListener);
        }

        // 2. Operatör Tuşları (btnFraction yani Yüzde tuşu tekrar buraya eklendi)
        int[] operatorButtonIds = {
                R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide,
                R.id.btnPower, R.id.btnRoot, R.id.btnFraction
        };

        View.OnClickListener operatorClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentInput.isEmpty()) {
                    firstOperand = Double.parseDouble(currentInput);
                    Button button = (Button) v;
                    operator = button.getText().toString(); // Burada operatörü hafızaya alır (Örn: "%")
                    currentInput = "";
                    tvDisplay.setText(operator);
                }
            }
        };

        for (int id : operatorButtonIds) {
            findViewById(id).setOnClickListener(operatorClickListener);
        }

        // 3. 1/x Butonu (Bu anında çalışmaya devam eder)
        findViewById(R.id.btnInverse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentInput.isEmpty()) {
                    double value = Double.parseDouble(currentInput);
                    if (value != 0) {
                        double result = 1.0 / value;
                        currentInput = formatResult(result);
                        tvDisplay.setText(currentInput);
                    } else {
                        tvDisplay.setText("Sıfıra bölünemez");
                        currentInput = "";
                    }
                }
            }
        });

        // 4. Temizle (C) Butonu
        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentInput = "";
                operator = "";
                firstOperand = 0;
                tvDisplay.setText("");
            }
        });

        // 5. Eşittir (=) Butonu ve Matematiksel İşlemler
        findViewById(R.id.btnEquals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentInput.isEmpty() && !operator.isEmpty()) {
                    double secondOperand = Double.parseDouble(currentInput);
                    double result = 0;
                    boolean isError = false;

                    switch (operator) {
                        case "+":
                            result = firstOperand + secondOperand;
                            break;
                        case "-":
                            result = firstOperand - secondOperand;
                            break;
                        case "*":
                            result = firstOperand * secondOperand;
                            break;
                        case "/":
                            if (secondOperand != 0) {
                                result = firstOperand / secondOperand;
                            } else {
                                isError = true;
                            }
                            break;
                        case "x^y":
                            result = Math.pow(firstOperand, secondOperand);
                            break;
                        case "y√x":
                            // Kullanıcı y√x okunuşuna göre önce y'yi (firstOperand), sonra x'i (secondOperand) girer.
                            if (firstOperand != 0) {
                                // x üzeri (1/y) formülü ile doğru kök işlemi yapılır
                                result = Math.pow(secondOperand, 1.0 / firstOperand);
                            } else {
                                isError = true; // 0. dereceden kök tanımsızdır
                            }
                            break;
                        case "%": // XML'de buton text'ini "%" yapmalısın!
                        case "a/b": // Text'i henüz değiştirmediysen de çalışsın diye eklendi
                            // İLK SAYININ İKİNCİ SAYI KADAR YÜZDESİNİ ALIR
                            result = (firstOperand * secondOperand) / 100.0;
                            break;
                    }

                    if (isError) {
                        tvDisplay.setText("Tanımsız");
                        currentInput = "";
                    } else {
                        currentInput = formatResult(result);
                        tvDisplay.setText(currentInput);
                    }
                    operator = "";
                }
            }
        });
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.valueOf(result);
        }
    }
}