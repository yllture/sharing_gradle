package com.neuedu.entity;

import java.util.List;

public class Paper_fill {
    private Integer qId;

    private String qTitle;

    private Integer paperid;

    private String qKnowledge;

    private String qAnalysis;

    private Integer qDifficulty;

    private Integer examinee;

    private Integer qScore;

    private Integer examineescore;
    
    private List<Paper_fillchoice> filllist;

    private String c1;

    private String c2;

    private Integer c3;

    public Integer getqId() {
        return qId;
    }

    public void setqId(Integer qId) {
        this.qId = qId;
    }

    public String getqTitle() {
        return qTitle;
    }

    public void setqTitle(String qTitle) {
        this.qTitle = qTitle == null ? null : qTitle.trim();
    }

    public Integer getPaperid() {
        return paperid;
    }

    public void setPaperid(Integer paperid) {
        this.paperid = paperid;
    }

    public String getqKnowledge() {
        return qKnowledge;
    }

    public void setqKnowledge(String qKnowledge) {
        this.qKnowledge = qKnowledge == null ? null : qKnowledge.trim();
    }

    public String getqAnalysis() {
        return qAnalysis;
    }

    public void setqAnalysis(String qAnalysis) {
        this.qAnalysis = qAnalysis == null ? null : qAnalysis.trim();
    }

    public Integer getqDifficulty() {
        return qDifficulty;
    }

    public void setqDifficulty(Integer qDifficulty) {
        this.qDifficulty = qDifficulty;
    }

    public Integer getExaminee() {
        return examinee;
    }

    public void setExaminee(Integer examinee) {
        this.examinee = examinee;
    }

    public Integer getqScore() {
        return qScore;
    }

    public void setqScore(Integer qScore) {
        this.qScore = qScore;
    }

    public Integer getExamineescore() {
        return examineescore;
    }

    public void setExamineescore(Integer examineescore) {
        this.examineescore = examineescore;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1 == null ? null : c1.trim();
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2 == null ? null : c2.trim();
    }

    public Integer getC3() {
        return c3;
    }

    public void setC3(Integer c3) {
        this.c3 = c3;
    }

	public List<Paper_fillchoice> getFilllist() {
		return filllist;
	}

	public void setFilllist(List<Paper_fillchoice> filllist) {
		this.filllist = filllist;
	}
}