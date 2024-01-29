package com.plugin;

import javax.swing.*;

public class PulginInputForm {
    public JTextArea leftInput;
    public JTextArea rightInput;
    public JButton cancel;
    public JButton confirm;
    public JPanel content;
    public JScrollPane leftJscrollPane;
    public JScrollPane rightJscrollPane;

    public JPanel getContent() {
        return content;
    }
    public PulginInputForm() {
        //设置当文本字长度超过frame的宽度时自动换行（）
        leftInput.setLineWrap(true);
        rightInput.setLineWrap(true);
    }
}
