package mc.image.bed.entity;

import java.util.List;

public class GiteeOssResponse {

    public String sha;
    public String url;
    public List<GiteeOssFile> tree;
    public Boolean truncated;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<GiteeOssFile> getTree() {
        return tree;
    }

    public void setTree(List<GiteeOssFile> tree) {
        this.tree = tree;
    }

    public Boolean getTruncated() {
        return truncated;
    }

    public void setTruncated(Boolean truncated) {
        this.truncated = truncated;
    }
}
