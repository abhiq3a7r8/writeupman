package com.writeupbackend.writeupman.dto;

public class WriteupResponse {
    public String aim;
    public String theory;
    public String conclusion;

    public WriteupResponse(String aim, String theory, String conclusion) {
        this.aim = aim;
        this.theory = theory;
        this.conclusion = conclusion;
    }
    public String getAim() {
        return aim;
    }

    public String getTheory() {
        return theory;
    }

    public String getConclusion() {
        return conclusion;
    }
}
