public class Publication{
    private Exchange _exchange;
    private Payload _payload;

    public Payload getPayload(){
        return this._payload;
    }
    public setPayload(Payload payload){
        this._payload=payload;
    }

    public Exchange getExchange(){
        return this._exchange;
    }
    public setPayload(Exchange exchange){
        this._exchange=exchange;
    }
}