package com.homework.calculator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CalculatorFrame {
    public static void main(String[] args) {
        CalculatorFrame calculatorFrame = new CalculatorFrame(400,200,"Calculator");
    }

    private JFrame window;
    private static String[] labels = {"1","2","3","+","4","5","6","-","7","8","9","*","0","CLR","=","\\"};

    public CalculatorFrame(int width, int height, String title) {
        window = CreateFrame(width, height, "Calculator");
        if (window == null) {
            throw new NullPointerException("Failed to create Calculator window");
        }
    }

    public JFrame getWindow() {
        return window;
    }

    public void setWindow(JFrame window) {
        this.window = window;
    }

    private JFrame CreateFrame(int width, int height, String title) {
        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new FlowLayout());

        InitFrame(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        return frame;
    }

    private void InitFrame(JFrame frame) {
        Container container = frame.getContentPane();
        JPanel textPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4,4));

        container.add(textPanel);
        container.add(buttonPanel);

        JTextField area = new JTextField(20);
        area.setText("0");
        textPanel.add(area);

        for (int i = 0; i < labels.length; ++i)
        {
            JButton button = new JButton(labels[i]);
            button.addActionListener(new ButtonAction(labels[i], area));
            buttonPanel.add(button);
        }
    }

    class ButtonAction implements ActionListener {
        String string;
        JTextField field;
        public ButtonAction(String arg, JTextField area) {
            string = arg;
            field = area;
        }

        public void actionPerformed(ActionEvent e) {
            String display = field.getText();
            switch (string){
                case "1": case "2": case "3":case "4": case "5": case "6": case "7": case "8": case "9": case "0":
                    if (display.equals("0")) {
                        if (string.equals("0")) {
                            break;
                        }else {
                            field.setText(string);
                            break;
                        }
                    }else {
                        field.setText(display + string);
                        break;
                    }
                case "CLR":
                    field.setText("0");
                    break;
                case "+":case "-":case "*":case "/":
                    field.setText(display + string);
                    break;
                case "=":
                    double result = StringCalculator.getResult(field.getText());
                    field.setText(Double.toString(result));
                    break;
                default:
                    break;
            }
        }
    }
}
