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
import com.plugin.util.StringUtil;

import java.util.List;

/**
 * 生成单个搜索DTO的语句（只有左边，即根据左边生成构建语句）
 *
 * @author 沈钦林
 * @date 2023/9/28 17:30
 */
public class GenerateSingleLeft extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Document document = editor.getDocument();


        try {
            GenerateController generateController = new GenerateController();
            GenerateRequestDTO generateRequestDTO = new GenerateRequestDTO();
            generateRequestDTO.setLeftText(document.getText());
            Result<List<String>> result = generateController.generateSingleLeft(generateRequestDTO);


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
                        List<String> list = (List<String>) result.getData();
                        //在光标处插入一段代码
                        document.insertString(offset, StringUtil.handleTab(list.get(0)) + StringUtil.handleTab(list.get(1)));
                    }
                });
            } else { //索引不一致：当前有选中内容
                //选中的文本替换
                WriteCommandAction.runWriteCommandAction(project, () -> {
                    //获取请求结果
                    List<String> list = (List<String>) result.getData();
                    document.replaceString(start, end, StringUtil.handleTab(list.get(0)) + StringUtil.handleTab(list.get(1)));
                });
            }
            // 对替换后的文本移除选中效果
            primaryCaret.removeSelection();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}
