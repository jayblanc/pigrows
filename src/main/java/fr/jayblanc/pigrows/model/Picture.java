package fr.jayblanc.pigrows.model;

public class Picture {

    private String key;
    private String year;
    private String month;
    private String datetime;
    private String name;

    public Picture(String key, String year, String month, String datetime, String name) {
        super();
        this.year = year;
        this.key = key;
        this.month = month;
        this.datetime = datetime;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
