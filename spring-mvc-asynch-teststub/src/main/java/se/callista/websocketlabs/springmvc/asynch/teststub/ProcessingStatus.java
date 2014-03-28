package se.callista.websocketlabs.springmvc.asynch.teststub;

public class ProcessingStatus {

    private final String status;
    private final int processingTimeMs;

    public ProcessingStatus(String status, int processingTimeMs) {
        this.status = status;
        this.processingTimeMs = processingTimeMs;
    }

    public String getStatus() {
        return status;
    }

    public int getProcessingTimeMs() {
        return processingTimeMs;
    }

}