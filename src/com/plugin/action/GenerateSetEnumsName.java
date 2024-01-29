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

/**
 * 生成枚举转换-----左边传入要生成的枚举转换的整个类（如dto）会将包含Enums的属性生成设置枚举转换
 *
 * @author 沈钦林
 * @date 2023/9/28 17:21
 */
public class GenerateSetEnumsName extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //获取操作项目对象，获取首个光标
        Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
        Document document = editor.getDocument();

        GenerateController generateController=new GenerateController();
        GenerateRequestDTO generateRequestDTO=new GenerateRequestDTO();
        generateRequestDTO.setLeftText(document.getText());
        Result<String> result = generateController.generateSetEnumsName(generateRequestDTO);

        // 获取选中文本起始索引和结束索引
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        if(start==end) {//索引一致：当前无选中内容
            //当前光标处插入替换
            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
                    //获取光标位置
                    Caret caret = editor.getCaretModel().getCurrentCaret();
                    int offset = caret.getOffset();
                    //在光标处插入一段代码
                    document.insertString(offset, StringUtil.handleTab((String)result.getData()));
                }
            });
        }else { //索引不一致：当前有选中内容
            //选中的文本替换
            WriteCommandAction.runWriteCommandAction(project,() ->{
                //选中内容替换
                document.replaceString(start,end,StringUtil.handleTab((String)result.getData()));
            });
        }
        // 对替换后的文本移除选中效果
        primaryCaret.removeSelection();
    }
}
