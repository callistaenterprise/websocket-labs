package se.callista.websocketlabs.springmvc.asynch;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

@RestController
public class RoutingController {
    
    private static final Logger LOG = LoggerFactory.getLogger(RoutingController.class);

    private static final AtomicLong lastRequestId = new AtomicLong(0);
    private RestTemplate      restTemplate       = new RestTemplate();
    private AsyncRestTemplate asyncRestTemplate  = new AsyncRestTemplate();
    private AsyncHttpClient   asyncHttpClient    = new AsyncHttpClient();
    
    private String SP_BLOCKING_URL     = "http://localhost:9090/process-blocking";
    private String SP_NON_BLOCKING_URL = "http://localhost:9090/process-non-blocking";

    @RequestMapping("/route-blocking")
    public String blockingRouting(
        @RequestParam(value = "minMs", required = false, defaultValue = "0") int minMs,
        @RequestParam(value = "maxMs", required = false, defaultValue = "0") int maxMs) {

        long reqId = lastRequestId.getAndIncrement();
        
        LOG.info("Start blocking routing #{}", reqId);

        ResponseEntity<String> result = restTemplate.getForEntity(
            SP_BLOCKING_URL + "?minMs={minMs}&maxMs={maxMs}", String.class, minMs, maxMs);        

        // TODO: Handle status codes other than 200...
        
        LOG.info("Routing of blocking request #{} is done, status: {}", reqId, result.getStatusCode());        

        return result.getBody();
    }

    @RequestMapping("/route-non-blocking")
    public DeferredResult<String> nonBlockingRouting(
        @RequestParam(value = "minMs", required = false, defaultValue = "0") int minMs,
        @RequestParam(value = "maxMs", required = false, defaultValue = "0") int maxMs) throws IOException {

        long reqId = lastRequestId.getAndIncrement();
        
        LOG.info("Start non-blocking routing #{}.", reqId);

        DeferredResult<String> deferredResult = new DeferredResult<>();

        asyncHttpClient.prepareGet(SP_NON_BLOCKING_URL + "?minMs=" + minMs + "&maxMs=" + maxMs).execute(new RoutingCallbackTask(reqId, deferredResult));                

        LOG.info("Processing of non-blocking routing #{} leave the request thread", reqId);

        // Return to let go of the precious thread we are holding on to...
        return deferredResult;
    }
    
    /**
     * The spring version of asynch http cleint has two major drawbacks
     * 1. It doesn't work with the code below, no call is made to the SP
     * 2. The call is not executed non-blocking but i in a separate thread, i.e. it doesn't scale very good...
     * 
     * @param minMs
     * @param maxMs
     * @return
     * @throws IOException
     */
    @RequestMapping("/route-non-blocking-spring")
    public DeferredResult<String> nonBlockingRouting_Spring(
        @RequestParam(value = "minMs", required = false, defaultValue = "0") int minMs,
        @RequestParam(value = "maxMs", required = false, defaultValue = "0") int maxMs) throws IOException {

        long reqId = lastRequestId.getAndIncrement();
        
        LOG.info("Start non-blocking routing #{}.", reqId);

        DeferredResult<String> deferredResult = new DeferredResult<>();

        ListenableFuture<ResponseEntity<String>> futureEntity = asyncRestTemplate.getForEntity(
            SP_NON_BLOCKING_URL + "?minMs={minMs}&maxMs={maxMs}", String.class, minMs, maxMs);        

        // Register a callback for the completion of the asynchronous rest call
        futureEntity.addCallback(new RoutingCallbackTask_Spring_AsyncRestTemplate(reqId, deferredResult));

        LOG.info("Processing of non-blocking routing #{} leave the request thread", reqId);

        // Return to let go of the precious thread we are holding on to...
        return deferredResult;
    }
}