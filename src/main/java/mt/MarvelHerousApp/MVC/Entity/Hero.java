package mt.MarvelHerousApp.MVC.Entity;

import java.util.Objects;

public class Hero {
    private String imageLink;
    private String name;
    private String nickname;
    private String kind;


    public Hero() {
    }

    public Hero(String imageLink, String name, String nickname, String kind) {
        this.imageLink = imageLink;
        this.name = name;
        this.nickname = nickname;
        this.kind = kind;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return name.equals(hero.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return  "Hero : " + name + "\n" +
                "Прозвище = " + nickname + "\n" +
                "Вид = " + kind + "\n";
    }
}
