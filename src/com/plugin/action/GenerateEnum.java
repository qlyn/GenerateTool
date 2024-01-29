package com.plugin.action;

import com.example.bean.GenerateRequestDTO;
import com.example.controller.GenerateController;
import com.example.util.Result;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.plugin.PulginInputForm;
import com.plugin.util.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 生成枚举类-----左边传入要生成的枚举属性文本，右边传入依照顺序是枚举属性分隔符、code的类型（是String还是Integer）、枚举类名，以@@分隔
 *
 * @author 沈钦林
 * @date 2023/9/28 17:21
 */
public class GenerateEnum extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //获取操作项目对象，获取首个光标
        Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
        Document document = editor.getDocument();


        JFrame frame = new JFrame("生成枚举类-----左边传入要生成的枚举属性文本，右边传入依照顺序是枚举属性分隔符、code的类型（是String还是Integer）、枚举类名，以@@分隔");
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
                try {
                    GenerateController generateController = new GenerateController();
                    GenerateRequestDTO generateRequestDTO = new GenerateRequestDTO();
                    generateRequestDTO.setLeftText(leftText);
                    generateRequestDTO.setRightText(rightText);
                    Result<String> result = generateController.generateEnum(generateRequestDTO);


                    // 获取选中文本起始索引和结束索引
                    Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
                    int start = primaryCaret.getSelectionStart();
                    int end = primaryCaret.getSelectionEnd();
                    if (start == end) {//索引一致：当前无选中内容
                        //当前光标处插入替换
                        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                            @Override
                            public void run() {
                                //获取光标位置
                                Caret caret = editor.getCaretModel().getCurrentCaret();
                                int offset = caret.getOffset();
                                //在光标处插入一段代码
                                document.insertString(offset, StringUtil.handleTab((String) result.getData()));
                            }
                        });
                    } else { //索引不一致：当前有选中内容
                        //选中的文本替换
                        WriteCommandAction.runWriteCommandAction(project, () -> {
                            //获取选中前的光标位置
                            Caret caret = editor.getCaretModel().getPrimaryCaret();
                            int offset = caret.getOffset();
                            //选中内容替换
                            document.replaceString(start, end, StringUtil.handleTab((String) result.getData()));
                        });
                    }
                    // 对替换后的文本移除选中效果
                    primaryCaret.removeSelection();
                    //隐藏输入框
                    frame.setVisible(false);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    throw new RuntimeException(exception);
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
