package searchengine.Utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import searchengine.config.ConnectionConfig;
import searchengine.exceptions.ApiException;
import searchengine.exceptions.MessagesAndErrorCodes;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component

@AllArgsConstructor
public class NetworkWorker {
    public static final String regExDomain = "^(?:https?://)?(?:www\\.)?([\\wа-я\\-]{1,256}(?:\\.[\\wа-я\\-]{1,256}){1,6})/?.*$";
    public static final String regExUrlGeneral = "^(https?://)?(www\\.)?[a-zA-Zа-яА-Я0-9][а-яa-zА-ЯA-Z0-9]{0,62}(\\.[a-zA-Zа-яА-Я0-9][а-яa-zА-ЯA-Z0-9]{0,62})+(?:[-a-zа-яA-ZА-Я0-9()@:%_\\+.~#?&/=]*)$";
    public static final String regExProtocol = "^(https?://)?(?:www\\.)?";
    public static final String regExPath ="^(?:https?://)?(?:www\\.)?([\\wа-я\\-]{1,256}(?:\\.[\\wа-я\\-]{1,256}){1,6})";
    private static final String AnchorAndFilesRegEx = "((.+\\/\\?.+)|(.+&.+)|(.+#.*)?)$";
    @Autowired


    public static Connection.Response getConnection(String url, int timeoutMillis) throws IOException {
        return Jsoup.connect(url)
                .timeout(timeoutMillis)
                .ignoreContentType(true)  // Игнорируем тип контента
                .ignoreHttpErrors(true)   // Не бросать исключения при HTTP ошибках
                .method(Connection.Method.HEAD)  // Используем HEAD для скорости
                .execute();
    }

    public static boolean checkUrlConnection(String url, int timeoutMillis) {
        try {
            Connection.Response response = getConnection(url, timeoutMillis);
            int statusCode = response.statusCode();
            return (statusCode >= 200 && statusCode < 400);
        } catch (IOException e) {
            return false;
        }
    }

    public static String extractDomain(String url) {

        Matcher matcher = Pattern.compile(regExDomain, Pattern.CASE_INSENSITIVE).matcher(url);
        if (!matcher.find()) {
            return "";
        }
        return matcher.group(1).toLowerCase().trim();
    }
    public static String removeProtocol(String url){
        return url.replaceFirst(NetworkWorker.regExProtocol, "");
    }
    public static String getPagePath(String url){
        String path = url.replaceFirst(regExPath,"");
        if (path.isEmpty()) return "/";
        else return path;
    }
    public static Document getDocument(Connection connection) throws IOException {
        if (connection != null)
            return connection.get();
        else throw new ApiException(MessagesAndErrorCodes.CONNECTION_ERROR);
    }
    public static Connection getConnection(String url, ConnectionConfig connectionConfig){
        return Jsoup.connect(url) .maxBodySize(0)
                .userAgent(connectionConfig.getUserAgent())
                .referrer(connectionConfig.getReferrer())
                .header("Accept-Language", "ru")
                .ignoreHttpErrors(connectionConfig.isIgnoreHttpErrors());

    }

    public static Connection.Response getConnectionResponse(Connection connection){
        try {
            return connection.execute();
        }catch (IOException e) {
            throw new ApiException(MessagesAndErrorCodes.CONNECTION_ERROR);
        }
    }

   public  Document getPageDocument(Connection connection){
       try {
           return connection.get();
       } catch (IOException e) {
           throw new ApiException(MessagesAndErrorCodes.PAGE_INVALID_CONNECT_TO_URL);
       }


   }
}

