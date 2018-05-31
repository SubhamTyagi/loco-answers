package tyagi.shubham.lokohack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class FindAnswers {
    private static final String GOOGLE_URL = "https://www.google.com/search?q=";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64)";
    String answer = "";

    private String A, B, C, question;

    private int a = 0, b = 0, c = 0;


    public FindAnswers(String question, String optionA, String optionB, String optionC) {
        A = optionA;
        B = optionB;
        C = optionC;
        this.question = question;

    }

    int getAcount() {
        return a;
    }

    int getBcount() {
        return b;
    }

    int getCcount() {
        return c;
    }

    private int count(String subString, String string) {
        int lastIndex = 0;
        int cnt = 0;

        while (lastIndex != -1) {
            lastIndex = string.indexOf(subString, lastIndex);

            if (lastIndex != -1) {
                cnt++;
                lastIndex += subString.length();
            }
        }
        return cnt;
    }

    private int count2(String subString, String string) {
        int cnt = 0;
        Pattern p = Pattern.compile(Pattern.quote(subString));
        Matcher m = p.matcher(string);
        while (m.find())
            cnt++;
        return cnt;
    }

    public String getAnswer() {
        return answer;
    }

    public void googleSearch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc;
                int max = 0;
                try {
                    doc = Jsoup.connect(GOOGLE_URL + question + "&num=20").userAgent(USER_AGENT).get();
                    String text = doc.body().text();


                    String optionA[] = A.split(" ");
                    for (String option : optionA) {
                        a += count(option, text);
                    }
                    String optionB[] = A.split(" ");
                    for (String option : optionB) {
                        b += count(option, text);
                    }
                    String optionC[] = A.split(" ");
                    for (String option : optionC) {
                        c += count(option, text);
                    }


                    if (a > b) {
                        if (c > a) {
                            //c ic greater c>a>b
                            answer = C;
                            max = c;
                        } else {
                            //a is greater a>c>b
                            answer = A;
                            max = a;
                        }
                    } else if (b > c) {//b is gerater than a check for c and b b>c>a
                        answer = B;
                        max = b;
                    } else { // c is greater c>b>a
                        answer = C;
                        max = c;

                    }


                } catch (IOException ioe) {
                    ioe.printStackTrace();

                    answer = "Error";
                }
                answer += "  :" + max + "";
            }
        }).start();

    }


}
