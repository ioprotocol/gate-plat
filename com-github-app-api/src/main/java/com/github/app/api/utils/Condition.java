package com.github.app.api.utils;

public class Condition {
    private String property;
    private Object value;
    private Object secondValue;
    private boolean likeValue = false;
    private boolean notLikeValue = false;
    private boolean betweenValue = false;
    private boolean notBetweenValue = false;
    private boolean equalValue = true;
    private boolean notEqualValue = false;
    private boolean greeterValue = false;
    private boolean greeterEqualValue = false;
    private boolean lessValue = false;
    private boolean lessEqualValue = false;
    private boolean nullValue = false;
    private boolean notNullValue = false;
    private boolean inValue = false;
    private boolean notInValue = false;

    public Condition() {
    }

    public Condition(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    public Condition(String property, Object value, Object secondValue) {
        this.property = property;
        this.value = value;
        this.secondValue = secondValue;
        this.betweenValue = true;
    }

    public String getProperty() {
        return property;
    }

    public Condition setProperty(String property) {
        this.property = property;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public Condition setValue(Object value) {
        this.value = value;
        return this;
    }

    public Object getSecondValue() {
        return secondValue;
    }

    public Condition setSecondValue(Object secondValue) {
        this.secondValue = secondValue;
        return this;
    }

    public boolean isLikeValue() {
        return likeValue;
    }

    public Condition setLikeValue(boolean likeValue) {
        updateAllToFalse();
        this.likeValue = likeValue;
        return this;
    }

    public boolean isBetweenValue() {
        return betweenValue;
    }

    public Condition setBetweenValue(boolean betweenValue) {
        updateAllToFalse();
        this.betweenValue = betweenValue;
        return this;
    }

    public boolean isEqualValue() {
        return equalValue;
    }

    public Condition setEqualValue(boolean equalValue) {
        updateAllToFalse();
        this.equalValue = equalValue;
        return this;
    }

    public boolean isNotLikeValue() {
        return notLikeValue;
    }

    public Condition setNotLikeValue(boolean notLikeValue) {
        updateAllToFalse();
        this.notLikeValue = notLikeValue;
        return this;
    }

    public boolean isNotBetweenValue() {
        return notBetweenValue;
    }

    public Condition setNotBetweenValue(boolean notBetweenValue) {
        updateAllToFalse();
        this.notBetweenValue = notBetweenValue;
        return this;
    }

    public boolean isNotEqualValue() {
        return notEqualValue;
    }

    public Condition setNotEqualValue(boolean notEqualValue) {
        updateAllToFalse();
        this.notEqualValue = notEqualValue;
        return this;
    }

    public boolean isGreeterValue() {
        return greeterValue;
    }

    public Condition setGreeterValue(boolean greeterValue) {
        updateAllToFalse();
        this.greeterValue = greeterValue;
        return this;
    }

    public boolean isGreeterEqualValue() {
        return greeterEqualValue;
    }

    public Condition setGreeterEqualValue(boolean greeterEqualValue) {
        updateAllToFalse();
        this.greeterEqualValue = greeterEqualValue;
        return this;
    }

    public boolean isLessValue() {
        return lessValue;
    }

    public Condition setLessValue(boolean lessValue) {
        updateAllToFalse();
        this.lessValue = lessValue;
        return this;
    }

    public boolean isLessEqualValue() {
        return lessEqualValue;
    }

    public Condition setLessEqualValue(boolean lessEqualValue) {
        updateAllToFalse();
        this.lessEqualValue = lessEqualValue;
        return this;
    }

    public boolean isNullValue() {
        return nullValue;
    }

    public Condition setNullValue(boolean nullValue) {
        updateAllToFalse();
        this.nullValue = nullValue;
        return this;
    }

    public boolean isNotNullValue() {
        return notNullValue;
    }

    public Condition setNotNullValue(boolean notNullValue) {
        updateAllToFalse();
        this.notNullValue = notNullValue;
        return this;
    }

    public boolean isInValue() {
        return inValue;
    }

    public Condition setInValue(boolean inValue) {
        updateAllToFalse();
        this.inValue = inValue;
        return this;
    }

    public boolean isNotInValue() {
        return notInValue;
    }

    public Condition setNotInValue(boolean notInValue) {
        updateAllToFalse();
        this.notInValue = notInValue;
        return this;
    }

    private void updateAllToFalse() {
        this.likeValue = false;
        this.notLikeValue = false;
        this.betweenValue = false;
        this.notBetweenValue = false;
        this.equalValue = false;
        this.notEqualValue = false;
        this.greeterValue = false;
        this.greeterEqualValue = false;
        this.lessValue = false;
        this.lessEqualValue = false;
        this.nullValue = false;
        this.notNullValue = false;
        this.inValue = false;
        this.notInValue = false;
    }
}
