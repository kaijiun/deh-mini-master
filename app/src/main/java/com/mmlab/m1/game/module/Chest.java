package com.mmlab.m1.game.module;

public class Chest {



    public Chest(int id, int src_id, double lat, double lng, int num, int remain, int point, int distance, int question_type, String question, String option1, String option2, String option3, String option4, String hint1, String hint2, String hint3, String hint4, String answer, int game_id_id, int poi_id_id) {
        this.id = id;
        this.src_id = src_id;
        this.lat = lat;
        this.lng = lng;
        this.num = num;
        this.remain = remain;
        this.point = point;
        this.distance = distance;
        this.question_type = question_type;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.hint1 = hint1;
        this.hint2 = hint2;
        this.hint3 = hint3;
        this.hint4 = hint4;
        this.answer = answer;
        this.game_id_id = game_id_id;
        this.poi_id_id = poi_id_id;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSrc_id() {
        return src_id;
    }

    public void setSrc_id(int src_id) {
        this.src_id = src_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(int question_type) {
        this.question_type = question_type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getHint1() {
        return hint1;
    }

    public void setHint1(String hint1) {
        this.hint1 = hint1;
    }

    public String getHint2() {
        return hint2;
    }

    public void setHint2(String hint2) {
        this.hint2 = hint2;
    }

    public String getHint3() {
        return hint3;
    }

    public void setHint3(String hint3) {
        this.hint3 = hint3;
    }

    public String getHint4() {
        return hint4;
    }

    public void setHint4(String hint4) {
        this.hint4 = hint4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getGame_id_id() {
        return game_id_id;
    }

    public void setGame_id_id(int game_id_id) {
        this.game_id_id = game_id_id;
    }

    public int getPoi_id_id() {
        return poi_id_id;
    }

    public void setPoi_id_id(int poi_id_id) {
        this.poi_id_id = poi_id_id;
    }

    int id;
    int src_id;
    double lat;
    double lng;
    int num;
    int remain;
    int point;
    int distance;
    int question_type;
    String question;
    String option1;
    String option2;
    String option3;
    String option4;
    String hint1;
    String hint2;
    String hint3;
    String hint4;
    String answer;
    int game_id_id;
    int poi_id_id;


}
