package ai.loko.hk.ui.answers;

import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ai.loko.hk.ui.data.Data;
import ai.loko.hk.ui.model.Question;

import static ai.loko.hk.ui.data.Data.USER_AGENT;
import static ai.loko.hk.ui.data.Data.delete;
import static ai.loko.hk.ui.utils.Utils.count;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedQuestion;
import static ai.loko.hk.ui.utils.Utils.getSimplifiedString;
import static ai.loko.hk.ui.utils.Utils.stringToArrayList;

public class Engine extends Base {
    private final String TAG = "Engine";

    public Engine(Question questionObj) {
        super(questionObj);
        startGoogle()
        startArtificialEngine();
        startShophiaEngine();
        startWikiPediaInBackground();
        startBingInBackGround();
        startPeerForExtraOrdanaryAnswer();
        //TODO: Impliment code for unit testing
        //startCustomBlockChainForAnswer();  
        getAnswerFromMicrosoftCortana();
        getAnswerFromGoogleAssistant();
        getAnserFromAmazonAlexa();
        //TODO: Impliment code for unit testing
        //getAnswerFromBlockChainClinet()
    }
    //OTHERS CODE ARE HERE FILL FORM
    }
