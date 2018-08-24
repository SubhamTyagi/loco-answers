package ai.loko.hk.ui.utils;

import android.support.annotation.Nullable;
import android.util.Pair;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        int lastIndex = 0;
        int cnt = 0;

        while (lastIndex != -1) {
            lastIndex = string.indexOf(subString, lastIndex);

            if (lastIndex != -1) {
                cnt = cnt + 1;
                lastIndex += subString.length();
            }
        }
        return cnt;
    }
   /*
    static int count2(String subString, String string) {
        int cnt = 0;
        Pattern p = Pattern.compile(Pattern.quote(subString));
        Matcher m = p.matcher(string);
        while (m.find())
            cnt++;
        return cnt;
    }
    */

    public static List<String> getCircleButtonData(ArrayList<Pair> piecesAndButtons) {
        List<String> data = new ArrayList<>();
        for (int p = 0; p < PiecePlaceEnum.values().length - 1; p++) {
            for (int b = 0; b < ButtonPlaceEnum.values().length - 1; b++) {
                PiecePlaceEnum piecePlaceEnum = PiecePlaceEnum.getEnum(p);
                ButtonPlaceEnum buttonPlaceEnum = ButtonPlaceEnum.getEnum(b);
                if (piecePlaceEnum.pieceNumber() == buttonPlaceEnum.buttonNumber()
                        || buttonPlaceEnum == ButtonPlaceEnum.Horizontal
                        || buttonPlaceEnum == ButtonPlaceEnum.Vertical) {
                    piecesAndButtons.add(new Pair<>(piecePlaceEnum, buttonPlaceEnum));
                    data.add(piecePlaceEnum + " " + buttonPlaceEnum);
                    if (piecePlaceEnum == PiecePlaceEnum.HAM_1
                            || piecePlaceEnum == PiecePlaceEnum.HAM_2
                            || piecePlaceEnum == PiecePlaceEnum.HAM_3
                            || piecePlaceEnum == PiecePlaceEnum.HAM_4
                            || piecePlaceEnum == PiecePlaceEnum.HAM_5
                            || piecePlaceEnum == PiecePlaceEnum.HAM_6
                            || piecePlaceEnum == PiecePlaceEnum.Share
                            || piecePlaceEnum == PiecePlaceEnum.Custom
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_1
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_2
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_3
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_4
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_5
                            || buttonPlaceEnum == ButtonPlaceEnum.HAM_6
                            || buttonPlaceEnum == ButtonPlaceEnum.Custom) {
                        piecesAndButtons.remove(piecesAndButtons.size() - 1);
                        data.remove(data.size() - 1);
                    }
                }
            }
        }
        return data;
    }
}
