package com.plugin;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class OutputResult {
    public JPanel jPanel;
    public JTextArea generateText;
    private JTextArea leftGenerateText;
    private JTextArea rightGenerateText;
    private JScrollPane jScrollPane;
    private JScrollPane leftJScrollPane;
    private JScrollPane rightJScrollPane;

    public OutputResult() {
        //设置当文本字长度超过frame的宽度时自动换行（）
        generateText.setLineWrap(true);
        leftGenerateText.setLineWrap(true);
        rightGenerateText.setLineWrap(true);
    }
    //设置文本框内容
    public void setResult(String leftText, String text, String rightText) {
        if (Optional.ofNullable(leftText).isPresent()) {
            leftGenerateText.setText(leftText);
            //设置滚动条使其在上方
            javax.swing.SwingUtilities.invokeLater(new Runnable() { public void run() {
                //设置滚动条水平位置、垂直位置，使其在左上方
                leftJScrollPane.getViewport().setViewPosition( new Point(0, 0) );
            }});
        }else{
            leftJScrollPane.setVisible(false);
        }
        if (Optional.ofNullable(text).isPresent()) {
            generateText.setText(text);
            //设置滚动条使其在上方
            javax.swing.SwingUtilities.invokeLater(new Runnable() { public void run() {
                //设置滚动条水平位置、垂直位置，使其在左上方
                jScrollPane.getViewport().setViewPosition( new Point(0, 0) );
            }});
        }else{
            jScrollPane.setVisible(false);
        }
        if (Optional.ofNullable(rightText).isPresent()) {
            rightGenerateText.setText(rightText);
            //设置滚动条使其在上方
            javax.swing.SwingUtilities.invokeLater(new Runnable() { public void run() {
                //设置滚动条水平位置、垂直位置，使其在左上方
                rightJScrollPane.getViewport().setViewPosition( new Point(0, 0) );
            }});
        }else{
            rightJScrollPane.setVisible(false);
        }
    }

}
