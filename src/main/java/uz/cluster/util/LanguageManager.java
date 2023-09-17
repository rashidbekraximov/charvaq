package uz.cluster.util;

import org.springframework.context.i18n.LocaleContextHolder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class LanguageManager {
    static private final String baseBundleName = "langs/message";
    static private final String excelBundleName = "langs/excel";

    public static String getLangMessage(String keyword) {
        String text = ResourceBundle.getBundle(baseBundleName, LocaleContextHolder.getLocale()).getString(keyword);
        ByteBuffer encodedText = StandardCharsets.UTF_8.encode(text);
        return StandardCharsets.UTF_8.decode(encodedText).toString();
    }

    public static String getExcelLangMessage(String keyword) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(excelBundleName, LocaleContextHolder.getLocale());
        if (resourceBundle.containsKey(keyword))
            return resourceBundle.getString(keyword);
        else
            return keyword;
    }
}
