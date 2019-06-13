package model;

public class Tester {

    private String name;
    private String country;
    private String device;
    private int experience;

    public Tester(String name, String country, String device, int experience) {
        this.name = name;
        this.country = country;
        this.device = device;
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public String getDevice() {
        return device;
    }

    public String getCountry() {
        return country;
    }

    public int getExperience() {
        return experience;
    }

    @Override
    public String toString() {
        return "Tester{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", device='" + device + '\'' +
                ", experience=" + experience +
                '}';
    }
}
