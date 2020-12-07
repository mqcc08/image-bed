package mc.image.bed.entity;

import org.apache.http.cookie.Cookie;

import java.util.List;

public class HttpResponseEntity {
    public Integer status;
    public List<Cookie> cookies;
    public String content;

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
