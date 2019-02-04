package mt.MarvelHerousApp.Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.springframework.context.support.GenericXmlApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HeroesParser {
    private String urlLink;

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    private static Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .get();
    }

    private   Map<String, String> parseHeroes( String url, Map<String, String> heroesLinks){
        try {
            Document document = getDocument(url);
            Elements heroesLinksElements = document.getElementsByClass("category-page__members")
                    .select("a");

            heroesLinksElements.forEach(
                    element ->
                        heroesLinks.put(element.text(), element.attr("href"))

            );
            String nextPage = document.selectFirst("div.category-page__pagination")
                    .getElementsByClass("category-page__pagination-next wds-button wds-is-secondary")
                    .first()
                    .select("a")
                    .attr("href");


            if (!nextPage.isEmpty() || !nextPage.equals("")) parseHeroes(nextPage,heroesLinks);
            return heroesLinks;

        } catch (IOException  e) {
            e.printStackTrace();
        }catch (NullPointerException  e) {
            e.printStackTrace();
        }

        return heroesLinks;
    }

    public static void main(String[] args) {
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load("app-context.xml");
        ctx.refresh();
        HeroesParser parser = ctx.getBean("parserBean", HeroesParser.class);

        Map<String, String> heroesLinks = new HashMap<>();
        heroesLinks = parser.parseHeroes(parser.urlLink, heroesLinks);
        System.out.println(heroesLinks.size());
    }
}
