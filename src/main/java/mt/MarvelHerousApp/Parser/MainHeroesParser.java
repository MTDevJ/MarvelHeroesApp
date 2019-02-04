package mt.MarvelHerousApp.Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;;
import java.util.Map;

public class MainHeroesParser {

    public static void main(String[] args) {
        String url =
                "https://marvel.fandom.com/ru/wiki/%D0%9A%D0%B0%D1%82%D0%B5%D0%B3%D0%BE%D1%80%D0%B8%D1%8F:%D0%A1%D1%83%D0%BF%D0%B5%D1%80%D0%B3%D0%B5%D1%80%D0%BE%D0%B8";
        Map<String, String> heroesLinks = new HashMap<>();
        heroesLinks = parseHeroes(url, heroesLinks);
       // heroesLinks.forEach((s, s2) -> System.out.println(s));
      //  System.out.println(heroesLinks.size() + " Done!");
    }

    private static Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .get();
    }

    public static Map<String, String> parseHeroes( String url, Map<String, String> heroesLinks){
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


            parseHeroes(nextPage,heroesLinks);

            return heroesLinks;

        } catch (IOException  e) {
            e.printStackTrace();
        } catch (NullPointerException  e) {
        }
        return heroesLinks;
    }
}
