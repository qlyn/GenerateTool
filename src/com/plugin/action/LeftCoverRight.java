package com.plugin.action;

import com.example.bean.GenerateRequestDTO;
import com.example.controller.GenerateController;
import com.example.util.Result;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.plugin.PulginInputForm;
import com.plugin.util.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 当前类的同名属性的注释会转化为另一个类的同名属性注释（在修改注释时使用，输入当前要转换的类的定义，会识别是否同名属性即只转化同名属性的注释）
 *
 * @author 沈钦林
 * @date 2023/9/28 15:08
 */
public class LeftCoverRight extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //获取操作项目对象，获取首个光标
        Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
        Document document = editor.getDocument();


        JFrame frame = new JFrame("当前类的同名属性注释覆盖为输入类的同名属性注释");
        PulginInputForm pulginInputForm = new PulginInputForm();
        pulginInputForm.rightJscrollPane.setVisible(false);
        frame.setContentPane(pulginInputForm.content);
        //根据窗口里面的布局及组件的preferedSize来确定frame的最佳大小
        frame.pack();
        //传入参数null 即可让JFrame 位于屏幕中央, 这个函数若传入一个Component ,则JFrame位于该组件的中央
        frame.setLocationRelativeTo(null);
        // 显示窗口
        frame.setVisible(true);
        //确定按钮监听
        pulginInputForm.confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取左右输入框的文本
                String leftText = pulginInputForm.leftInput.getText();
                String rightText = document.getText();


                GenerateController generateController = new GenerateController();
                GenerateRequestDTO generateRequestDTO = new GenerateRequestDTO();
                generateRequestDTO.setLeftText(leftText);
                generateRequestDTO.setRightText(rightText);
                Result<String> result = generateController.leftCoverRight(generateRequestDTO);


                document.setText(StringUtil.handleTab((String) result.getData()));

                //隐藏输入框
                frame.setVisible(false);

            }
        });
        //取消按钮监听
        pulginInputForm.cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pulginInputForm.content.setVisible(false);
                frame.setVisible(false);
            }
        });
    }
}

