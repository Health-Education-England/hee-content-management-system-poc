package uk.nhs.hee.web.beans;

public class Link {

    public final String label;
    public final String url;

    public Link(String label, String url) {
        this.label = label;
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }
}
