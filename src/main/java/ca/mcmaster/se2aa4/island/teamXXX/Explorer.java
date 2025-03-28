package ca.mcmaster.se2aa4.island.teamXXX;

import java.io.StringReader;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import eu.ace_design.island.bot.IExplorerRaid;

public class Explorer implements IExplorerRaid {

    //************************************************************

    private Decider decider = new Decider();
    ArrayList<String> inout = new ArrayList<>();
    ArrayList<String> poses = new ArrayList<>();
    //************************************************************

    private final Logger logger = LogManager.getLogger();

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);

        logger.info("###################################################################");
        //String direction = info.getString("heading");
        logger.info(direction);

        logger.info("###################################################################");
    }

    @Override
    public String takeDecision() {
        JSONObject decision = new JSONObject();

        //************************************************************
        JSONObject temp = new JSONObject();
        //String[][] commands = decider.decide();
        String[][] commands = decider.decide();
        logger.info("###################################################################");
        logger.info(commands[0][0]);
        inout.add(commands[0][0]);
        if (commands[0][0].equals("stop")) {
            gamestat();
        }
        poses.add(decider.pos_msg);
        logger.info("###################################################################");
        for (int i=0; i<commands.length; i++) {
            if (i == 0) {
                decision.put("action", commands[0][0]);
            } else {
                temp.put(commands[i][0], commands[i][1]);
            }
        }
        if (commands.length > 0) {
            decision.put("parameters", temp);
        }
        //************************************************************


        logger.info("** Decision: {}",decision.toString());
        return decision.toString();
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);


        //************************************************************
        logger.info("###################################################################");
        logger.info(response.toString());
        logger.info("###########################budget########################################");
        String budget = decider.analyse(response);
        logger.info(budget);
        logger.info("###################################################################");

        //************************************************************
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

    //########################################################################################
    public void gamestat() {
        int i = 1;
        int j = 0;
        for (String e : inout) {
            logger.info(j);
            //if (j % 2 == 0) {
            //    logger.info(i);
                i += 1;
            //}
            logger.info(e);
            logger.info(poses.get(j));
            j += 1;
        }
    }
    //########################################################################################

}
