package mt.MarvelHerousApp.Parser;

import mt.MarvelHerousApp.MVC.Entity.Hero;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.context.support.GenericXmlApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HeroesLinksParser {
    private String urlLink;

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    private List<String> parseLinks(String url, List<String> heroesLinks){
        try {
            Document document = Connector.getDocument(url);
            Elements heroesLinksElements = document.getElementsByClass("category-page__members")
                    .select("a");

            heroesLinksElements.forEach(
                    element ->
                        heroesLinks.add( element.absUrl("href"))

            );

            String nextPage = document.selectFirst("div.category-page__pagination")
                    .getElementsByClass("category-page__pagination-next wds-button wds-is-secondary")
                    .first()
                    .select("a")
                    .attr("href");


            if (!nextPage.isEmpty() || !nextPage.equals("")) parseLinks(nextPage,heroesLinks);
            return heroesLinks;

        } catch (IOException  e) {
            e.printStackTrace();
        }catch (NullPointerException  e) {
            System.out.println("Все страницы выгружены! Количество ссылок - " + heroesLinks.size());
        }
        return heroesLinks;
    }

    private static Hero getHero(String heroLink) {
        Hero hero = new Hero();

        try {
            Document document = Connector.getDocument(heroLink);
            Element element = document.getElementsByTag("aside").first();

            hero.setName(getName(element));
            hero.setImageLink(getImageLink(element));
            hero.setNickname(getNickName(element));
            hero.setKind(getKind(element));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return hero;
    }

    private static String getKind(Element element) {
        String kind = element.getElementsByAttributeValue("data-source","Вид")
                .first()
                .getElementsByClass("pi-data-value pi-font")
                .text();
        return !kind.isEmpty()? kind : "-Информация отсутствует-";
    }

    private static String getNickName(Element element) {
        String nickName = element.getElementsByAttributeValue("data-source","Прозвище").text();
        return !nickName.isEmpty()? nickName : "-Информация отсутствует-";
    }

    private static String getImageLink(Element element) {
        Element imageElement = element.getElementsByAttributeValue("data-source","Картинка")
                .first();

         String imageURL = imageElement
                .child(0)
                .absUrl("href");

        return !imageURL.isEmpty() ? imageURL : "-Изображение отсутствует-";
    }

    private static String getName(Element element) {
       String name = element.getElementsByAttributeValue("data-source","Имя").text();
        return !name.isEmpty()? name : "-Информация отсутствует-";
    }

    public static void main(String[] args) {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load("app-context.xml");
        ctx.refresh();
        HeroesLinksParser parser = ctx.getBean("parserBean", HeroesLinksParser.class);

        List<String> heroesLinks = new ArrayList<>();
        heroesLinks = parser.parseLinks(parser.urlLink, heroesLinks);

        /*for (int i = 0; i < 5; i++) {
            Hero hero = getHero(heroesLinks.get(i));
            System.out.println(hero.toString());
        }*/
    }
}
