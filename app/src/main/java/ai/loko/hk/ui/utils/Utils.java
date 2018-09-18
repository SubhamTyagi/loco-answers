package ai.loko.hk.ui.utils;

import android.support.annotation.Nullable;
import android.util.Pair;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.loko.hk.ui.data.Data;

public class Utils {


    public static String getSimplifiedString(String text, @Nullable String optionalRemoveWord) {
        String split[] = text.split(" ");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(split));
        list.removeAll(Data.removeWords);

        if (optionalRemoveWord != null) {
            String x[] = optionalRemoveWord.split(" ");
            ArrayList<String> x2 = new ArrayList<>(Arrays.asList(x));
            list.removeAll(x2);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : list) {
            stringBuilder.append(s).append(" ");
        }
        return stringBuilder.toString();
    }

    public static ArrayList<String> stringToArrayList(String text) {
        String split[] = text.split(" ");
        return new ArrayList<>(Arrays.asList(split));
    }

    public static ArrayList<String> getSimplifiedQuestion(String question) {
        String splitQuestion[] = question.split(" ");
        ArrayList<String> simplifiedQuestion = new ArrayList<>(Arrays.asList(splitQuestion));
        simplifiedQuestion.removeAll(Data.removeWords);
        return simplifiedQuestion;
    }

    public static ArrayList<String> getSimplifiedQuestion(String question, int a) {
        String splitQuestion[] = question.split(" ");
        ArrayList<String> simplifiedQuestion = new ArrayList<>(Arrays.asList(splitQuestion));
        simplifiedQuestion.removeAll(Data.removeNegativeWords);
        return simplifiedQuestion;
    }


    public static int count(String subString, String string) {
        int cnt = 0;
        String regex="\\b"+subString+"\\b";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);
        while (m.find())
            cnt++;
        return cnt;
    }
}
