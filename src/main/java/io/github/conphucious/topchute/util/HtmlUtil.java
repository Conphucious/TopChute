package io.github.conphucious.topchute.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HtmlUtil {

    private static final String STRING_PLACEHOLDER = "{NAME}";
    private static final String OTP_PLACEHOLDER = "{OTP}";
    private static final String OTP_TEMPLATE = "<html lang=en style=box-sizing:border-box><style>body{box-sizing:border-box;background:#f5f5f5;font-family:ArtifaktElement,sans-serif;padding:0}a,a:active,a:hover,a:visited{color:#000}.email-container{width:670px}@media only screen and (max-width:670px){.email-container{width:100%}}</style><table align=center><tr><td><table style=\"margin:0 auto\"bgcolor=white class=email-container><tr><td><table style=background-color:#000;color:#fff;width:100%><tr><td style=\"padding:17px 32px;line-height:13.5px\"align=left><img src=\"https://raw.githubusercontent.com/Conphucious/TC_Assets/refs/heads/main/tclogo.png\"></table><table style=width:100%><tr><td style=\"padding:0 32px 10px 32px\"><h1 style=\"color:#000;font-size:32px;font-weight:700;margin:0 0 20px 0\">One-time passcode</h1><p style=font-size:16px;color:#000;line-height:24px>Hi " + STRING_PLACEHOLDER + ",<p style=font-size:16px;color:#000;line-height:24px>To complete the action for your Top Chute account, enter the one-time passcode (OTP).<p style=font-size:16px;color:#000;line-height:24px>Code:<p style=\"font-size:32px;font-weight:700;color:#000;line-height:24px;margin:0 0 30px 0\">" + OTP_PLACEHOLDER + "<p style=\"font-size:16px;color:#000;line-height:24px;margin:0 0 40px 0\">Don't share this code with anyone or forward this email.</table><table style=width:100%><tr><td style=\"padding:0 32px 0 32px;line-height:24px\"><div style=\"border-top:1px solid #ccc;margin-bottom:25px\"></div></table><table style=width:100%><tr><td style=\"padding:0 32px 0 32px\"><p style=\"font-size:12px;color:#9a9a9a;line-height:16px;margin:0 0 10px 0\">Top Chute - Phuc Nguyen - All rights reserved.<p style=\"font-size:12px;color:#9a9a9a;line-height:16px;margin:0 0 5px 0\">This is an operational email. Please do not reply to this email. Replies to this email will not be responded to or read.</table></table></table>";
    private static final String REGISTER_TEMPLATE = "<html lang=en style=box-sizing:border-box><style>body{box-sizing:border-box;background:#f5f5f5;font-family:ArtifaktElement,sans-serif;padding:0}a,a:active,a:hover,a:visited{color:#000}.email-container{width:670px}@media only screen and (max-width:670px){.email-container{width:100%}}</style><table align=center><tr><td><table style=\"margin:0 auto\"bgcolor=white class=email-container><tr><td><table style=background-color:#000;color:#fff;width:100%><tr><td style=\"padding:17px 32px;line-height:13.5px\"align=left><img src=\"https://raw.githubusercontent.com/Conphucious/TC_Assets/refs/heads/main/tclogo.png\"></table><table style=width:100%><tr><td style=\"padding:0 32px 10px 32px\"><h1 style=\"color:#000;font-size:32px;font-weight:700;margin:0 0 20px 0\">One-time passcode</h1><p style=font-size:16px;color:#000;line-height:24px>Hi " + STRING_PLACEHOLDER + ",<p style=font-size:16px;color:#000;line-height:24px>To complete the action for your Top Chute account, enter the one-time passcode (OTP).<p style=font-size:16px;color:#000;line-height:24px>Code:<p style=\"font-size:32px;font-weight:700;color:#000;line-height:24px;margin:0 0 30px 0\">" + OTP_PLACEHOLDER + "<p style=\"font-size:16px;color:#000;line-height:24px;margin:0 0 40px 0\">Don't share this code with anyone or forward this email.</table><table style=width:100%><tr><td style=\"padding:0 32px 0 32px;line-height:24px\"><div style=\"border-top:1px solid #ccc;margin-bottom:25px\"></div></table><table style=width:100%><tr><td style=\"padding:0 32px 0 32px\"><p style=\"font-size:12px;color:#9a9a9a;line-height:16px;margin:0 0 10px 0\">Top Chute - Phuc Nguyen - All rights reserved.<p style=\"font-size:12px;color:#9a9a9a;line-height:16px;margin:0 0 5px 0\">This is an operational email. Please do not reply to this email. Replies to this email will not be responded to or read.</table></table></table>";

    public static String getOtpTemplate(String emailAddress, int otp) {
        return OTP_TEMPLATE
                .replace(STRING_PLACEHOLDER, emailAddress)
                .replace(OTP_PLACEHOLDER, String.valueOf(otp));
    }

    public static String getRegisterTemplate(String emailAddress, int otp) {
        return REGISTER_TEMPLATE;
    }

}
