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
import com.plugin.OutputResult;
import com.plugin.PulginInputForm;
import com.plugin.util.StringUtil;

import javax.swing.*;
import javax.xml.transform.TransformerConfigurationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 生成类的mapper语句语句---左边是传入的DO/DTO(where条件的字段)的类定义，右边是返回的VO(select后的字段)的类定义
 *
 * @author 沈钦林
 * @date 2023/9/28 17:15
 */
public class GenerateMapper extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //获取操作项目对象，获取首个光标
        Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
        Document document = editor.getDocument();


        JFrame frame = new JFrame("生成类的mapper语句语句(左边是传入的DO/DTO即where条件，右边是返回的VO select后的字段)");
        PulginInputForm pulginInputForm = new PulginInputForm();
        pulginInputForm.rightJscrollPane.setVisible(false);//右输入框设置不可见
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
                String leftText = document.getText();
                String rightText = pulginInputForm.leftInput.getText();
                try {
                    GenerateController generateController = new GenerateController();
                    GenerateRequestDTO generateRequestDTO = new GenerateRequestDTO();
                    generateRequestDTO.setLeftText(leftText);
                    generateRequestDTO.setRightText(rightText);
                    Result<List<String>> result =generateController.generateMapper(generateRequestDTO);
                    //结果输入框
                    OutputResult outputResult = new OutputResult();
                    outputResult.setResult(null, StringUtil.handleTab(((List<String>) result.getData()).get(0)),null);
                    frame.setContentPane(outputResult.jPanel);
                    frame.pack();
                    frame.setVisible(true);
                    //设置窗口最大化
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } catch (TransformerConfigurationException transformerConfigurationException) {
                    transformerConfigurationException.printStackTrace();
                }
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
