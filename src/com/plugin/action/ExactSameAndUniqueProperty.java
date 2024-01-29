package com.plugin.action;

import com.example.bean.GenerateRequestDTO;
import com.example.controller.GenerateController;
import com.example.util.Result;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.plugin.OutputResult;
import com.plugin.PulginInputForm;
import com.plugin.util.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 提取出同名和不同名字段
 *
 * @author 沈钦林
 * @date 2023/9/19 16:40
 */
public class ExactSameAndUniqueProperty extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //获取操作项目对象，获取首个光标
        Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);

        JFrame frame = new JFrame("提取出同名和不同名字段");
        PulginInputForm pulginInputForm = new PulginInputForm();
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
                String rightText = pulginInputForm.rightInput.getText();

                GenerateController generateController=new GenerateController();
                GenerateRequestDTO generateRequestDTO=new GenerateRequestDTO();
                generateRequestDTO.setLeftText(leftText);
                generateRequestDTO.setRightText(rightText);
                Result<List<String>> result = generateController.exactSameAndUniqueProperty(generateRequestDTO);
                //结果输入框
                OutputResult outputResult = new OutputResult();
                List<String> list=(List<String>)result.getData();
                outputResult.setResult(StringUtil.handleTab(list.get(1)),StringUtil.handleTab(list.get(0)),StringUtil.handleTab(list.get(2)));
                frame.setContentPane(outputResult.jPanel);
                frame.pack();
                frame.setVisible(true);
                //设置窗口最大化
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
