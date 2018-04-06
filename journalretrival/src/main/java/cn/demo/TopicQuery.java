package cn.demo;


import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public String getformedNarritive() {
        //TODO ugly code, to be refactored
//        System.out.println(this.narrative);
        String content = this.narrative.replace("\n", " ");
//        String[] sentences = content.split("[\\.|,|;]");
        String[] sentences = content.split("[\\.|,|;]");
        Pattern regrex = Pattern.compile(".*not relevant.*");
        List<String> sentenceList =
                Arrays.stream(sentences)
                        .filter((String sentence) -> !regrex.matcher(sentence).matches())
                        .collect(Collectors.toList());
        String updatedNarrative = "";
        for(String sentence : sentenceList) {
            updatedNarrative += sentence;
        }
//        System.out.println(sentences.length +" " +  sentenceList.size());
        return "the" + updatedNarrative ;
    }

    //NOTE could weight the tile more than the description
    public String formNoNarrativeQuery() {
        return this.title + " " + this.description;
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

