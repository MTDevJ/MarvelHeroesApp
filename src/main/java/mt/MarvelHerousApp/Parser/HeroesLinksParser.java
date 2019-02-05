package mt.MarvelHerousApp.Parser;

import mt.MarvelHerousApp.MVC.Entity.Hero;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.context.support.GenericXmlApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HeroesLinksParser {
    private static final Logger logger = LogManager.getLogger(HeroesLinksParser.class.getName());
    private String urlLink;
    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    private List<String> parseLinks(String url, List<String> heroesLinks){
        logger.info("Start parsing");
        try {
            Document document = Connector.getDocument(url);
            Elements heroesLinksElements = document.getElementsByClass("category-page__members")
                    .select("a");
            heroesLinksElements
                    .forEach(
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
            logger.error("IOException error", e);
            e.printStackTrace();
        }catch (NullPointerException  e) {
            logger.error("NullPointerException error", e);
            logger.info("Все страницы выгружены! Количество ссылок - " + heroesLinks.size());
        }
        logger.info("Parsing is done");
        return heroesLinks;
    }

    private static Hero getHero(String heroLink) {
        Hero hero = new Hero();
        try {
            Document document = Connector.getDocument(heroLink);
            Element element = document.getElementsByTag("aside").first();

            if (element != null) {
                hero.setName(getName(element));
                hero.setImageLink(getImageLink(element));
                hero.setNickname(getNickName(element));
                hero.setKind(getKind(element));
            }
        } catch (IOException e) {
            logger.error("IOException error", e);
            e.printStackTrace();
        }
        return hero;
    }

    private static String getKind(Element element) {
        String kind;
        try {
            kind = element.getElementsByAttributeValue("data-source","Вид")
                    .first()
                    .getElementsByClass("pi-data-value pi-font")
                    .text();
        } catch (NullPointerException e) {
            logger.error("NullPointerException error", e);
            return "-Информация отсутствует-";
        }
        return !kind.isEmpty()? kind : "-Информация отсутствует-";
    }

    private static String getNickName(Element element) {
        String nickName = element.getElementsByAttributeValue("data-source","Прозвище").text();
        return !nickName.isEmpty()? nickName : "-Информация отсутствует-";
    }

    private static String getImageLink(Element element) {

        String imageURL;
        try {
            Element imageElement = element.getElementsByAttributeValue("data-source","Картинка")
                    .first();

            imageURL = imageElement
                   .child(0)
                   .absUrl("href");
        } catch (NullPointerException e) {
            logger.error("NullPointerException error", e);
            return "-Изображение отсутствует-";
        }
        return !imageURL.isEmpty() ? imageURL : "-Изображение отсутствует-";
    }

    private static String getName(Element element) {
       String name = element.getElementsByAttributeValue("data-source","Имя").text();
        return !name.isEmpty()? name : "-Информация отсутствует-";
    }

    private static List<Hero> getHeroes(List<String> heroesLinks) {
        logger.info("Start collecting heroes");
        List<Hero> heroes = new ArrayList<>();
        heroesLinks.forEach(link -> {
            Hero hero = getHero(link);
           if (hero.getName() != null) {
               heroes.add(hero);
               logger.info("Персонаж " + hero.getName() + "добавлен в список");
           }
        });
        logger.info("Collecting heroes is done");
        return heroes;
    }

    public static void main(String[] args) {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load("app-context.xml");
        ctx.refresh();
        HeroesLinksParser parser = ctx.getBean("parserBean", HeroesLinksParser.class);

        List<String> heroesLinks = new ArrayList<>();
        heroesLinks = parser.parseLinks(parser.urlLink, heroesLinks);

        List<Hero> heroes = getHeroes(heroesLinks);

        System.out.println("Итого персонажей " + heroes.size());
    }
}
