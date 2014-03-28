package se.callista.websocketlabs.springmvc.asynch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;

public class RoutingCallbackTask extends AsyncCompletionHandler<Response> {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingCallbackTask.class);

    private long reqId;
    private DeferredResult<String> deferredResult;

    public RoutingCallbackTask(long reqId, DeferredResult<String> deferredResult) {
        this.reqId = reqId;
        this.deferredResult = deferredResult;
    }

    @Override
    public Response onCompleted(Response response) throws Exception{
        // TODO: Handle status codes other than 200...

        if (deferredResult.isSetOrExpired()) {
            LOG.warn("Processing of non-blocking routing #{} already expiered", reqId);        
        } else {
            boolean ok = deferredResult.setResult(response.getResponseBody());
            LOG.info("Processing of non-blocking routing #{} done, http-status = {}, setResult() returned {}", reqId, response.getStatusCode(), ok);        
        }
        return response;
    }

    @Override
    public void onThrowable(Throwable t){
        // TODO: Handle asynchronous processing errors...

        if (deferredResult.isSetOrExpired()) {
            LOG.warn("Processing of non-blocking routing #{} caused an exception: {}", reqId, t);        
        }
    }
}