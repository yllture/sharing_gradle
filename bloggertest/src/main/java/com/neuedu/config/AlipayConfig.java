﻿package com.neuedu.config;

import java.io.FileWriter;
import java.io.IOException;
 
/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016100200646461";
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCAitymQev+G/CxB9xd4O8Eqe+GrT0gqcr+qosdO7HYttHtHQc1YIEc2jhj0BQyblHQn925o0vMWjJaJkuHb9uDqHMiMdPopnXuoTKh7crwAAKGB3Yhbt3BocFs8+t6x9Gqqvq4RCO8E5QHPHNjtdW9G+NLqNW5whBEIgRbzZlnRMPOJJzNqhG6EDB8m2IMtLJl2O97V34kZOkwdYwFiHspA/IwrfGqw/5i8XqvT/lN1DpeLku4IJJgBelL24GyVpHlEz3Xbp/v+WH6ztZXyqb2yDwZPuavtivpQNj8RH+FVU5A29urEbhurgCFp2a26IyeohxcbjXbSum1lK83b263AgMBAAECggEAD7FkSMPpjMdadjJwe+GXLKmWfYmu2csA5mBpxXLhPoTrUhhmJeUXPi+CDOV9Yb7ILEEoL565BUTJuunL9Mf6M7OgAr+0kEWqRizV4qEGUpCdvkEw8dn4JsiZtbR9viqg9UYUf+SJvE9jlhKSP80WQBUkhbsiBKClnbvNbmvGW64EMpanQcEtElS8RQDuRjruepfO6hdkLoh7YwhrMF8xxwARXHo/JqyU+O1IS4ZviuQYubsyOoC9sDRT4RLWX7gqYi++aRNjpkHhTAW7xTEEam/OEeFkCiL7ro1GgT93p0CPOKw3UhLJNWwhGneBCVbvHgBNi33J7sQQn7P7RIqGoQKBgQC1faBrrfgtrT6BUqc3YjV5+jQmyRNEU/g1WcAaoAbT1sr84kE3MY6PMnTQyKqEKSOZJEKrixh3Cd+J/+Gf1wlYA6wnBRvcuCrMF++kvUqSmMKhBXkcJ+pY+Iy6gEYC/yUqTNRMhlnvAkSvXcjKWON+A5c8edwsIQRBBSCruSpGpwKBgQC1UHUH3i9qrEsW/88CqnVXyrHSdVYaVR9vSWuvYSSKW8tUD6ENwvfSQ8W8RdfRL3PA8JaMszEemhOvFPvqaewtzoVeyU6MQprMWLZPO/V8RpgA1adVYE/5KIm4+e5lRaUy2x9lebIlgFWG03HXHe7F8d2+IsaYBzHI+wxpWg6pcQKBgF3v3RV+4SK7Ncv3/ObUpTsyPsYIczl9gM4UWzMsuvvwg/YqIXkw5EnH4uChzGhFZtCADSX/A7WxXYtu7tgtk/+DPxqfMVGn3Tbv+LC67Rs9iBeR3qKtIkXUt/pcJGCusQzY4VWOgIQOF4DxONlK8sypjSDXLheFK0+AGO3pFDGnAoGAYf4mAiaXQfP0cW24GBzAZr5p9PDtpDW1U5Q+2v6JnS2Xass3pBRnlACE8ghdZ/VZfkGJXGnMdlmXodyZekvdFbS0aFIsXanxt9eKULB7MLuGk4zlb0MqdeLu7EZIDfviGOZMxa9MFPCTSNcmg7jtv5XE3mo1Ix1gTttADgBaREECgYA/jUTWaX66xPeAu2GDjlmntYE/j2//hrQYJrA1uvNTjRoeSmohu9l2gzS1fvsgHozd/ET3VzBQQ/iLh0XRUkEuqlQA3VC2uKdOOYyor8ra10gxqWd08NuQF7FEpMvzSA7yjAcarrpzrt206J1GAkk8zJNOe4BFzLDFogrJCaksww==";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq+99SJJ/GSd/jQTTHBahzSQxqzo0nIhQaXs/Iwir4QLnjLLPxsOlW9uJWNAhdiQq71vYxMhuPLQu78hG19QScu9w8idTi3GwuxX9Kanv7E91nUxQS1GD4aA51WcnYTJcMDykEyqh7lcn6hKMmySlH47R2yUqiWeW2usIfIqm8lwhRCD36hqwpxyobdkzRYQYqg6I7Wy50YRSW/dWTcH7+tWTs+ZJZs4lzDbK5Ga+nSd3i48KscUuVEtB0kAYMXbxt3Hbm+MwOE08XqEagYJSxtEwxUjDRcoFtU8UmuVr6vAX3rVL886jhDMu+kD8Zo0oNbM5neLOzRz+XeWKt8Q7mwIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = " http://flyhorse.nat300.top/notify_url";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "  http://flyhorse.nat300.top/return_url";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

