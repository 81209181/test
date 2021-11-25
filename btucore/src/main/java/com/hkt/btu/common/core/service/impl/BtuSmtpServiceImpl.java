package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuSiteConfigService;
import com.hkt.btu.common.core.service.BtuSmtpService;
import com.hkt.btu.common.core.service.BtuTemplateEngineService;
import com.hkt.btu.common.core.service.bean.BtuSiteConfigBean;
import com.hkt.btu.common.core.util.Base64Utile_cc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.hkt.btu.common.core.service.bean.BtuEmailBean.*;

public class BtuSmtpServiceImpl implements BtuSmtpService {

    private static final Logger LOG = LogManager.getLogger(BtuSmtpServiceImpl.class);

    protected static final String EMAIL_TEMPLATE_DIR = "email/";
    protected static final String EMAIL_TEMPLATE_SUBJECT_SUFFIX = "_subject";
    protected static final String EMAIL_TEMPLATE_BODY_SUFFIX = "_body";

    @Resource(name = "siteConfigService")
    BtuSiteConfigService siteConfigService;
    @Resource(name = "templateEngineService")
    BtuTemplateEngineService btuTemplateEngineService;

    @Override
    public void send(String templateId, String recipient, File file, Map<String, Object> dataMap) {
        dataMap = dataMap==null ? new HashMap<>() : dataMap;
        dataMap.put(EMAIL_BASIC_RECIPIENT, recipient);
        send(templateId, new String[]{recipient}, file, dataMap);
    }

    @Override
    public void send(String templateId, String[] recipients, File file, Map<String, Object> dataMap) {
        send(templateId, recipients, null, file, dataMap);
    }

    @Override
    public void send(String templateId, String[] recipients, String[] ccRecipients, File file, Map<String, Object> dataMap) {
        BtuSiteConfigBean sdSiteConfigBean = siteConfigService.getSiteConfigBean();

        String subjectTemplate = EMAIL_TEMPLATE_DIR + templateId + EMAIL_TEMPLATE_SUBJECT_SUFFIX;
        String bodyTemplate = EMAIL_TEMPLATE_DIR + templateId + EMAIL_TEMPLATE_BODY_SUFFIX;

        // inject common data to dataMap
        dataMap = dataMap==null ? new HashMap<>() : dataMap;

        dataMap.put(EMAIL_BASIC_TEMPLATE_ID, templateId);
        injectGlobalEmailData(dataMap, sdSiteConfigBean);

        // build html content
        String subjectString = btuTemplateEngineService.buildHtmlStringFromHtmlFile(subjectTemplate, dataMap);
        String htmlBodyString = btuTemplateEngineService.buildHtmlStringFromHtmlFile(bodyTemplate, dataMap);

        // add subject prefix to non-production email
        if (!siteConfigService.isProductionServer()) {
            subjectString = String.format("[%s] %s", sdSiteConfigBean.getServerType(), subjectString);
        }

        send(recipients, ccRecipients, subjectString, htmlBodyString, file);
    }

    @Override
    public void send(String[] recipients, String[] ccRecipients, String subjectString, String htmlBodyString, File file) {
        /*
         *用户名和密码
         */
        String SendUser="rico_wx_liu@163.com";
        String SendPassword="SVFQQHKQLLABLHVE";
//        String ReceiveUser="rico_wx_liu01@163.com";
        String ReceiveUser = "";
        for (String string : recipients) {
            ReceiveUser+=string;
        }

        /*
         *对用户名和密码进行Base64编码
         */
        String UserBase64= Base64Utile_cc.EncodeBase64(SendUser.getBytes());
        String PasswordBase64=Base64Utile_cc.EncodeBase64(SendPassword.getBytes());

        try {
            /*
             *远程连接smtp.163.com服务器的25号端口
             *并定义输入流和输出流(输入流读取服务器返回的信息、输出流向服务器发送相应的信息)
             */
            Socket socket=new Socket("smtp.163.com", 25);
            InputStream inputStream=socket.getInputStream();//读取服务器返回信息的流
            InputStreamReader isr=new InputStreamReader(inputStream);//字节解码为字符
            BufferedReader br=new BufferedReader(isr);//字符缓冲

            OutputStream outputStream=socket.getOutputStream();//向服务器发送相应信息
            PrintWriter pw=new PrintWriter(outputStream, true);//true代表自带flush
            System.out.println(br.readLine());

            /*
             *向服务器发送信息以及返回其相应结果
             */

            //helo
            pw.println("helo 163.com");
            System.out.println(br.readLine());

            //auth login
            pw.println("auth login");
            System.out.println(br.readLine());
            pw.println(UserBase64);
            System.out.println(br.readLine());
            pw.println(PasswordBase64);
            System.out.println(br.readLine());

            //Set "mail from" and  "rect to"
            pw.println("mail from:<"+SendUser+">");
            System.out.println(br.readLine());
            pw.println("rcpt to:<"+ReceiveUser+">");
            System.out.println(br.readLine());

            //Set "data"
            pw.println("data");
            System.out.println(br.readLine());

            //正文主体(包括标题,发送方,接收方,内容,点)
            pw.println("subject:"+subjectString);
            pw.println("from:"+SendUser);
            pw.println("to:"+ReceiveUser);
            pw.println("Content-Type: text/html;charset=\"utf-8\"");//设置编码格式可发送中文内容
            pw.println();
            pw.println(htmlBodyString);
            pw.println(".");
            pw.print("");
            System.out.println(br.readLine());

            LOG.warn("Email Recipient: " + Arrays.toString(recipients));
            LOG.warn("Email CC Recipient: " + Arrays.toString(ccRecipients));
            LOG.warn("Email Subject: " + subjectString);
            LOG.warn("Email Body: \n" + htmlBodyString);
            LOG.info("Sent email [" + subjectString + "] to " + Arrays.toString(recipients) + ".");

            /*
             *发送完毕,中断与服务器连接
             */
            pw.println("rset");
            System.out.println(br.readLine());
            pw.println("quit");
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void injectGlobalEmailData(Map<String, Object> dataMap, BtuSiteConfigBean siteConfigBean) {
        if (dataMap != null) {
            String webUrl = siteConfigBean.getAppHttpsUrl();
            String webName = siteConfigBean.getAppName();

            dataMap.put(EMAIL_GLOBAL_CONTACT_WEB_URL, webUrl);
            dataMap.put(EMAIL_GLOBAL_CONTACT_WEB_NAME, webName);
            dataMap.put(EMAIL_GLOBAL_LOGO_CID, EMAIL_GLOBAL_LOGO_CID);
            dataMap.put(EMAIL_GLOBAL_LOGO_HKT_CID, EMAIL_GLOBAL_LOGO_HKT_CID);
            dataMap.put(EMAIL_GLOBAL_LOGO_PCCW_GRP_CID, EMAIL_GLOBAL_LOGO_PCCW_GRP_CID);
        }
    }

}