package com.plugin;

import com.example.util.Result;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.plugin.bean.LoginDto;
import com.plugin.bean.RequestAO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class SecondAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        // TODO: insert action logic here
//        System.out.println("生成");

        //获取操作项目对象，获取首个光标
        Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
        Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        Document document = editor.getDocument();
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        //获取当前光标位置
        Caret caret = editor.getCaretModel().getCurrentCaret();
        int offset = caret.getOffset();

        JFrame frame = new JFrame("窗口标题");
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
                //设置窗口最大化
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//                System.out.println("确定按钮监听");
                //获取登录token
                SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
                factory.setConnectTimeout(3000);
                factory.setReadTimeout(30000);
                RestTemplate restTemplate = new RestTemplate(factory);
                LoginDto loginDto = new LoginDto();
                loginDto.setUsername("admin");
                loginDto.setPassword("123");
                HttpEntity requestEntity = new HttpEntity<>(loginDto);
                ResponseEntity<Result> responseEntity = restTemplate.exchange("http://localhost:9001/index/login", HttpMethod.POST, requestEntity, Result.class);
                //获取左右输入框的文本
                String leftText = pulginInputForm.leftInput.getText();
                String rightText = pulginInputForm.rightInput.getText();
                //请求头
                HttpHeaders headers = new HttpHeaders();
                Map<String, String> map = (Map<String, String>) responseEntity.getBody().getData();
                headers.set("token", map.get("token"));
                //请求体
                RequestAO requestAO = new RequestAO();
                requestAO.setLeftText(leftText);
                requestAO.setRightText(rightText);
                requestEntity = new HttpEntity<>(requestAO, headers);
                restTemplate = new RestTemplate(factory);
                responseEntity = null;
                try {
                    responseEntity = restTemplate.exchange("http://localhost:9001/generate/generateEnum/", HttpMethod.POST, requestEntity, Result.class);
                } catch (RestClientException exception) {
                    exception.printStackTrace();
                }
                Result result = responseEntity.getBody();
                //结果输入框
                OutputResult outputResult = new OutputResult();
                outputResult.setResult(null,(String) result.getData(),null);
                frame.setContentPane(outputResult.jPanel);
                frame.pack();
                frame.setVisible(true);

                // 获取选中文本起始索引和结束索引
//                int start = primaryCaret.getSelectionStart();
//                int end = primaryCaret.getSelectionEnd();
//                if (start == end) {//索引一致：当前无选中内容
//                    //当前光标处插入替换
//                    WriteCommandAction.runWriteCommandAction(project, new Runnable() {
//                        @Override
//                        public void run() {
//                            String str = (String) result.getData();
//                            //在光标处插入一段代码（\r不被识别，会报错）
//                            document.insertString(offset, str.replace("\r", "    "));
//                        }
//                    });
//                } else { //索引不一致：当前有选中内容
//                    //选中的文本替换
//                    WriteCommandAction.runWriteCommandAction(project, () -> document.replaceString(start, end, (String) result.getData().replace("\r", "    "))));
//                }
//                frame.setVisible(false);
            }
        });
        //取消按钮监听
        pulginInputForm.cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pulginInputForm.content.setVisible(false);
                frame.setVisible(false);
//                System.out.println("取消按钮监听");

            }
        });
        // 对替换后的文本移除选中效果
        primaryCaret.removeSelection();
    }
}
