package mt.MarvelHerousApp.MVC.Entity;

public class Hero {
    private String name;
    private String description;
    private String power;

    public Hero() {
    }

    public Hero(String name, String description, String power) {
        this.name = name;
        this.description = description;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }
}
