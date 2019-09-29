public class Exchange{
    ChangedFlagEnum changedFlag;
    String clientIdentification;
    boolean deliveryBreak;
    DenyReasonEnum denyReason;
    Date historicalStartDate;
    Date historicalStopDate;
    boolean keepAlive;
    RequestTypeEnum requestType;
    ResponseEnum response;
    String subscriptionReference;

    public ChangedFlagEnum getChangedFlag() {
        return changedFlag;
    }
    public void setChangedFlag(ChangedFlagEnum changedFlag) {
        this.changedFlag = changedFlag;
    }
    public String getClientIdentification() {
        return clientIdentification;
    }
    public void setClientIdentification(String clientIdentification) {
        this.clientIdentification = clientIdentification;
    }
    public boolean isDeliveryBreak() {
        return deliveryBreak;
    }
    public void setDeliveryBreak(boolean deliveryBreak) {
        this.deliveryBreak = deliveryBreak;
    }
    public DenyReasonEnum getDenyReason() {
        return denyReason;
    }
    public void setDenyReason(DenyReasonEnum denyReason) {
        this.denyReason = denyReason;
    }
    public Date getHistoricalStartDate() {
        return historicalStartDate;
    }
    public void setHistoricalStartDate(Date historicalStartDate) {
        this.historicalStartDate = historicalStartDate;
    }
    public Date getHistoricalStopDate() {
        return historicalStopDate;
    }
    public void setHistoricalStopDate(Date historicalStopDate) {
        this.historicalStopDate = historicalStopDate;
    }
    public boolean isKeepAlive() {
        return keepAlive;
    }
    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
    public RequestTypeEnum getRequestType() {
        return requestType;
    }
    public void setRequestType(RequestTypeEnum requestType) {
        this.requestType = requestType;
    }
    public ResponseEnum getResponse() {
        return response;
    }
    public void setResponse(ResponseEnum response) {
        this.response = response;
    }
    public String getSubscriptionReference() {
        return subscriptionReference;
    }
    public void setSubscriptionReference(String subscriptionReference) {
        this.subscriptionReference = subscriptionReference;
    }
}
