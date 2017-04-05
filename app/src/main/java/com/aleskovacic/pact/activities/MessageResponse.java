package com.aleskovacic.pact.activities;

/**
 * Created by Alex on 03.12.2016.
 */

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class MessageResponse {

    private List<MessageResult> result = new ArrayList<MessageResult>();

    /**
     *
     * @return
     *     The result
     */
    public ArrayList<MessageResult> getResult() {
        return (ArrayList<MessageResult>) result;
    }

    /**
     *
     * @param result
     *     The result
     */
    public void setResult(List<MessageResult> result) {
        this.result = result;
    }

}
