package com.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

/**
 * XXXXXX
 *
 * @author 沈钦林
 * @date 2023/9/19 16:40
 */
public class FirstAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        System.out.println("调用");
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Document document = editor.getDocument();

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
                    document.insertString(offset, "测试数据");
                }
            });
        }else { //索引不一致：当前有选中内容
            //选中的文本替换
            WriteCommandAction.runWriteCommandAction(project,() ->
                    document.replaceString(start,end,"editor_basics")
            );
        }
        // 对替换后的文本移除选中效果
        primaryCaret.removeSelection();
        //消息框
//        Messages.showMessageDialog(
//                e.getProject(),
//                e.getRequiredData(CommonDataKeys.EDITOR).getSelectionModel().getSelectedText(),
//                "选中的内容",
//                Messages.getInformationIcon()
//        );
    }
}
