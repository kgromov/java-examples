package html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import java.io.File;
import java.util.logging.Logger;

/**
 * for more examples visit:
 * @link https://jsoup.org/cookbook/
 */
/** implement some useful code using cookbook
 * 1) parse remote page by URL (e.g. development.stagingrevi.com or google);
 * 2) parse html files locally (e.g. change in framed improvements directly), compare execution time;
 * 3) try to rewrite functional of okhttp
*/
public class HtmlUtils {
    protected static final Logger log = Logger.getLogger(HtmlUtils.class.getName());
/*
    public static BuildData getBuildDataFromHTML(String path) {
        File htmlReport = new File(path);
        if (!htmlReport.exists()) return null;
        String html = FileSystem.getFileContent(path);
        Integer invocationCount = 0;
        long buildDuration = 0;
        try {
            invocationCount = Integer.parseInt(DataHelper.phoneTransformation(path.substring(0, path.indexOf("invocation"))));
        } catch (NumberFormatException | StringIndexOutOfBoundsException ignored) {
        }
        BuildData buildData = new BuildData(invocationCount);
        buildData.setBuildNumber(DataHelper.phoneTransformation(path.substring(0, path.indexOf("testng-report"))));
        buildData.setBuildDate(DataHelper.getDateByFormatSimple(DataHelper.REPORT_PATTERN, htmlReport.lastModified()));
        Document doc = Jsoup.parse(html);
        for (Element method : doc.getElementsByClass("method")) {
            if (method.parent().getElementsByClass("testOutput").size() > 0) {
                String testData = method.parent().getElementsByClass("testOutput").get(0).text();
//                System.out.println("testOutput:\n" + testData);
                if (!testData.contains("PollData")) continue;
                PollData pollData = new PollData(testData);
                pollData.addContactAnswers();
                pollData.setMobile(method.text().contains("Mobile"));
                String duration = method.parent().getElementsByClass("duration").get(0).text();
                try {
                    buildDuration += Long.parseLong(duration.substring(0, duration.indexOf("."))) * 1000;
                } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
                }
                pollData.setPollDate(DataHelper.getDateByFormatSimple(DataHelper.REPORT_PATTERN, buildData.getBuildDate(), buildDuration));
                pollData.setSuccess(method.parent().getElementsByClass("stackTrace").size() == 0);
                if (pollData.isSuccess()) {
                    String positionWith = testData.substring(testData.indexOf("CDS form is framed at"), testData.indexOf("PollData{"));
                    pollData.setPosition(Integer.parseInt(DataHelper.phoneTransformation(positionWith)));
                    log.info("String with position\t" + positionWith + "position" + pollData.getPosition());
                }
                buildData.addPollData(pollData, pollData.isSuccess());
            }
        }
        return buildData;
    }

    public static boolean isElementPresentOnPageByClass(String html, String className) {
        try {
            Document doc = Jsoup.parse(html);
            return doc.getElementsByClass(className).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * custom html fragment cleaner example
     */
    public static String cleanHTML(String unsafe) {
        Whitelist whitelist = Whitelist.none();
        whitelist.addAttributes("a", "href");
        whitelist.addAttributes("span", "class");
        return Jsoup.clean(unsafe, whitelist);
    }

 /*   public static void main(String[] args) {
//        String html = FileSystem.getFileContent("C:\\Users\\kgr\\Desktop\\test\\builds\\20132\\testng-report\\html\\suite1_test1_results.html");
        getBuildDataFromHTML("C:\\Users\\kgr\\Desktop\\test\\builds\\22609\\suite1_test1_results.html");
    }*/
}
