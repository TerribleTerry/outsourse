
package com.aleskovacic.pact.pojo;

import java.util.List;

public class CommentsResponse {

    private List<Comment> comments = null;

    /**
     *
     * @return
     *     The comments
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     *
     * @param comments
     *     The comments
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
