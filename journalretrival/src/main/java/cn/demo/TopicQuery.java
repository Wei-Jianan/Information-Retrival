package cn.demo;


public class TopicQuery {
    private int num;
    private String title;
    private String description;
    private String narrative;

    @Override
    public String toString() {
        return String.format("{\nnum: %d\ntitle: %s\ndescription: %s\nnarrative: %s\n}",
                this.num, this.title, this.description, this.narrative);
    }
    public String formQuery( ) {
        return this.title  + this.description;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String lines) {
        this.description = lines;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }
}

